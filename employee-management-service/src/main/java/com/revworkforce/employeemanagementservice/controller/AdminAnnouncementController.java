package com.revworkforce.employeemanagementservice.controller;
import jakarta.validation.Valid;
import com.revworkforce.employeemanagementservice.dto.AnnouncementRequest;
import com.revworkforce.employeemanagementservice.dto.ApiResponse;
import com.revworkforce.employeemanagementservice.model.Announcement;
import com.revworkforce.employeemanagementservice.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/announcements")
public class AdminAnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    @PostMapping
    public ResponseEntity<ApiResponse> createAnnouncement(@RequestHeader("X-User-Email") String adminEmail, @Valid @RequestBody AnnouncementRequest request) {
        Announcement announcement = announcementService.createAnnouncement(adminEmail, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Announcement created successfully", announcement));
    }

    @PutMapping("/{announcementId}")
    public ResponseEntity<ApiResponse> updateAnnouncement(@PathVariable Integer announcementId, @Valid @RequestBody AnnouncementRequest request) {
        Announcement announcement = announcementService.updateAnnouncement(announcementId, request);
        return ResponseEntity.ok(new ApiResponse(true, "Announcement updated successfully", announcement));
    }

    @PatchMapping("/{announcementId}/deactivate")
    public ResponseEntity<ApiResponse> deactivateAnnouncement(@PathVariable Integer announcementId) {
        Announcement announcement = announcementService.deactivateAnnouncement(announcementId);
        return ResponseEntity.ok(new ApiResponse(true, "Announcement deactivated successfully", announcement));
    }

    @PatchMapping("/{announcementId}/activate")
    public ResponseEntity<ApiResponse> activateAnnouncement(@PathVariable Integer announcementId) {
        Announcement announcement = announcementService.activateAnnouncement(announcementId);
        return ResponseEntity.ok(new ApiResponse(true, "Announcement activated successfully", announcement));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAnnouncements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Announcement> announcements = announcementService.getAllAnnouncements(pageable);
        return ResponseEntity.ok(new ApiResponse(true, "Announcements fetched successfully", announcements));
    }
}
