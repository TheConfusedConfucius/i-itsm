package com.example.demo;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Entity
public class Incident {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String description;
	@Enumerated(EnumType.STRING)
	private IncidentStatus status;
	private LocalDateTime createdAt;

	@ManyToOne(optional = false) private User reporter;
	@ManyToOne(optional = false) private ItService service;
	@ManyToOne(optional = false) private Category category;
	@ManyToOne private Technician assignedTo;

	private int impact;
	private int urgency;
	private int priorityScore;
	@Enumerated(EnumType.STRING)
	private Priority priority;

	protected Incident() {}

	public Incident(String description, User reporter, ItService service, Category category,
					int impact, int urgency, PriorityResult priorityResult) {
		this.description = description;
		this.reporter = reporter;
		this.service = service;
		this.category = category;
		this.impact = impact;
		this.urgency = urgency;
		this.priority = priorityResult.priority();
		this.priorityScore = priorityResult.score();
		this.status = IncidentStatus.NEW;
		this.createdAt = LocalDateTime.now();
	}

	public Long getId() { return id; }
	public String getDescription() { return description; }
	public IncidentStatus getStatus() { return status; }
	public LocalDateTime getCreatedAt() { return createdAt; }
	public User getReporter() { return reporter; }
	public ItService getService() { return service; }
	public Category getCategory() { return category; }
	public int getImpact() { return impact; }
	public int getUrgency() { return urgency; }
	public Priority getPriority() { return priority; }
	public int getPriorityScore() { return priorityScore; }
	public Technician getAssignedTo() { return assignedTo; }
	private IncidentState state() {
		return IncidentStates.of(status);
	}

	public void classify() {
		status = state().classify();
	}

	public void assign(Technician technician) {

		status = state().assign();      // throws first if the state is wrong
		this.assignedTo = technician;
	}

	public void acknowledge() {
		status = state().acknowledge();
	}
	public void startWork() { status = state().startWork(); }
	public void resolve()   { status = state().resolve(); }
	public void verify()    { status = state().verify(); }
	public void close()     { status = state().close(); }
}
