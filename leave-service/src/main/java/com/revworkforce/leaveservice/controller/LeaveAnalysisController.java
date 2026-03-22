package com.revworkforce.leaveservice.controller;

import com.revworkforce.leaveservice.dto.LeaveAnalysisResponse;
import com.revworkforce.leaveservice.service.LeaveAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/leave-analysis")
public class LeaveAnalysisController {
    @Autowired private LeaveAnalysisService leaveAnalysisService;

    @GetMapping("/{leaveId}")
    public ResponseEntity<LeaveAnalysisResponse> analyzeLeave(@PathVariable Integer leaveId) {
        return ResponseEntity.ok(leaveAnalysisService.analyzeLeaveRequest(leaveId));
    }
}

