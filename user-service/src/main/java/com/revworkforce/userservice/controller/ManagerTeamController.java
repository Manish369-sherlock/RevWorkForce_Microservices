package com.revworkforce.userservice.controller;

import com.revworkforce.userservice.dto.ApiResponse;
import com.revworkforce.userservice.dto.EmployeeDirectoryResponse;
import com.revworkforce.userservice.dto.EmployeeProfileResponse;
import com.revworkforce.userservice.exception.InvalidActionException;
import com.revworkforce.userservice.exception.ResourceNotFoundException;
import com.revworkforce.userservice.exception.UnauthorizedException;
import com.revworkforce.userservice.model.Employee;
import com.revworkforce.userservice.repository.EmployeeRepository;
import com.revworkforce.userservice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager/team")
public class ManagerTeamController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<ApiResponse> getTeamMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        String email = getManagerEmail();
        Employee manager = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Employee> team = employeeRepository.findByManager_EmployeeCode(manager.getEmployeeCode(), pageable);
        Page<EmployeeDirectoryResponse> response = team.map(this::mapToDirectoryResponse);
        return ResponseEntity.ok(new ApiResponse(true, "Team members fetched successfully", response));
    }

    @GetMapping("/{employeeCode}")
    public ResponseEntity<ApiResponse> getTeamMemberProfile(@PathVariable String employeeCode) {
        String email = getManagerEmail();
        Employee manager = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        Employee member = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        if (member.getManager() == null || !member.getManager().getEmployeeCode().equals(manager.getEmployeeCode())) {
            throw new InvalidActionException("This employee is not in your team");
        }
        EmployeeProfileResponse profile = employeeService.getEmployeeByCode(employeeCode);
        return ResponseEntity.ok(new ApiResponse(true, "Team member profile fetched successfully", profile));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse> getTeamCount() {
        String email = getManagerEmail();
        Employee manager = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        List<Employee> teamMembers = employeeRepository.findByManager_EmployeeCode(manager.getEmployeeCode());
        long activeCount = teamMembers.stream().filter(Employee::getIsActive).count();
        return ResponseEntity.ok(new ApiResponse(true, "Team count fetched successfully",
                java.util.Map.of("total", teamMembers.size(), "active", activeCount)));
    }

    private EmployeeDirectoryResponse mapToDirectoryResponse(Employee employee) {
        return EmployeeDirectoryResponse.builder().employeeCode(employee.getEmployeeCode()).firstName(employee.getFirstName()).lastName(employee.getLastName()).email(employee.getEmail()).phone(employee.getPhone()).departmentName(employee.getDepartment() != null ? employee.getDepartment().getDepartmentName() : null).designationTitle(employee.getDesignation() != null ? employee.getDesignation().getDesignationName() : null).role(employee.getRole().name()).isActive(employee.getIsActive()).build();
    }

    private String getManagerEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("Manager not authenticated");
        }
        return auth.getName();
    }
}

