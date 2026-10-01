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

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private NotificationService notificationService;

    @Value("${app.admin.email}") private String adminEmail;
    @Value("${app.admin.username:admin}") private String adminUsername;
    @Value("${app.frontend.url:}") private String frontendUrl;
    @Value("${google.client.id:}") private String googleClientId;

    public String register(RegisterRequest r) {
        if (r.getRole() != User.Role.BORROWER) throw new RuntimeException("Usajili wa public ni wa BORROWER pekee. Admin ndiye anayewapa staff/guarantor role baada ya uthibitishaji.");
        if (Period.between(r.getDateOfBirth(), LocalDate.now()).getYears() < 18)
            throw new RuntimeException("Lazima uwe na umri wa miaka 18 au zaidi");

        String photo = r.getPhoto();
        if (!photo.startsWith("data:image/jpeg;base64,") || photo.length() > 400_000)
            throw new RuntimeException("Picha si sahihi au ni kubwa mno");

        String email = r.getEmail().trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmail(email)) throw new RuntimeException("Barua pepe imetumika tayari");
        if (userRepository.existsByPhone(r.getPhone())) throw new RuntimeException("Namba ya simu imetumika tayari");
        if (userRepository.existsByNidaNumber(r.getIdNumber().trim())) throw new RuntimeException("Namba ya kitambulisho imetumika tayari");

        User user = User.builder()
                .fullName(r.getFullName().trim()).email(email).phone(r.getPhone())
                .nidaNumber(r.getIdNumber().trim()).idType(r.getIdType())
                .dateOfBirth(r.getDateOfBirth()).gender(r.getGender()).maritalStatus(r.getMaritalStatus())
                .nationality(r.getNationality()).address(r.getAddress()).city(r.getCity()).country(r.getCountry())
                .employmentStatus(r.getEmploymentStatus()).occupation(r.getOccupation()).employer(r.getEmployer())
                .monthlyIncome(r.getMonthlyIncome()).kinName(r.getKinName()).kinPhone(r.getKinPhone())
                .kinRelationship(r.getKinRelationship()).photoData(photo).role(r.getRole())
                .status(User.UserStatus.PENDING).active(false).build();

        userRepository.save(user);
        return "Usajili wako umepokelewa. Admin atakagua na kukutumia link ya kuweka password.";
    }

    public AuthResponse login(LoginRequest request) {
        String id = request.getUsername().trim();
        if (id.equalsIgnoreCase(adminUsername)) id = adminEmail;
        else if (id.contains("@")) id = id.toLowerCase(Locale.ROOT);

        final String identifier = id;
        User user = userRepository.findByEmail(identifier)
                .orElseGet(() -> userRepository.findByPhone(identifier)
                        .orElseThrow(() -> new RuntimeException("Barua pepe au password si sahihi")));

        if (user.getPassword() == null || !passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Barua pepe au password si sahihi");
        if (!Boolean.TRUE.equals(user.getActive()))
            throw new RuntimeException("Akaunti yako haijakubaliwa bado");

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return AuthResponse.builder().token(token).userId(user.getId()).fullName(user.getFullName())
                .email(user.getEmail()).role(user.getRole().name()).build();
    }

    public AuthResponse loginWithGoogle(String credential) {
        if (googleClientId == null || googleClientId.isBlank()) throw new RuntimeException("Google Sign-In haijawekwa kwenye mfumo");
        try {
            String url="https://oauth2.googleapis.com/tokeninfo?id_token="+java.net.URLEncoder.encode(credential, java.nio.charset.StandardCharsets.UTF_8);
            Map<?,?> claims=new RestTemplate().getForObject(url, Map.class);
            if(claims==null) throw new RuntimeException("Google credential haijasomwa");
            if(!googleClientId.equals(String.valueOf(claims.get("aud")))) throw new RuntimeException("Google client ID si sahihi");
            String issuer=String.valueOf(claims.get("iss"));
            if(!"https://accounts.google.com".equals(issuer) && !"accounts.google.com".equals(issuer)) throw new RuntimeException("Google issuer si sahihi");
            if(!"true".equalsIgnoreCase(String.valueOf(claims.get("email_verified")))) throw new RuntimeException("Google email haijathibitishwa");
            String email=String.valueOf(claims.get("email")).trim().toLowerCase(Locale.ROOT);
            User user=userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Akaunti ya email hii haipo. Jisajili kwanza kwa taarifa kamili za KYC."));
            if(!Boolean.TRUE.equals(user.getActive()) || user.getStatus()!=User.UserStatus.APPROVED) throw new RuntimeException("Akaunti yako haijakubaliwa bado");
            String token=jwtUtil.generateToken(user.getEmail(),user.getRole().name());
            return AuthResponse.builder().token(token).userId(user.getId()).fullName(user.getFullName()).email(user.getEmail()).role(user.getRole().name()).build();
        } catch (org.springframework.web.client.RestClientException ex) {
            throw new RuntimeException("Google Sign-In imeshindikana. Jaribu tena.");
        }
    }

    public void setPassword(String token, String newPassword) {
        validatePassword(newPassword);
        User u = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Link si sahihi au imeisha muda"));

        if (u.getTokenExpiry() == null || u.getTokenExpiry().isBefore(LocalDateTime.now())
                || u.getStatus() != User.UserStatus.APPROVED)
            throw new RuntimeException("Link si sahihi au imeisha muda");

        u.setPassword(passwordEncoder.encode(newPassword));
        u.setActive(true);
        u.setVerificationToken(null);
        u.setTokenExpiry(null);
        userRepository.save(u);
    }

    public void requestPasswordReset(String email) {
        String normalized = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        if (normalized.isBlank()) return;

        userRepository.findByEmail(normalized).ifPresent(user -> {
            if (!Boolean.TRUE.equals(user.getActive()) || user.getStatus() != User.UserStatus.APPROVED) return;

            String token = secureToken();
            user.setPasswordResetToken(token);
            user.setPasswordResetExpiry(LocalDateTime.now().plusMinutes(30));
            userRepository.save(user);

            String link = frontendUrl + "/reset-password?token=" + token;
            notificationService.sendPasswordResetEmail(user, link, 30);
        });
    }

    public void changePassword(String email, String currentPassword, String newPassword) {
        if (currentPassword == null || currentPassword.isBlank()) {
            throw new RuntimeException("Password ya sasa inahitajika");
        }
        validatePassword(newPassword);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Mtumiaji hajapatikana"));
        if (user.getPassword() == null || !passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Password ya sasa si sahihi");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new RuntimeException("Password mpya lazima iwe tofauti na ya sasa");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiry(null);
        userRepository.save(user);
    }

    public void resetPassword(String token, String newPassword) {
        validatePassword(newPassword);
        if (token == null || token.isBlank()) throw new RuntimeException("Link si sahihi au imeisha muda");

        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new RuntimeException("Link si sahihi au imeisha muda"));

        if (user.getPasswordResetExpiry() == null || user.getPasswordResetExpiry().isBefore(LocalDateTime.now())
                || !Boolean.TRUE.equals(user.getActive()) || user.getStatus() != User.UserStatus.APPROVED)
            throw new RuntimeException("Link si sahihi au imeisha muda");

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiry(null);
        user.setVerificationToken(null);
        user.setTokenExpiry(null);
        userRepository.save(user);
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8)
            throw new RuntimeException("Password iwe angalau herufi 8");
    }

    private String secureToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
