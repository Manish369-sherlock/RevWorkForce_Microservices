package com.revworkforce.userservice.repository;
import com.revworkforce.userservice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findByDepartmentName(String departmentName);
    boolean existsByDepartmentName(String departmentName);
}

