package lng.bridge.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@SpringBootApplication
//@EnableScheduling
public class LearningApplication {
    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(LearningApplication.class, args);
    }
}
