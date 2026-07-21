package edu.cit.soriano.pawwatch.feature.notification;

import edu.cit.soriano.pawwatch.feature.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByDateSentDesc(User user);
    List<Notification> findByUserAndStatus(User user, String status);
}