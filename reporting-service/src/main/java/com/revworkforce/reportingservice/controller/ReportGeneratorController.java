package com.revworkforce.reportingservice.controller;

import com.revworkforce.reportingservice.dto.PerformanceReportResponse;
import com.revworkforce.reportingservice.service.ReportGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reports")
public class ReportGeneratorController {
    @Autowired private ReportGeneratorService reportGeneratorService;

    @GetMapping("/performance/{employeeId}")
    public ResponseEntity<PerformanceReportResponse> generateReport(
            @PathVariable Integer employeeId,
            @RequestParam(required = false) String period) {
        return ResponseEntity.ok(reportGeneratorService.generateReport(employeeId, period));
    }
}

