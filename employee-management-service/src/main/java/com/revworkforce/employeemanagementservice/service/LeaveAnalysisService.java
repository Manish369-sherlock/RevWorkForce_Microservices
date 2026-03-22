package com.revworkforce.employeemanagementservice.service;

import com.revworkforce.employeemanagementservice.dto.LeaveAnalysisResponse;
import com.revworkforce.employeemanagementservice.integration.OllamaClient;
import com.revworkforce.employeemanagementservice.model.*;
import com.revworkforce.employeemanagementservice.model.enums.LeaveStatus;
import com.revworkforce.employeemanagementservice.repository.LeaveApplicationRepository;
import com.revworkforce.employeemanagementservice.repository.LeaveBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaveAnalysisService {
    private static final Logger log = LoggerFactory.getLogger(LeaveAnalysisService.class);

    @Autowired private LeaveApplicationRepository leaveApplicationRepository;
    @Autowired private LeaveBalanceRepository leaveBalanceRepository;
    @Autowired private OllamaClient ollamaClient;

    public LeaveAnalysisResponse analyzeLeaveRequest(Integer leaveId) {
        LeaveApplication request = leaveApplicationRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave application not found: " + leaveId));

        Employee employee = request.getEmployee();
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();

        List<LeaveApplication> allLeaves = leaveApplicationRepository
                .findByEmployeeEmployeeId(employee.getEmployeeId().intValue());

        List<LeaveApplication> thisYearLeaves = allLeaves.stream()
                .filter(l -> l.getStartDate().getYear() == currentYear)
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED || l.getStatus() == LeaveStatus.PENDING)
                .toList();

        List<LeaveApplication> lastYearLeaves = allLeaves.stream()
                .filter(l -> l.getStartDate().getYear() == currentYear - 1)
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED)
                .toList();

        Map<String, Integer> leavesByType = thisYearLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED)
                .collect(Collectors.groupingBy(
                        l -> l.getLeaveType().getLeaveTypeName(),
                        Collectors.summingInt(LeaveApplication::getTotalDays)));

        Map<String, Integer> leavesByMonth = thisYearLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED)
                .collect(Collectors.groupingBy(
                        l -> l.getStartDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                        Collectors.summingInt(LeaveApplication::getTotalDays)));

        double avgDuration = thisYearLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED)
                .mapToInt(LeaveApplication::getTotalDays)
                .average().orElse(0);

        List<LeaveBalance> balances = leaveBalanceRepository
                .findByEmployee_EmployeeIdAndYear(employee.getEmployeeId(), currentYear);
        Map<String, Integer> currentBalances = balances.stream()
                .collect(Collectors.toMap(
                        b -> b.getLeaveType().getLeaveTypeName(),
                        LeaveBalance::getAvailableBalance));

        String requestedType = request.getLeaveType().getLeaveTypeName();
        int currentBalance = currentBalances.getOrDefault(requestedType, 0);
        int balanceAfterApproval = currentBalance - request.getTotalDays();

        List<String> patterns = detectPatterns(thisYearLeaves);

        int thisYearCount = thisYearLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED)
                .mapToInt(LeaveApplication::getTotalDays).sum();
        int lastYearCount = lastYearLeaves.stream()
                .mapToInt(LeaveApplication::getTotalDays).sum();
        String trend = thisYearCount > lastYearCount * 1.2 ? "INCREASING" :
                thisYearCount < lastYearCount * 0.8 ? "DECREASING" : "STABLE";

        int teamOnLeave = 0;
        if (employee.getManager() != null) {
            teamOnLeave = countTeamMembersOnLeave(employee.getManager().getEmployeeId(), today);
        }

        long pendingCount = allLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.PENDING)
                .count();

        LeaveAnalysisResponse response = LeaveAnalysisResponse.builder()
                .employeeName(employee.getFirstName() + " " + employee.getLastName())
                .department(employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : "N/A")
                .designation(employee.getDesignation() != null ? employee.getDesignation().getDesignationName() : "N/A")
                .totalLeavesTakenThisYear(thisYearCount)
                .totalLeavesTakenLastYear(lastYearCount)
                .leavesByType(leavesByType)
                .leavesByMonth(leavesByMonth)
                .pendingLeaveRequests((int) pendingCount)
                .averageLeaveDuration(Math.round(avgDuration * 10.0) / 10.0)
                .currentBalances(currentBalances)
                .patterns(patterns)
                .frequencyTrend(trend)
                .teamMembersOnLeaveToday(teamOnLeave)
                .requestedDays(request.getTotalDays())
                .requestedType(requestedType)
                .balanceAfterApproval(balanceAfterApproval)
                .build();

        generateAiAnalysis(response, request);

        return response;
    }

    private List<String> detectPatterns(List<LeaveApplication> leaves) {
        List<String> patterns = new ArrayList<>();
        List<LeaveApplication> approved = leaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED).toList();

        if (approved.isEmpty()) {
            patterns.add("No approved leave history this year");
            return patterns;
        }

        long mondayFriday = approved.stream()
                .filter(l -> {
                    DayOfWeek dow = l.getStartDate().getDayOfWeek();
                    return dow == DayOfWeek.MONDAY || dow == DayOfWeek.FRIDAY;
                }).count();
        if (mondayFriday > approved.size() * 0.5 && approved.size() >= 3) {
            patterns.add("Frequently takes leave on Mondays/Fridays (" + mondayFriday + "/" + approved.size() + " requests)");
        }

        long singleDays = approved.stream().filter(l -> l.getTotalDays() == 1).count();
        if (singleDays > 3) {
            patterns.add("Takes many single-day leaves (" + singleDays + " this year)");
        }

        Map<Integer, Long> byMonth = approved.stream()
                .collect(Collectors.groupingBy(l -> l.getStartDate().getMonthValue(), Collectors.counting()));
        byMonth.entrySet().stream()
                .filter(e -> e.getValue() >= 3)
                .forEach(e -> patterns.add("High leave frequency in " +
                        java.time.Month.of(e.getKey()).getDisplayName(TextStyle.FULL, Locale.ENGLISH)));

        if (patterns.isEmpty()) {
            patterns.add("No unusual patterns detected");
        }

        return patterns;
    }

    private int countTeamMembersOnLeave(Integer managerId, LocalDate date) {
        return leaveApplicationRepository.countTeamOnLeave(managerId, LeaveStatus.APPROVED, date);
    }

    private void generateAiAnalysis(LeaveAnalysisResponse response, LeaveApplication request) {
        if (!ollamaClient.isAvailable()) {
            log.info("Ollama not available — using data-driven analysis fallback");
            setDefaultAnalysis(response);
            return;
        }

        try {
            String prompt = String.format("""
                    Analyze this leave request concisely (2-3 sentences).
                    Employee: %s | Dept: %s | Request: %d days %s
                    Balance after: %d | Trend: %s | Team on leave: %d
                    Patterns: %s
                    Respond EXACTLY as:
                    SUMMARY: [analysis]
                    RECOMMENDATION: [APPROVE or REVIEW_FURTHER]
                    REASONS: [reason1, reason2]
                    """,
                    response.getEmployeeName(), response.getDepartment(),
                    request.getTotalDays(), response.getRequestedType(),
                    response.getBalanceAfterApproval(),
                    response.getFrequencyTrend(), response.getTeamMembersOnLeaveToday(),
                    String.join("; ", response.getPatterns()));

            String aiResponse = ollamaClient.generate(prompt, 150);

            if (aiResponse != null && !aiResponse.startsWith("Error")) {
                String summary = extractField(aiResponse, "SUMMARY:");
                String recommendation = extractField(aiResponse, "RECOMMENDATION:");
                String reasons = extractField(aiResponse, "REASONS:");

                response.setAiSummary(summary != null ? summary : aiResponse.trim());
                response.setAiRecommendation(recommendation != null && recommendation.contains("REVIEW") ?
                        "REVIEW_FURTHER" : "APPROVE");
                response.setAiReasons(reasons != null ?
                        Arrays.asList(reasons.split(",\\s*")) :
                        List.of("Based on available data"));
            } else {
                setDefaultAnalysis(response);
            }
        } catch (Exception e) {
            setDefaultAnalysis(response);
        }
    }

    private void setDefaultAnalysis(LeaveAnalysisResponse response) {
        List<String> reasons = new ArrayList<>();
        StringBuilder summary = new StringBuilder();

        boolean recommend = true;

        if (response.getBalanceAfterApproval() >= 0) {
            reasons.add("Sufficient leave balance (" + (response.getBalanceAfterApproval() + response.getRequestedDays()) + " available, " + response.getBalanceAfterApproval() + " remaining after)");
        } else {
            recommend = false;
            reasons.add("Insufficient leave balance (would be " + response.getBalanceAfterApproval() + " after approval)");
        }

        if (response.getTeamMembersOnLeaveToday() >= 3) {
            recommend = false;
            reasons.add("High team absence — " + response.getTeamMembersOnLeaveToday() + " team members already on leave today");
        } else if (response.getTeamMembersOnLeaveToday() > 0) {
            reasons.add(response.getTeamMembersOnLeaveToday() + " team member(s) on leave today — manageable impact");
        } else {
            reasons.add("No team members currently on leave — minimal team impact");
        }

        if ("INCREASING".equals(response.getFrequencyTrend()) && response.getTotalLeavesTakenThisYear() > 10) {
            reasons.add("Leave frequency is increasing compared to last year (" + response.getTotalLeavesTakenThisYear() + " vs " + response.getTotalLeavesTakenLastYear() + " days)");
        } else if ("STABLE".equals(response.getFrequencyTrend()) || "DECREASING".equals(response.getFrequencyTrend())) {
            reasons.add("Leave usage trend is " + response.getFrequencyTrend().toLowerCase() + " compared to last year");
        }

        boolean hasWarningPattern = response.getPatterns() != null &&
                response.getPatterns().stream().anyMatch(p -> p.contains("Frequently") || p.contains("High leave frequency"));
        if (hasWarningPattern) {
            reasons.add("Some leave patterns detected — review patterns section for details");
        }

        if (response.getPendingLeaveRequests() > 3) {
            reasons.add(response.getPendingLeaveRequests() + " pending leave requests — consider reviewing all together");
        }

        summary.append(response.getEmployeeName()).append(" (").append(response.getDepartment()).append(") ")
                .append("is requesting ").append(response.getRequestedDays()).append(" day(s) of ").append(response.getRequestedType()).append(". ");

        if (response.getBalanceAfterApproval() >= 0) {
            summary.append("They have sufficient balance with ").append(response.getBalanceAfterApproval()).append(" days remaining after approval. ");
        } else {
            summary.append("This would exceed their available balance by ").append(Math.abs(response.getBalanceAfterApproval())).append(" day(s). ");
        }

        summary.append("This year they've taken ").append(response.getTotalLeavesTakenThisYear()).append(" days ")
                .append("(last year: ").append(response.getTotalLeavesTakenLastYear()).append(" days). ");

        if (response.getTeamMembersOnLeaveToday() == 0) {
            summary.append("No team members are on leave today, so team coverage looks fine.");
        } else {
            summary.append(response.getTeamMembersOnLeaveToday()).append(" team member(s) are on leave today.");
        }

        response.setAiSummary(summary.toString());
        response.setAiRecommendation(recommend ? "APPROVE" : "REVIEW_FURTHER");
        response.setAiReasons(reasons);
    }

    private String extractField(String text, String fieldName) {
        int idx = text.indexOf(fieldName);
        if (idx < 0) return null;
        String after = text.substring(idx + fieldName.length()).trim();
        int newline = after.indexOf('\n');
        return newline >= 0 ? after.substring(0, newline).trim() : after.trim();
    }
}

