package com.revworkforce.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignManagerRequest {
    @NotBlank(message = "Manager code is required")
    private String managerCode;
}
