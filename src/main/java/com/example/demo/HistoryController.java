package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
class HistoryController {
    private final AuditRecordRepository audit;
    private final NotificationRepository notifications;
    HistoryController(AuditRecordRepository audit, NotificationRepository notifications) {
        this.audit = audit; this.notifications = notifications;
    }

    @GetMapping("/incidents/{id}/history")
    List<AuditRecord> history(@PathVariable Long id) {
        return audit.findByIncidentIdOrderByTimestampAsc(id);
    }

    @GetMapping("/users/{id}/notifications")
    List<Notification> notificationsFor(@PathVariable Long id) {
        return notifications.findByRecipientIdOrderByCreatedAtDesc(id);
    }
}