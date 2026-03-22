package com.revworkforce.notificationservice.controller;
import com.revworkforce.notificationservice.dto.ApiResponse;
import com.revworkforce.notificationservice.model.Employee;
import com.revworkforce.notificationservice.model.enums.NotificationType;
import com.revworkforce.notificationservice.service.NotificationService;
import com.revworkforce.notificationservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationInternalController {
    @Autowired private NotificationService notificationService;
    @Autowired private EmployeeRepository employeeRepository;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendNotification(@RequestBody Map<String, Object> request) {
        Integer recipientId = (Integer) request.get("recipientId");
        String title = (String) request.get("title");
        String message = (String) request.get("message");
        String type = (String) request.get("type");
        Integer referenceId = request.get("referenceId") != null ? (Integer) request.get("referenceId") : null;
        String referenceType = (String) request.get("referenceType");

        Employee recipient = employeeRepository.findById(recipientId).orElse(null);
        if (recipient != null) {
            notificationService.sendNotification(recipient, title, message,
                NotificationType.valueOf(type), referenceId, referenceType);
        }
        return ResponseEntity.ok(new ApiResponse(true, "Notification sent"));
    }
}
