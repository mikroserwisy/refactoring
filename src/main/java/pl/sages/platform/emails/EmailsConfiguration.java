package pl.sages.platform.emails;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailsConfiguration {

    @Bean
    public Queue sendEmailQueue(@Value("${platform.send-email-queue}") String queueName) {
        return new Queue(queueName);
    }

}
