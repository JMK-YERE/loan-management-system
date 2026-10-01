package com.loanapp.config;

import com.loanapp.model.User;
import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DemoUserSeeder {
 @Bean
 CommandLineRunner seedDemoUsers(UserRepository repo, PasswordEncoder encoder,
   @Value("${app.demo.seed-enabled:false}") boolean enabled,
   @Value("${app.demo.lender-password:}") String lenderPassword,
   @Value("${app.demo.borrower-password:}") String borrowerPassword) {
  return args -> {
   if(!enabled || lenderPassword.isBlank() || borrowerPassword.isBlank()) return;
   upsert(repo,encoder,"JMK Demo Lender","lender.demo@jmkloan.co.tz","+255710000001","DEMO-NIDA-LENDER",User.Role.LENDER,lenderPassword);
   upsert(repo,encoder,"JMK Demo Borrower","borrower.demo@jmkloan.co.tz","+255710000002","DEMO-NIDA-BORROWER",User.Role.BORROWER,borrowerPassword);

  };
 }
 private void upsert(UserRepository repo, PasswordEncoder encoder, String name,String email,String phone,String nida,User.Role role,String password){
  User u=repo.findByEmail(email).orElseGet(()->User.builder().fullName(name).email(email).phone(phone).nidaNumber(nida).role(role).build());
  u.setFullName(name);u.setPhone(phone);u.setNidaNumber(nida);u.setRole(role);u.setStatus(User.UserStatus.APPROVED);u.setActive(true);u.setPassword(encoder.encode(password));u.setCity("Dodoma");u.setCountry("Tanzania");u.setIdType("NIDA");u.setNationality("Tanzanian");
  repo.save(u);
 }
}
