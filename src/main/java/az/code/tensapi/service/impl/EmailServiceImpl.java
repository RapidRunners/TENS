package az.code.tensapi.service.impl;


import az.code.tensapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sentMailMessage(String to, String notification) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Task Notification");
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(notification);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}