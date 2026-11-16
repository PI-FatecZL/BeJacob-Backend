package br.com.bejacob.backend.repository;

import br.com.bejacob.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    long countByIsReadFalse();

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.isRead = false")
    void markAllAsRead();

    // PARA A PROCEDURE
    @Modifying
    @Query(value = "CALL sp_cleanup_old_notifications(:daysOld)", nativeQuery = true)
    void cleanupOldNotifications(@Param("daysOld") int daysOld);
}
