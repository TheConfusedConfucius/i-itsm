package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    long countByAssignedToIdAndStatusIn(Long technicianId, java.util.Collection<IncidentStatus> statuses);
}
