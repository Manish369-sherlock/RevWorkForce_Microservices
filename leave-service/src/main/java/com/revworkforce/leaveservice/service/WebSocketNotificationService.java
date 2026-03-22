package com.revworkforce.leaveservice.service;

import com.revworkforce.leaveservice.dto.ChatMessageResponse;
import com.revworkforce.leaveservice.dto.TypingIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WebSocketNotificationService {
    private static final Logger log = LoggerFactory.getLogger(WebSocketNotificationService.class);

    @Autowired(required = false)
    private SimpMessagingTemplate messagingTemplate;

    public void sendChatMessage(String recipientEmail, ChatMessageResponse message) {
        if (messagingTemplate == null) {
            log.debug("WebSocket broker not configured; skipping chat message push");
            return;
        }
        messagingTemplate.convertAndSendToUser(
                recipientEmail, "/queue/messages", message);
    }

    public void sendTypingIndicator(String recipientEmail, TypingIndicator indicator) {
        if (messagingTemplate == null) {
            log.debug("WebSocket broker not configured; skipping typing indicator push");
            return;
        }
        messagingTemplate.convertAndSendToUser(
                recipientEmail, "/queue/typing", indicator);
    }

    public void pushNotification(String recipientEmail, Map<String, Object> notification) {
        if (messagingTemplate == null) {
            log.debug("WebSocket broker not configured; skipping notification push");
            return;
        }
        messagingTemplate.convertAndSendToUser(
                recipientEmail, "/queue/notifications", notification);
    }

    public void sendUnreadChatCount(String recipientEmail, long count) {
        if (messagingTemplate == null) {
            log.debug("WebSocket broker not configured; skipping unread count push");
            return;
        }
        messagingTemplate.convertAndSendToUser(
                recipientEmail, "/queue/chat-unread", Map.of("unreadCount", count));
    }
}
