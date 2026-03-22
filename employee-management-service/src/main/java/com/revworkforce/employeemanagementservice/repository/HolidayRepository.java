package com.revworkforce.employeemanagementservice.repository;
import com.revworkforce.employeemanagementservice.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Integer>{
    List<Holiday> findByYearOrderByHolidayDateAsc(Integer year);
    boolean existsByHolidayDate(LocalDate holidayDate);
    List<Holiday> findByHolidayDateBetween(LocalDate startDate, LocalDate endDate);
}
