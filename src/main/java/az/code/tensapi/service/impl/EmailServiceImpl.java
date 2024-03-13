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
    String supportEmail;

    @Override
    public void sendConfirmationEmail(String to, String confirmationToken) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("tensrapidteam@gmail.com");
        message.setTo(to);
        message.setSubject("Confirmation Email");
        message.setText(
                "Welcome to our application! Please confirm your email address by clicking the link below:\n\n"
                + "%s/api/v1/confirm?confirmationToken=".formatted("localhost:8080") + confirmationToken);
        javaMailSender.send(message);
    }
}
