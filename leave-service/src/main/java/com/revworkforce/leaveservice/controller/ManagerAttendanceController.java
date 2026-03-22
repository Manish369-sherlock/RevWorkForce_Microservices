package com.revworkforce.leaveservice.controller;

import com.revworkforce.leaveservice.dto.ApiResponse;
import com.revworkforce.leaveservice.dto.AttendanceResponse;
import com.revworkforce.leaveservice.exception.UnauthorizedException;
import com.revworkforce.leaveservice.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/manager/attendance")
public class ManagerAttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/team/today")
    public ResponseEntity<ApiResponse> getTeamAttendanceToday() {
        String managerEmail = getManagerEmail();
        List<AttendanceResponse> teamAttendance = attendanceService.getTeamAttendanceToday(managerEmail);
        return ResponseEntity.ok(new ApiResponse(true, "Team attendance for today fetched", teamAttendance));
    }

    @GetMapping("/team")
    public ResponseEntity<ApiResponse> getTeamAttendance(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        String managerEmail = getManagerEmail();
        List<AttendanceResponse> teamAttendance = attendanceService.getTeamAttendanceBetween(managerEmail, startDate, endDate);
        return ResponseEntity.ok(new ApiResponse(true, "Team attendance fetched", teamAttendance));
    }

    private String getManagerEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("Manager not authenticated");
        }
        return auth.getName();
    }
}
