package pl.sages.platform.emails;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    @NonNull
    private final Queue sendEmailQueue;
    @NonNull
    private final RabbitTemplate jmsTemplate;
    @NonNull
    private final JavaMailSender mailSender;
    @Value("${platform.email-sender}")
    @Setter
    private String sender;

    public void send(MailMessage mailMessage) {
        jmsTemplate.convertAndSend(sendEmailQueue.getName(), mailMessage);
    }

    @RabbitListener(queues = "${platform.send-email-queue}")
    private void onSendMailMessage(MailMessage message) {
        MimeMessagePreparator messagePreparator = createMimeMessagePreparator(message);
        try {
            mailSender.send(messagePreparator);
        } catch (MailException exception) {
            log.warn("Unable to send email: " + exception.getMessage());
        }
    }

    private MimeMessagePreparator createMimeMessagePreparator(MailMessage message) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(sender);
            messageHelper.setTo(message.getRecipient());
            messageHelper.setSubject(message.getSubject());
            messageHelper.setText(message.getText(), true);
        };
    }

}
