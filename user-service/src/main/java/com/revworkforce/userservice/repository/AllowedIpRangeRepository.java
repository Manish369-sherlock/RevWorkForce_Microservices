package com.revworkforce.userservice.repository;

import com.revworkforce.userservice.model.AllowedIpRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllowedIpRangeRepository extends JpaRepository<AllowedIpRange, Integer> {
    List<AllowedIpRange> findByIsActiveTrue();

    boolean existsByIpRange(String ipRange);

    List<AllowedIpRange> findAllByOrderByCreatedAtDesc();
}

