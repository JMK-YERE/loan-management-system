package com.loanapp.service;

import com.loanapp.dto.AuthResponse;
import com.loanapp.dto.LoginRequest;
import com.loanapp.dto.RegisterRequest;
import com.loanapp.model.User;
import com.loanapp.repository.UserRepository;
import com.loanapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Barua pepe imetumika tayari");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Namba ya simu imetumika tayari");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .nidaNumber(request.getNidaNumber())
                .role(request.getRole())
                .status(User.UserStatus.PENDING)
                .active(false)
                .build();

        userRepository.save(user);
        return "Usajili wako umepokelewa. Utapata email baada ya kukaguliwa.";
    }

    public AuthResponse login(LoginRequest request) {
        // SPECIAL ADMIN LOGIN
        if ("admin".equals(request.getUsername()) && "admin@123".equals(request.getPassword())) {
            User admin = userRepository.findByEmail("admin@jmkloanapp.co.tz")
                    .orElseGet(() -> {
                        User newAdmin = User.builder()
                                .fullName("Administrator")
                                .email("admin@jmkloanapp.co.tz")
                                .phone("+255700000001")
                                .nidaNumber("00000000000000000001")
                                .password(passwordEncoder.encode("admin@123"))
                                .role(User.Role.ADMIN)
                                .status(User.UserStatus.APPROVED)
                                .active(true)
                                .build();
                        return userRepository.save(newAdmin);
                    });

            String token = jwtUtil.generateToken(admin.getEmail(), admin.getRole().name());

            return AuthResponse.builder()
                    .token(token)
                    .userId(admin.getId())
                    .fullName(admin.getFullName())
                    .email(admin.getEmail())
                    .role(admin.getRole().name())
                    .build();
        }

        // NORMAL LOGIN
        User user = userRepository.findByEmail(request.getUsername())
                .orElseGet(() -> userRepository.findByPhone(request.getUsername())
                        .orElseThrow(() -> new RuntimeException("Mtumiaji hajapatikana")));

        boolean passwordMatch = false;
        if (user.getPassword() != null) {
            if (request.getPassword().equals(user.getPassword())) {
                passwordMatch = true;
            } else if (user.getPassword().startsWith("$2")) {
                try {
                    passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
                } catch (Exception e) {
                    passwordMatch = false;
                }
            }
        }

        if (!passwordMatch) {
            throw new RuntimeException("Password si sahihi");
        }

        if (!user.getActive()) {
            throw new RuntimeException("Akaunti yako haijakubaliwa bado");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    public void setPassword(String token, String newPassword) {
        throw new RuntimeException("Kipengele hiki hakijatekelezwa bado");
    }
}
