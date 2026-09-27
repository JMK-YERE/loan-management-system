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
        User user = userRepository.findByEmail(request.getUsername())
                .orElseGet(() -> userRepository.findByPhone(request.getUsername())
                        .orElseThrow(() -> new RuntimeException("Mtumiaji hajapatikana")));

        // KWA MAJARIBIO: Ruhusu password ya wazi AU BCrypt
        boolean passwordMatch = false;

        // 1. Jaribu BCrypt
        try {
            if (user.getPassword() != null && user.getPassword().startsWith("$2")) {
                passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
            }
        } catch (Exception e) {
            passwordMatch = false;
        }

        // 2. Jaribu plain text comparison
        if (!passwordMatch && user.getPassword() != null) {
            passwordMatch = request.getPassword().equals(user.getPassword());
        }

        // 3. Kwa majaribio: ruhusu "Joseph@2026" kwa admin
        if (!passwordMatch && "joseph@jmkloanapp.co.tz".equals(user.getEmail()) 
            && "Joseph@2026".equals(request.getPassword())) {
            passwordMatch = true;
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
