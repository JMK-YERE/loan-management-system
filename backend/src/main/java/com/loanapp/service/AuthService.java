package com.loanapp.service;

import com.loanapp.dto.AuthResponse;
import com.loanapp.dto.LoginRequest;
import com.loanapp.dto.RegisterRequest;
import com.loanapp.model.User;
import com.loanapp.repository.UserRepository;
import com.loanapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;

    @Value("${app.admin.email}") private String adminEmail;
    @Value("${app.admin.username:admin}") private String adminUsername;

    public String register(RegisterRequest r) {
        if (r.getRole() == User.Role.ADMIN) {
            throw new RuntimeException("Role hii hairuhusiwi");
        }
        if (Period.between(r.getDateOfBirth(), LocalDate.now()).getYears() < 18) {
            throw new RuntimeException("Lazima uwe na umri wa miaka 18 au zaidi");
        }
        String photo = r.getPhoto();
        if (!photo.startsWith("data:image/jpeg;base64,") || photo.length() > 400_000) {
            throw new RuntimeException("Picha si sahihi au ni kubwa mno");
        }
        String email = r.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) throw new RuntimeException("Barua pepe imetumika tayari");
        if (userRepository.existsByPhone(r.getPhone())) throw new RuntimeException("Namba ya simu imetumika tayari");
        if (userRepository.existsByNidaNumber(r.getIdNumber().trim())) throw new RuntimeException("Namba ya kitambulisho imetumika tayari");

        User user = User.builder()
                .fullName(r.getFullName().trim())
                .email(email)
                .phone(r.getPhone())
                .nidaNumber(r.getIdNumber().trim())
                .idType(r.getIdType())
                .dateOfBirth(r.getDateOfBirth())
                .gender(r.getGender())
                .maritalStatus(r.getMaritalStatus())
                .nationality(r.getNationality())
                .address(r.getAddress())
                .city(r.getCity())
                .country(r.getCountry())
                .employmentStatus(r.getEmploymentStatus())
                .occupation(r.getOccupation())
                .employer(r.getEmployer())
                .monthlyIncome(r.getMonthlyIncome())
                .kinName(r.getKinName())
                .kinPhone(r.getKinPhone())
                .kinRelationship(r.getKinRelationship())
                .photoData(photo)
                .role(r.getRole())
                .status(User.UserStatus.PENDING)
                .active(false)
                .build();
        userRepository.save(user);
        return "Usajili wako umepokelewa. Admin atakagua na kukupa link ya kuweka password.";
    }

    public AuthResponse login(LoginRequest request) {
        String id = request.getUsername().trim();
        if (id.equalsIgnoreCase(adminUsername)) id = adminEmail;
        else if (id.contains("@")) id = id.toLowerCase();
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
        if (newPassword == null || newPassword.length() < 8) {
            throw new RuntimeException("Password iwe angalau herufi 8");
        }
        User u = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Link si sahihi au imeisha muda"));
        if (u.getTokenExpiry() == null || u.getTokenExpiry().isBefore(LocalDateTime.now())
                || u.getStatus() != User.UserStatus.APPROVED) {
            throw new RuntimeException("Link si sahihi au imeisha muda");
        }
        u.setPassword(passwordEncoder.encode(newPassword));
        u.setActive(true);
        u.setVerificationToken(null);
        u.setTokenExpiry(null);
        userRepository.save(u);
    }
}
