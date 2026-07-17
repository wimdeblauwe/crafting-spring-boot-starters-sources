package com.example.requestid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Conditional(OnNonEmptyMapPropertyCondition.class) // <1>
public @interface ConditionalOnNonEmptyMapProperty {

    /**
     * The property prefix whose map value must contain at least one entry.
     */
    String value(); // <2>
}
