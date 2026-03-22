package com.revworkforce.userservice.repository;
import com.revworkforce.userservice.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Integer> {
    Optional<Designation> findByDesignationName(String designationName);
    boolean existsByDesignationName(String designationName);
}

