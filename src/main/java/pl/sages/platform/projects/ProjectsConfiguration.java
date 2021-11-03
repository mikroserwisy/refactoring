package pl.sages.platform.projects;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
public class ProjectsConfiguration {

    @Bean
    public Queue initProjectQueue(@Value("${platform.init-project-queue}") String queueName) {
        return new Queue(queueName);
    }

    @Bean
    public Queue runCommandQueue(@Value("${platform.run-command-queue}") String queueName) {
        return new Queue(queueName);
    }

}
