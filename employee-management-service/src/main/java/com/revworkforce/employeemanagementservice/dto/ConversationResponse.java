package com.revworkforce.employeemanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationResponse {
    private Long conversationId;
    private Integer otherParticipantId;
    private String otherParticipantEmail;
    private String otherParticipantName;
    private String otherParticipantDesignation;
    private String otherParticipantCode;
    private String otherParticipantRole;
    private String otherParticipantDepartment;
    private String lastMessageText;
    private Integer lastSenderId;
    private LocalDateTime lastMessageAt;
    private long unreadCount;
    private boolean online;
}
