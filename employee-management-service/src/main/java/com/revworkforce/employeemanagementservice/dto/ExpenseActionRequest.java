package com.revworkforce.employeemanagementservice.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExpenseActionRequest {
    private String action;
    private String comments;
}

