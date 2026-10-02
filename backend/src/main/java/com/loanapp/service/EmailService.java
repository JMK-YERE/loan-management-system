package com.loanapp.service;

import com.loanapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${app.frontend.url:https://jmkloanapp-frontend.onrender.com}")
    private String frontendUrl;

    @Value("${app.company.name:JMK Loan App}")
    private String companyName;

    public void sendPasswordResetEmail(User user, String token) {
        String resetLink = frontendUrl + "/reset-password?token=" + token;

        String subject = "Weka Password Mpya - " + companyName;
        String body = "Habari " + user.getFullName() + ",\n\n"
                + "Tumepokea ombi la kubadilisha password yako.\n\n"
                + "Bonyeza link hii kuweka password mpya:\n"
                + resetLink + "\n\n"
                + "Link hii itaisha baada ya saa 24.\n\n"
                + "Kama hukuomba kubadilisha password, puuza email hii.\n\n"
                + "Asante,\n"
                + companyName;

        sendEmail(user.getEmail(), subject, body);
    }

    public void sendEmail(String to, String subject, String body) {
        if (mailSender == null) {
            System.out.println("=== EMAIL MOCK ===");
            System.out.println("To: " + to);
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);
            System.out.println("==================");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("✅ Email imetumwa kwa: " + to);
        } catch (Exception e) {
            System.err.println("❌ Email error: " + e.getMessage());
            System.out.println("=== EMAIL FALLBACK ===");
            System.out.println("To: " + to);
            System.out.println("Body: " + body);
            System.out.println("======================");
        }
    }
}
