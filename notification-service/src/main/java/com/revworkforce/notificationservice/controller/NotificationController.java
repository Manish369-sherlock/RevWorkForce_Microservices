package com.revworkforce.notificationservice.controller;
import com.revworkforce.notificationservice.dto.ApiResponse;
import com.revworkforce.notificationservice.model.Notification;
import com.revworkforce.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse> getMyNotifications(
            @RequestHeader("X-User-Email") String email,
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Notification> notifications = notificationService.getMyNotifications(email, isRead, pageable);
        return ResponseEntity.ok(new ApiResponse(true, "Notifications fetched successfully", notifications));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse> getUnreadCount(@RequestHeader("X-User-Email") String email) {
        long count = notificationService.getUnreadCount(email);
        return ResponseEntity.ok(new ApiResponse(true, "Unread count fetched successfully", count));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse> markAsRead(
            @PathVariable Integer notificationId,
            @RequestHeader("X-User-Email") String email) {
        Notification notification = notificationService.markAsRead(email, notificationId);
        return ResponseEntity.ok(new ApiResponse(true, "Notification marked as read", notification));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<ApiResponse> markAllAsRead(@RequestHeader("X-User-Email") String email) {
        int count = notificationService.markAllAsRead(email);
        return ResponseEntity.ok(new ApiResponse(true, count + " notification(s) marked as read"));
    }
}
