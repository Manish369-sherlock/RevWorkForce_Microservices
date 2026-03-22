package com.revworkforce.performanceservice.controller;

import jakarta.validation.Valid;
import com.revworkforce.performanceservice.dto.*;
import com.revworkforce.performanceservice.model.Goal;
import com.revworkforce.performanceservice.model.PerformanceReview;
import com.revworkforce.performanceservice.model.enums.GoalStatus;
import com.revworkforce.performanceservice.model.enums.ReviewStatus;
import com.revworkforce.performanceservice.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeePerformanceController {
    @Autowired
    private PerformanceService performanceService;

    @PostMapping("/reviews")
    public ResponseEntity<ApiResponse> createReview(@Valid @RequestBody PerformanceReviewRequest request,
                                                       @RequestHeader("X-User-Email") String email) {
        PerformanceReview review = performanceService.createReview(email, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Performance review created as draft", review));
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> updateReview(@PathVariable Integer reviewId, @Valid @RequestBody PerformanceReviewRequest request,
                                                       @RequestHeader("X-User-Email") String email) {
        PerformanceReview review = performanceService.updateReview(email, reviewId, request);
        return ResponseEntity.ok(new ApiResponse(true, "Performance review updated successfully", review));
    }

    @PatchMapping("/reviews/{reviewId}/submit")
    public ResponseEntity<ApiResponse> submitReview(@PathVariable Integer reviewId,
                                                       @RequestHeader("X-User-Email") String email) {
        PerformanceReview review = performanceService.submitReview(email, reviewId);
        return ResponseEntity.ok(new ApiResponse(true, "Performance review submitted to manager", review));
    }

    @GetMapping("/reviews")
    public ResponseEntity<ApiResponse> getMyReviews(
            @RequestParam(required = false) ReviewStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestHeader("X-User-Email") String email) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PerformanceReview> reviews = performanceService.getMyReviews(email, status, pageable);
        return ResponseEntity.ok(new ApiResponse(true, "Reviews fetched successfully", reviews));
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> getReviewById(@PathVariable Integer reviewId,
                                                        @RequestHeader("X-User-Email") String email) {
        PerformanceReview review = performanceService.getReviewById(email, reviewId);
        return ResponseEntity.ok(new ApiResponse(true, "Review fetched successfully", review));
    }

    @PostMapping("/goals")
    public ResponseEntity<ApiResponse> createGoal(@Valid @RequestBody GoalRequest request,
                                                     @RequestHeader("X-User-Email") String email) {
        Goal goal = performanceService.createGoal(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Goal created successfully", goal));
    }

    @GetMapping("/goals")
    public ResponseEntity<ApiResponse> getMyGoals(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) GoalStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestHeader("X-User-Email") String email) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Goal> goals = performanceService.getMyGoals(email, year, status, pageable);
        return ResponseEntity.ok(new ApiResponse(true, "Goals fetched successfully", goals));
    }

    @PatchMapping("/goals/{goalId}/progress")
    public ResponseEntity<ApiResponse> updateGoalProgress(@PathVariable Integer goalId, @Valid @RequestBody GoalProgressRequest request,
                                                             @RequestHeader("X-User-Email") String email) {
        Goal goal = performanceService.updateGoalProgress(email, goalId, request);
        return ResponseEntity.ok(new ApiResponse(true, "Goal progress updated successfully", goal));
    }
}
