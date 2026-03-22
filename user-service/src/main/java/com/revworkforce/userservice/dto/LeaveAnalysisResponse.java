package com.revworkforce.userservice.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LeaveAnalysisResponse {
    private String employeeName;
    private String department;
    private String designation;

    private int totalLeavesTakenThisYear;
    private int totalLeavesTakenLastYear;
    private Map<String, Integer> leavesByType;
    private Map<String, Integer> leavesByMonth;
    private int pendingLeaveRequests;
    private double averageLeaveDuration;

    private Map<String, Integer> currentBalances;

    private List<String> patterns;
    private String frequencyTrend;
    private int teamMembersOnLeaveToday;

    private int requestedDays;
    private String requestedType;
    private int balanceAfterApproval;

    private String aiSummary;
    private String aiRecommendation;
    private List<String> aiReasons;
}

