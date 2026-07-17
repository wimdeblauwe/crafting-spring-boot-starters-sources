package com.example.queuepoller;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

public class MultipleMessageHandlersFailureAnalyzer extends AbstractFailureAnalyzer<MultipleMessageHandlersException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, MultipleMessageHandlersException cause) {
        String description = """
                The queue-poller starter requires exactly one MessageHandler bean, but found %d:
                %s""".formatted(
                cause.handlerBeanNames().size(),
                cause.handlerBeanNames().stream().map(name -> "  - " + name).reduce((a, b) -> a + "\n" + b).orElse(""));

        String action = """
                Register a single MessageHandler bean, or mark one of the existing beans as @Primary.
                If your application genuinely needs multiple handlers, compose them into a single
                MessageHandler that delegates to the others.""";

        return new FailureAnalysis(description, action, cause);
    }
}
