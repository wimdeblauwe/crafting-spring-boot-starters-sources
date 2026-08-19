package com.example.requestid;

import java.util.Collections;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnNonEmptyMapPropertyCondition extends SpringBootCondition { // <1>

    private static final Bindable<Map<String, String>> STRING_STRING_MAP =
            Bindable.mapOf(String.class, String.class);

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) { // <2>
        String propertyPrefix = metadata.getAnnotations()
                .get(ConditionalOnNonEmptyMapProperty.class)
                .getString(MergedAnnotation.VALUE); // <3>

        boolean empty = Binder.get(context.getEnvironment())
                .bind(propertyPrefix, STRING_STRING_MAP)
                .orElse(Collections.emptyMap())
                .isEmpty(); // <4>

        ConditionMessage.Builder message = ConditionMessage
                .forCondition(ConditionalOnNonEmptyMapProperty.class, "'%s'".formatted(propertyPrefix));
        return !empty
                ? ConditionOutcome.match(message.because("bound to a non-empty map"))
                : ConditionOutcome.noMatch(message.because("bound to an empty map")); // <5>
    }
}
