package com.revworkforce.userservice.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name = "otp_verification")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = {"employee"}) @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OtpVerification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otp_id")
    @EqualsAndHashCode.Include private Integer id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) private Employee employee;

    @Column(name = "otp_code", nullable = false, length = 10) private String otp;

    @Column(name = "otp", nullable = false, length = 10) private String legacyOtp;
    @Column(name = "pre_auth_token", nullable = false, unique = true, length = 500) private String preAuthToken;
    @Column(name = "expires_at", nullable = false) private LocalDateTime expiresAt;
    @Column(name = "attempts") @Builder.Default private Integer attempts = 0;
    @Column(name = "is_used") @Builder.Default private Boolean isUsed = false;
    @CreationTimestamp @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;

    @PrePersist
    @PreUpdate
    private void syncOtpColumns() {
        if (otp == null && legacyOtp != null) {
            otp = legacyOtp;
        }
        if (legacyOtp == null && otp != null) {
            legacyOtp = otp;
        }
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
}
