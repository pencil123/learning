package lng.bridge.learning.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;


@ConditionalOnProperty(value="app.scheduling.enable",havingValue = "true",matchIfMissing = true)
@Configuration
public class ScheduledConfig {
    @Bean
    public ScheduledAnnotationBeanPostProcessor processor(){
        return new ScheduledAnnotationBeanPostProcessor();
    }
}
