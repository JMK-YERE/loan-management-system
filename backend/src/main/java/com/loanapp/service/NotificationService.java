package com.loanapp.service;

import com.loanapp.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
 public NotificationService(JavaMailSender mailSender){this.mailSender=mailSender;}
 public void sendOverdue(User user,String message){
  if(user.getEmail()!=null && !user.getEmail().isBlank() && !from.isBlank()){
   try{SimpleMailMessage m=new SimpleMailMessage();m.setFrom(from);m.setTo(user.getEmail());m.setSubject("JMK Loan - Taarifa ya malipo yaliyochelewa");m.setText(message);mailSender.send(m);}catch(Exception ignored){}
  }
  sendSms(user.getPhone(),message);
 }
 public boolean sendSms(String to,String body){
  if(twilioSid.isBlank()||twilioToken.isBlank()||twilioFrom.isBlank()||to==null||to.isBlank()) return false;
  try{
   String form="To="+enc(to)+"&From="+enc(twilioFrom)+"&Body="+enc(body);
   String auth=Base64.getEncoder().encodeToString((twilioSid+":"+twilioToken).getBytes(StandardCharsets.UTF_8));
   HttpRequest req=HttpRequest.newBuilder(URI.create("https://api.twilio.com/2010-04-01/Accounts/"+twilioSid+"/Messages.json"))
    .header("Authorization","Basic "+auth).header("Content-Type","application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(form)).build();
   return HttpClient.newHttpClient().send(req,HttpResponse.BodyHandlers.ofString()).statusCode()<300;
  }catch(Exception e){return false;}
 }
 private String enc(String s){return java.net.URLEncoder.encode(s,StandardCharsets.UTF_8);}
}
