package com.example.sample;

import com.example.queuepoller.DefaultQueuePollerObservationConvention;
import com.example.queuepoller.QueuePollerContext;
import com.example.queuepoller.QueuePollerObservationConvention;
import io.micrometer.common.KeyValues;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("demo.custom-convention")
public class CustomConventionConfiguration {

    @Bean
    public QueuePollerObservationConvention environmentTagConvention() {
        DefaultQueuePollerObservationConvention base = new DefaultQueuePollerObservationConvention();
        return new QueuePollerObservationConvention() {

            @Override
            public String getName() {
                return base.getName();
            }

            @Override
            public String getContextualName(QueuePollerContext context) {
                return base.getContextualName(context);
            }

            @Override
            public KeyValues getLowCardinalityKeyValues(QueuePollerContext context) {
                return base.getLowCardinalityKeyValues(context)
                        .and("deployment.env", "staging");
            }
        };
    }
}
