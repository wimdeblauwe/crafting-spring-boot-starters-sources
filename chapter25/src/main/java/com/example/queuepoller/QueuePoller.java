package com.example.queuepoller;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

public class QueuePoller implements SmartLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(QueuePoller.class);

    private final InMemoryBroker broker;
    private final MessageHandler handler;
    private final String queueName;
    private final Duration receiveTimeout;
    private final ThreadFactory threadFactory;
    private final ObservationRegistry observationRegistry;
    private final QueuePollerObservationConvention userConvention;
    private final QueuePollerObservationConvention defaultConvention;

    private final Object lifecycleLock = new Object();
    private volatile boolean running;
    private Thread worker;
    private CountDownLatch stopped;

    public QueuePoller(InMemoryBroker broker,
                       MessageHandler handler,
                       String queueName,
                       Duration receiveTimeout,
                       ThreadFactory threadFactory,
                       ObservationRegistry observationRegistry,
                       QueuePollerObservationConvention userConvention,
                       QueuePollerObservationConvention defaultConvention) {
        this.broker = broker;
        this.handler = handler;
        this.queueName = queueName;
        this.receiveTimeout = receiveTimeout;
        this.threadFactory = threadFactory;
        this.observationRegistry = observationRegistry;
        this.userConvention = userConvention;
        this.defaultConvention = defaultConvention;
    }

    @Override
    public void start() {
        synchronized (lifecycleLock) {
            if (running) {
                return;
            }
            running = true;
            stopped = new CountDownLatch(1);
            worker = threadFactory.newThread(this::loop);
            worker.setName("queue-poller-" + queueName);
            worker.start();
            logger.info("Started polling '{}' (receive timeout {})", queueName, receiveTimeout);
        }
    }

    @Override
    public void stop() {
        Thread toJoin;
        CountDownLatch toAwait;
        synchronized (lifecycleLock) {
            if (!running) {
                return;
            }
            running = false;
            toJoin = worker;
            toAwait = stopped;
            if (toJoin != null) {
                toJoin.interrupt();
            }
        }
        try {
            if (!toAwait.await(5, TimeUnit.SECONDS)) {
                logger.warn("Poller for '{}' did not stop within 5s", queueName);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        logger.info("Stopped polling '{}'", queueName);
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return DEFAULT_PHASE;
    }

    private void loop() {
        try {
            while (running) {
                pollOnce();
            }
        } finally {
            running = false;
            stopped.countDown();
        }
    }

    // tag::poll-once[]
    private void pollOnce() {
        QueuePollerContext context = new QueuePollerContext(queueName); // <1>
        Observation observation = QueuePollerObservation.POLL_CYCLE
                .observation(userConvention, defaultConvention, () -> context, observationRegistry) // <2>
                .start();
        try (Observation.Scope scope = observation.openScope()) { // <3>
            String message = broker.poll(queueName, receiveTimeout);
            if (message != null) {
                context.setOutcome("received"); // <4>
                dispatch(message);
            } else {
                context.setOutcome("empty");
            }
        } catch (InterruptedException ex) {
            context.setOutcome("interrupted");
            Thread.currentThread().interrupt();
            running = false;
        } catch (Throwable t) {
            context.setOutcome("error");
            observation.error(t); // <5>
        } finally {
            observation.stop();
        }
    }
    // end::poll-once[]

    private void dispatch(String message) {
        try {
            handler.handle(message);
        } catch (Throwable t) {
            logger.error("Handler failed for message from '{}': {}", queueName, message, t);
        }
    }
}
