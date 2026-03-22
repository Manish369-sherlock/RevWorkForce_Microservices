package com.revworkforce.employeemanagementservice.dto;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PerformanceReportResponse {
    private String employeeName;
    private String employeeCode;
    private String department;
    private String designation;
    private String reportPeriod;

    private int totalPresentDays;
    private int totalAbsentDays;
    private int lateArrivals;
    private double averageHoursPerDay;

    private int totalLeavesTaken;
    private Map<String, Integer> leaveBreakdown;

    private int totalGoals;
    private int completedGoals;
    private int inProgressGoals;
    private double averageGoalProgress;
    private List<GoalSummary> goals;

    private List<ReviewSummary> reviews;
    private Double averageSelfRating;
    private Double averageManagerRating;

    private String aiOverallAssessment;
    private String aiStrengths;
    private String aiAreasForImprovement;
    private String aiRecommendations;
    private String aiRating;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class GoalSummary {
        private String title;
        private String status;
        private int progress;
        private String priority;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ReviewSummary {
        private String period;
        private Integer selfRating;
        private Integer managerRating;
        private String status;
    }
}

