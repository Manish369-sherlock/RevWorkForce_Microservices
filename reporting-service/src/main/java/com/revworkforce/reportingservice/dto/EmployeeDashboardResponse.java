package com.revworkforce.reportingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDashboardResponse {
    private String employeeName;
    private String employeeCode;
    private String departmentName;
    private String designationTitle;
    private long pendingLeaveRequests;
    private long approvedLeaves;
    private long unreadNotifications;
    private List<LeaveBalanceSummary> leaveBalances;
    private List<UpcomingHolidaySummary> upcomingHolidays;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LeaveBalanceSummary {
        private String leaveTypeName;
        private Integer totalLeaves;
        private Integer usedLeaves;
        private Integer availableBalance;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpcomingHolidaySummary {
        private String holidayName;
        private LocalDate holidayDate;
        private String description;
    }
}
