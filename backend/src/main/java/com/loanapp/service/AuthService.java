package com.loanapp.service;

import com.loanapp.dto.AuthResponse;
import com.loanapp.dto.LoginRequest;
import com.loanapp.dto.RegisterRequest;
import com.loanapp.model.PasswordResetToken;
import com.loanapp.model.User;
import com.loanapp.repository.PasswordResetTokenRepository;
import com.loanapp.repository.UserRepository;
import com.loanapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

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

    @Transactional
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Barua pepe haijasajiliwa"));

        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .used(false)
                .build();

        passwordResetTokenRepository.save(resetToken);
        emailService.sendPasswordResetEmail(user, token);

        return "Email ya kubadilisha password imetumwa kwa " + email;
    }

    @Transactional
    public String resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token si sahihi au imeisha muda"));

        if (resetToken.getUsed()) {
            throw new RuntimeException("Token imetumika tayari");
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token imeisha muda. Omba link mpya.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);

        return "Password yako imewekwa. Unaweza kuingia.";
    }
}
