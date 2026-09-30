package com.loanapp.service;

import com.loanapp.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class NotificationService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}") private String from;
    @Value("${app.twilio.account-sid:}") private String twilioSid;
    @Value("${app.twilio.auth-token:}") private String twilioToken;
    @Value("${app.twilio.from:}") private String twilioFrom;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendEmail(String to, String subject, String html) {
        if (to == null || to.isBlank() || from == null || from.isBlank()) return false;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
            return true;
        } catch (MessagingException | RuntimeException ex) {
            return false;
        }
    }

    public boolean sendPasswordSetupEmail(User user, String link, long expiresHours) {
        String name = escape(user.getFullName());
        String safeLink = escape(link);
        String html = """
            <div style="font-family:Arial,sans-serif;max-width:620px;margin:auto;padding:28px;color:#172033">
              <h2 style="color:#2563eb">JmkLoanApp</h2>
              <p>Habari %s,</p>
              <p>Akaunti yako imekubaliwa. Tumia kitufe hapa chini kuweka password yako.</p>
              <p><a href="%s" style="display:inline-block;background:#2563eb;color:#fff;padding:13px 20px;border-radius:9px;text-decoration:none;font-weight:bold">Weka Password</a></p>
              <p>Link hii ita-expire baada ya %d hours.</p>
              <p>Usishiriki link hii na mtu mwingine.</p>
              <hr><small>JmkLoanApp • Ujumbe wa usalama</small>
            </div>
            """.formatted(name, safeLink, expiresHours);
        return sendEmail(user.getEmail(), "JmkLoanApp - Weka Password ya Akaunti Yako", html);
    }

    public boolean sendPasswordResetEmail(User user, String link, long expiresMinutes) {
        String name = escape(user.getFullName());
        String safeLink = escape(link);
        String html = """
            <div style="font-family:Arial,sans-serif;max-width:620px;margin:auto;padding:28px;color:#172033">
              <h2 style="color:#2563eb">JmkLoanApp</h2>
              <p>Habari %s,</p>
              <p>Tumepokea ombi la kubadilisha password ya akaunti yako.</p>
              <p><a href="%s" style="display:inline-block;background:#2563eb;color:#fff;padding:13px 20px;border-radius:9px;text-decoration:none;font-weight:bold">Reset Password</a></p>
              <p>Link hii ita-expire baada ya dakika %d na inaweza kutumika mara moja tu.</p>
              <p>Kama hukuomba reset, unaweza kupuuza ujumbe huu.</p>
              <hr><small>JmkLoanApp • Ujumbe wa usalama</small>
            </div>
            """.formatted(name, safeLink, expiresMinutes);
        return sendEmail(user.getEmail(), "JmkLoanApp - Password Reset", html);
    }

    public void sendOverdue(User user, String message) {
        if (user.getEmail() != null && !user.getEmail().isBlank() && from != null && !from.isBlank()) {
            sendEmail(user.getEmail(), "Jmk Loan - Taarifa ya malipo yaliyochelewa",
                    "<div style='font-family:Arial,sans-serif'><p>Habari " + escape(user.getFullName()) +
                    ",</p><p>" + escape(message) + "</p></div>");
        }
        sendSms(user.getPhone(), message);
    }

    public boolean sendSms(String to, String body) {
        if (twilioSid.isBlank() || twilioToken.isBlank() || twilioFrom.isBlank() || to == null || to.isBlank()) return false;
        try {
            String form = "To=" + enc(to) + "&From=" + enc(twilioFrom) + "&Body=" + enc(body);
            String auth = Base64.getEncoder().encodeToString((twilioSid + ":" + twilioToken).getBytes(StandardCharsets.UTF_8));
            HttpRequest req = HttpRequest.newBuilder(
                    URI.create("https://api.twilio.com/2010-04-01/Accounts/" + twilioSid + "/Messages.json"))
                    .header("Authorization", "Basic " + auth)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form)).build();
            return HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString()).statusCode() < 300;
        } catch (Exception e) {
            return false;
        }
    }

    private String enc(String s) { return java.net.URLEncoder.encode(s, StandardCharsets.UTF_8); }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;")
                .replace("\"","&quot;").replace("'","&#39;");
    }
}
