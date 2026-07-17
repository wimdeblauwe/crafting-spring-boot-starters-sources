package com.example.queuepoller;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

public class InvalidReceiveTimeoutFailureAnalyzer extends AbstractFailureAnalyzer<InvalidReceiveTimeoutException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, InvalidReceiveTimeoutException cause) {
        String description = """
                queue-poller.receive-timeout is set to %s, which is not a positive duration.
                A non-positive timeout causes the poller's receive call to return immediately,
                turning the worker into a CPU-bound spin loop on the broker.""".formatted(cause.value());

        String action = """
                Set queue-poller.receive-timeout to a positive duration in your application.yaml,
                for example:
                  queue-poller:
                    receive-timeout: 100ms
                Omit the property entirely to use the starter's default of 50ms.""";

        return new FailureAnalysis(description, action, cause);
    }
}
