package com.revworkforce.userservice.service;

import com.revworkforce.userservice.dto.DepartmentRequest;
import com.revworkforce.userservice.exception.BadRequestException;
import com.revworkforce.userservice.exception.DuplicateResourceException;
import com.revworkforce.userservice.exception.InvalidActionException;
import com.revworkforce.userservice.exception.ResourceNotFoundException;
import com.revworkforce.userservice.model.Department;
import com.revworkforce.userservice.repository.DepartmentRepository;
import com.revworkforce.userservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public Department createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByDepartmentName(request.getDepartmentName())) {
            throw new DuplicateResourceException("Department '" + request.getDepartmentName() + "' already exists");
        }
        Department department = Department.builder()
                .departmentName(request.getDepartmentName())
                .description(request.getDescription())
                .build();
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Integer departmentId, DepartmentRequest request) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));
        departmentRepository.findByDepartmentName(request.getDepartmentName()).ifPresent(existing -> {
            if (!existing.getDepartmentId().equals(departmentId)) {
                throw new DuplicateResourceException("Department '" + request.getDepartmentName() + "' already exists");
            }
        });
        department.setDepartmentName(request.getDepartmentName());
        department.setDescription(request.getDescription());
        return departmentRepository.save(department);
    }

    public Department deactivateDepartment(Integer departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));
        long employeeCount = employeeRepository.countByDepartment_DepartmentId(departmentId);
        if (employeeCount > 0) {
            throw new BadRequestException("Cannot deactivate department. " + employeeCount + " employee(s) are assigned to it.");
        }
        if (!department.getIsActive()) {
            throw new InvalidActionException("Department is already deactivated");
        }
        department.setIsActive(false);
        return departmentRepository.save(department);
    }

    public Department activateDepartment(Integer departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));
        if (department.getIsActive()) {
            throw new InvalidActionException("Department is already active");
        }
        department.setIsActive(true);
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Integer departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentId));
    }
}

