package com.loanapp.repository;

import com.loanapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<User> findByVerificationToken(String token);
    Optional<User> findByPasswordResetToken(String token);
    boolean existsByNidaNumber(String nidaNumber);
    java.util.List<User> findByRole(User.Role role);
    long countByStatus(User.UserStatus status);
    long countByActiveTrue();

    @Query("select u.id,u.fullName,u.email,u.phone,u.role,u.status,u.active,u.createdAt,u.idType,u.nidaNumber from User u where u.role <> :role order by u.createdAt desc")
    java.util.List<Object[]> findApplicantSummaries(@Param("role") User.Role role);
}