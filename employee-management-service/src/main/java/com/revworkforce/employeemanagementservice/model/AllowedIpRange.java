package com.revworkforce.employeemanagementservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "allowed_ip_range")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllowedIpRange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip_range_id")
    private Integer ipRangeId;

    @Column(name = "ip_range", nullable = false, length = 50)
    private String ipRange;

    @Column(length = 200)
    private String description;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
