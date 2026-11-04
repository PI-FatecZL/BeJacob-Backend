package br.com.bejacob.backend.service;

import br.com.bejacob.backend.dto.notification.NotificationResponse;
import br.com.bejacob.backend.entity.Appointment;
import br.com.bejacob.backend.entity.Notification;
import br.com.bejacob.backend.exception.notification.NotificationNotFoundException;
import br.com.bejacob.backend.mapper.NotificationMapper;
import br.com.bejacob.backend.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    public NotificationService(NotificationRepository repository, NotificationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

  
    @Transactional
    public void notifyNewAppointment(Appointment appointment) {
        repository.save(mapper.toEntity(appointment));
    }

 
    @Transactional(readOnly = true)
    public Page<NotificationResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

   
    @Transactional(readOnly = true)
    public long countUnread() {
        return repository.countByIsReadFalse();
    }

   
    @Transactional
    public NotificationResponse markAsRead(Long id) {
        Notification notification = findNotificationById(id);
        notification.markAsRead();
        return mapper.toResponse(repository.save(notification));
    }

  
    @Transactional
    public void markAllAsRead() {
        repository.markAllAsRead();
    }

  
    @Transactional
    public void delete(Long id) {
        Notification notification = findNotificationById(id);
        repository.delete(notification);
    }

    private Notification findNotificationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notificação não encontrada."));
    }
}
