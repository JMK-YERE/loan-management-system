package com.loanapp.config;

import com.loanapp.model.User;
import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}") private String adminEmail;
    @Value("${app.admin.password}") private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail(adminEmail).isPresent()) return;
        User admin = User.builder()
                .fullName("Administrator")
                .email(adminEmail)
                .phone("+255700000001")
                .nidaNumber("00000000000000000001")
                .password(passwordEncoder.encode(adminPassword))
                .role(User.Role.ADMIN)
                .status(User.UserStatus.APPROVED)
                .active(true)
                .build();
        userRepository.save(admin);
        System.out.println("Admin created: " + adminEmail);
    }
}
