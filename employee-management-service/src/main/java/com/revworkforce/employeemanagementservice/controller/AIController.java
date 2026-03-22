package com.revworkforce.employeemanagementservice.controller;

import jakarta.validation.Valid;
import com.revworkforce.employeemanagementservice.dto.AIChatRequest;
import com.revworkforce.employeemanagementservice.dto.AIChatResponse;
import com.revworkforce.employeemanagementservice.dto.ApiResponse;
import com.revworkforce.employeemanagementservice.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {
    @Autowired
    private AIService aiService;
    @PostMapping("/chat")
    public ResponseEntity<ApiResponse> chat(@RequestHeader("X-User-Email") String email, @Valid @RequestBody AIChatRequest request) {
        AIChatResponse response = aiService.processMessage(email, request);
        return ResponseEntity.ok(new ApiResponse(true, "AI response generated", response));
    }
}