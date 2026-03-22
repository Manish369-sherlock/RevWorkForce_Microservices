package com.revworkforce.leaveservice.controller;

import jakarta.validation.Valid;
import com.revworkforce.leaveservice.dto.ApiResponse;
import com.revworkforce.leaveservice.dto.LeaveApplyRequest;
import com.revworkforce.leaveservice.exception.UnauthorizedException;
import com.revworkforce.leaveservice.model.Holiday;
import com.revworkforce.leaveservice.model.LeaveBalance;
import com.revworkforce.leaveservice.model.LeaveType;
import com.revworkforce.leaveservice.model.enums.LeaveStatus;
import com.revworkforce.leaveservice.service.LeaveService;
import com.revworkforce.leaveservice.model.LeaveApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees/leaves")
public class EmployeeLeaveController {
    @Autowired
    private LeaveService leaveService;

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse> applyLeave(@Valid @RequestBody LeaveApplyRequest request) {
        String email = getCurrentUserEmail();
        LeaveApplication leave = leaveService.applyLeave(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Leave applied successfully", leave));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getMyLeaves(
            @RequestParam(required = false) LeaveStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appliedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        String email = getCurrentUserEmail();
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<LeaveApplication> leaves = leaveService.getMyLeaves(email, status, pageable);
        return ResponseEntity.ok(new ApiResponse(true, "Leaves fetched successfully", leaves));
    }

    @PatchMapping("/{leaveId}/cancel")
    public ResponseEntity<ApiResponse> cancelLeave(@PathVariable Integer leaveId) {
        String email = getCurrentUserEmail();
        LeaveApplication leave = leaveService.cancelLeave(email, leaveId);
        return ResponseEntity.ok(new ApiResponse(true, "Leave cancelled successfully", leave));
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse> getMyLeaveBalance() {
        String email = getCurrentUserEmail();
        List<LeaveBalance> balances = leaveService.getMyLeaveBalance(email);
        return ResponseEntity.ok(new ApiResponse(true, "Leave balance fetched successfully", balances));
    }

    @GetMapping("/holidays")
    public ResponseEntity<ApiResponse> getHolidays(@RequestParam(required = false) Integer year) {
        List<Holiday> holidays = leaveService.getHolidays(year);
        return ResponseEntity.ok(new ApiResponse(true, "Holidays fetched successfully", holidays));
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse> getActiveLeaveTypes() {
        List<LeaveType> leaveTypes = leaveService.getActiveLeaveTypes();
        return ResponseEntity.ok(new ApiResponse(true, "Active leave types fetched successfully", leaveTypes));
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User not authenticated");
        }
        return authentication.getName();
    }
}
