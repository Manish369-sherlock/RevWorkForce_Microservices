package com.revworkforce.reportingservice.repository;
import com.revworkforce.reportingservice.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Integer> {
    Optional<Designation> findByDesignationName(String designationName);
    boolean existsByDesignationName(String designationName);
}
