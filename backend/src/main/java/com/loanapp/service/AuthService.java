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

    @org.springframework.beans.factory.annotation.Value("${app.admin.email}")
    private String adminEmail;

    @org.springframework.beans.factory.annotation.Value("${app.admin.username:admin}")
    private String adminUsername;

    public AuthResponse login(LoginRequest request) {
        String id = request.getUsername().trim();
        if (id.equalsIgnoreCase(adminUsername)) id = adminEmail;
        final String identifier = id;

        User user = userRepository.findByEmail(identifier)
                .orElseGet(() -> userRepository.findByPhone(identifier)
                        .orElseThrow(() -> new RuntimeException("Barua pepe au password si sahihi")));

        if (user.getPassword() == null
                || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Barua pepe au password si sahihi");
        }
        if (!Boolean.TRUE.equals(user.getActive())) {
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
