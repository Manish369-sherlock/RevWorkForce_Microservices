package com.revworkforce.employeemanagementservice.controller;

import com.revworkforce.employeemanagementservice.dto.ApiResponse;
import com.revworkforce.employeemanagementservice.dto.ChatMessageRequest;
import com.revworkforce.employeemanagementservice.dto.ChatMessageResponse;
import com.revworkforce.employeemanagementservice.dto.ConversationResponse;
import com.revworkforce.employeemanagementservice.service.ChatService;
import com.revworkforce.employeemanagementservice.service.PresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @Autowired
    private PresenceService presenceService;

    @GetMapping("/conversations")
    public ResponseEntity<ApiResponse> getMyConversations(@RequestHeader("X-User-Email") String email) {
        presenceService.touchUser(email);
        List<ConversationResponse> conversations = chatService.getMyConversations(email);
        return ResponseEntity.ok(new ApiResponse(true, "Conversations fetched successfully", conversations));
    }

    @PostMapping("/conversations/{otherEmployeeId}")
    public ResponseEntity<ApiResponse> getOrCreateConversation(@RequestHeader("X-User-Email") String email, @PathVariable Integer otherEmployeeId) {
        presenceService.touchUser(email);
        ConversationResponse conversation = chatService.getOrCreateConversation(email, otherEmployeeId);
        return ResponseEntity.ok(new ApiResponse(true, "Conversation ready", conversation));
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<ApiResponse> getMessages(
            @PathVariable Long conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestHeader("X-User-Email") String email) {
        presenceService.touchUser(email);
        Page<ChatMessageResponse> messages = chatService.getMessages(email, conversationId, page, size);
        return ResponseEntity.ok(new ApiResponse(true, "Messages fetched successfully", messages));
    }

    @PostMapping("/messages")
    public ResponseEntity<ApiResponse> sendMessage(@RequestHeader("X-User-Email") String email,
                                                   @RequestBody ChatMessageRequest request) {
        presenceService.touchUser(email);
        ChatMessageResponse sent = chatService.sendMessage(email, request);
        return ResponseEntity.ok(new ApiResponse(true, "Message sent successfully", sent));
    }

    @PatchMapping("/conversations/{conversationId}/read")
    public ResponseEntity<ApiResponse> markConversationAsRead(@RequestHeader("X-User-Email") String email, @PathVariable Long conversationId) {
        presenceService.touchUser(email);
        int count = chatService.markConversationAsRead(email, conversationId);
        return ResponseEntity.ok(new ApiResponse(true, count + " message(s) marked as read"));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse> getUnreadCount(@RequestHeader("X-User-Email") String email) {
        presenceService.touchUser(email);
        long count = chatService.getTotalUnreadCount(email);
        return ResponseEntity.ok(new ApiResponse(true, "Unread count fetched", count));
    }

    @GetMapping("/online-users")
    public ResponseEntity<ApiResponse> getOnlineUsers(@RequestHeader(value = "X-User-Email", required = false) String email) {
        presenceService.touchUser(email);
        Set<String> onlineEmails = presenceService.getOnlineUsers();
        return ResponseEntity.ok(new ApiResponse(true, "Online users fetched", onlineEmails));
    }
}
