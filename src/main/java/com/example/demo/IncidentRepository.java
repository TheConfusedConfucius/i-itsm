package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    long countByAssignedToIdAndStatusIn(Long technicianId, java.util.Collection<IncidentStatus> statuses);
}

