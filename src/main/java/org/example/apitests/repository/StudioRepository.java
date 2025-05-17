package org.example.apitests.repository;

import org.example.apitests.model.Studio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudioRepository extends JpaRepository<Studio, Long> {
    List<Studio> findByNameContainingIgnoreCase(String namePart);
}
