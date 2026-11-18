package br.com.bejacob.backend.controller;

import br.com.bejacob.backend.dto.notification.NotificationResponse;
import br.com.bejacob.backend.service.NotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@SecurityRequirement(name = "bearer-key")
@PreAuthorize("hasRole('ADMIN')")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // LISTAR NOTIFICAÇÕES
    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> findAll(
            @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(notificationService.findAll(pageable));
    }

    // QUANTIDADE DE NÃO LIDAS
    @GetMapping("/unread-count")
    public ResponseEntity<Long> countUnread() {
        return ResponseEntity.ok(notificationService.countUnread());
    }

    // MARCAR COMO LIDA
    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    // MARCAR TODAS COMO LIDAS
    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.noContent().build();
    }

    // LIMPAR NOTIFICAÇÃO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // PARA FUNCIONAMENTO DA PROCEDURE
    @DeleteMapping("/clean-old-notifications")
    public ResponseEntity<Void> cleanupOldNotifications() {
        notificationService.cleanupOldNotifications();
        return ResponseEntity.noContent().build();
    }
}
