package com.revworkforce.userservice.repository;

import com.revworkforce.userservice.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByTokenAndIsRevokedFalse(String token);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.isRevoked = true WHERE rt.employee.employeeId = :employeeId")
    int revokeAllByEmployee(@Param("employeeId") Integer employeeId);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < :now OR rt.isRevoked = true")
    int deleteExpiredAndRevoked(@Param("now") LocalDateTime now);
}

