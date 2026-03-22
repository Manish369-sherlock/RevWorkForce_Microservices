package com.revworkforce.performanceservice.repository;

import com.revworkforce.performanceservice.model.Goal;
import com.revworkforce.performanceservice.model.enums.GoalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Integer> {
    Page<Goal> findByEmployee_EmployeeId(Integer employeeId, Pageable pageable);
    Page<Goal> findByEmployee_EmployeeIdAndYear(Integer employeeId, Integer year, Pageable pageable);

    List<Goal> findByEmployeeEmployeeIdAndYear(Integer employeeId, Integer year);
    Page<Goal> findByEmployee_EmployeeIdAndStatus(Integer employeeId, GoalStatus status, Pageable pageable);
    @Query("select g from Goal g where g.employee.employeeCode = :employeeCode and g.employee.manager.employeeCode = :managerCode")
    Page<Goal> findByEmployeeCodeAndManagerCode(@Param("employeeCode") String employeeCode, @Param("managerCode") String managerCode, Pageable pageable);
    @Query("select g from Goal g where g.employee.manager.employeeCode = :managerCode")
    Page<Goal> findByManagerCode(@Param("managerCode") String managerCode, Pageable pageable);
}
