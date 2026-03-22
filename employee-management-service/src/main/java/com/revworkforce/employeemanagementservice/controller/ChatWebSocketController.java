package com.revworkforce.employeemanagementservice.controller;

import com.revworkforce.employeemanagementservice.dto.ChatMessageRequest;
import com.revworkforce.employeemanagementservice.dto.TypingIndicator;
import com.revworkforce.employeemanagementservice.model.Employee;
import com.revworkforce.employeemanagementservice.repository.EmployeeRepository;
import com.revworkforce.employeemanagementservice.service.ChatService;
import com.revworkforce.employeemanagementservice.service.PresenceService;
import com.revworkforce.employeemanagementservice.service.WebSocketNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatWebSocketController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private WebSocketNotificationService wsNotificationService;
    @Autowired
    private PresenceService presenceService;
    @Autowired
    private EmployeeRepository employeeRepository;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageRequest request, Principal principal) {
        String senderEmail = principal.getName();
        presenceService.touchUser(senderEmail);
        chatService.sendMessage(senderEmail, request);
    }

    @MessageMapping("/chat.typing")
    public void typingIndicator(@Payload TypingIndicator indicator, Principal principal) {
        String senderEmail = principal.getName();
        presenceService.touchUser(senderEmail);
        Employee sender = employeeRepository.findByEmail(senderEmail).orElse(null);
        if (sender == null) return;

        indicator.setSenderId(sender.getEmployeeId());
        indicator.setSenderName(sender.getFirstName() + " " + sender.getLastName());

        Employee recipient = getRecipientFromConversation(indicator.getConversationId(), sender.getEmployeeId());
        if (recipient != null) {
            wsNotificationService.sendTypingIndicator(recipient.getEmail(), indicator);
        }
    }

    private Employee getRecipientFromConversation(Long conversationId, Integer senderId) {
        if (conversationId == null) return null;
        return chatService.getOtherParticipantByConversation(conversationId, senderId);
    }
}
