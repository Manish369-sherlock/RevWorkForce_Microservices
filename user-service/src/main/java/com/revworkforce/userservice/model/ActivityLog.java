package com.revworkforce.userservice.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity @Table(name = "activity_log", indexes = {
    @Index(name = "idx_log_action", columnList = "action"),
    @Index(name = "idx_log_performed_by", columnList = "performed_by")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ActivityLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "log_id")
    @EqualsAndHashCode.Include private Integer logId;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "performed_by", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) private Employee performedBy;
    @Column(nullable = false, length = 100) private String action;
    @Column(name = "entity_type", length = 50) private String entityType;
    @Column(name = "entity_id") private Integer entityId;
    @Column(columnDefinition = "TEXT") private String details;
    @Column(name = "ip_address", length = 45) private String ipAddress;
    @Column(name = "user_agent", length = 500) private String userAgent;
    @Column(length = 20) @Builder.Default private String status = "SUCCESS";
    @CreationTimestamp @Column(name = "created_at", updatable = false) private LocalDateTime createdAt;
}
