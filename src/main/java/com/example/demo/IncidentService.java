package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class IncidentService {
	private final IncidentRepository repo;
	private final UserRepository users;
	private final ItServiceRepository services;
	private final CategoryRepository categories;
	private final PriorityStrategy priorityStrategy;
	private final AssignmentService assignmentService;

	public IncidentService(IncidentRepository repo, UserRepository users, ItServiceRepository services,
						   CategoryRepository categories, PriorityStrategy priorityStrategy, AssignmentService assignmentService) {
		this.repo = repo; this.users = users; this.services = services;
		this.categories = categories; this.priorityStrategy = priorityStrategy;
		this.assignmentService=assignmentService;
	}

	@Transactional
	public Incident create(CreateIncidentRequest req) {
		User reporter = users.findById(req.reporterId())
				.orElseThrow(() -> new ResourceNotFoundException("User", req.reporterId()));
		ItService svc = services.findById(req.serviceId())
				.orElseThrow(() -> new ResourceNotFoundException("Service", req.serviceId()));
		Category cat = categories.findById(req.categoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category", req.categoryId()));

		PriorityResult pr = priorityStrategy.calculate(req.impact(), req.urgency(), svc.getBusinessCriticality());
		return repo.save(new Incident(req.description(), reporter, svc, cat, req.impact(), req.urgency(), pr));
	}
	public List<Incident> findAll() { return repo.findAll(); }

	public Incident get(Long id) {
		return repo.findById(id).orElseThrow(() -> new IncidentNotFoundException(id));
	}

	@Transactional public Incident classify(Long id)    { Incident i = get(id); i.classify(); return i; }
	@Transactional
	public Incident assign(Long id) {
		Incident i = get(id);
		i.assign(assignmentService.pick(i));
		return i;
	}
	public Technician recommend(Long id) {
		return assignmentService.pick(get(id));
	}
	@Transactional public Incident acknowledge(Long id) { Incident i = get(id); i.acknowledge(); return i; }
	@Transactional public Incident startWork(Long id) { Incident i = get(id); i.startWork(); return i; }
	@Transactional public Incident resolve(Long id)   { Incident i = get(id); i.resolve(); return i; }
	@Transactional public Incident verify(Long id)    { Incident i = get(id); i.verify(); return i; }
	@Transactional public Incident close(Long id)     { Incident i = get(id); i.close(); return i; }
}




