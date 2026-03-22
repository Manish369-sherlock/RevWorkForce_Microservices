package com.revworkforce.notificationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WebSocketNotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void pushNotification(String recipientEmail, Map<String, Object> notification) {
        messagingTemplate.convertAndSendToUser(
                recipientEmail, "/queue/notifications", notification);
    }

    public void sendUnreadChatCount(String recipientEmail, long count) {
        messagingTemplate.convertAndSendToUser(
                recipientEmail, "/queue/chat-unread", Map.of("unreadCount", count));
    }
}
