package com.loanapp.config;

import com.loanapp.model.User;
import com.loanapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("joseph@jmkloanapp.co.tz").isEmpty()) {
            User admin = User.builder()
                    .fullName("Joseph")
                    .email("joseph@jmkloanapp.co.tz")
                    .phone("+255700000000")
                    .nidaNumber("00000000000000000000")
                    .password(passwordEncoder.encode("Joseph@2026"))
                    .role(User.Role.ADMIN)
                    .status(User.UserStatus.APPROVED)
                    .active(true)
                    .build();

            userRepository.save(admin);
            System.out.println("========================================");
            System.out.println("✅ ADMIN AMEUNDWA!");
            System.out.println("   Email: joseph@jmkloanapp.co.tz");
            System.out.println("   Password: Joseph@2026");
            System.out.println("========================================");
        } else {
            System.out.println("✅ Admin tayari yupo kwenye database");
        }
    }
}
