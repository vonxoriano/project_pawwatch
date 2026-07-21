package edu.cit.soriano.pawwatch.feature.notification;

import edu.cit.soriano.pawwatch.feature.auth.User;
import edu.cit.soriano.pawwatch.feature.auth.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    // Called internally by other services (e.g. AdoptionApplicationService)
    // to create a notification for a user. Not exposed as an endpoint.
    public void createNotification(User user, String message, Long applicationId) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setApplicationId(applicationId);
        notification.setStatus("UNREAD");
        notificationRepository.save(notification);
    }

    public List<Notification> getMyNotifications(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findByUserOrderByDateSentDesc(user);
    }

    public Notification markAsRead(Long notificationId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Unauthorized");
        }

        notification.setStatus("READ");
        return notificationRepository.save(notification);
    }

    public void markAllAsRead(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Notification> unread = notificationRepository.findByUserAndStatus(user, "UNREAD");
        for (Notification notification : unread) {
            notification.setStatus("READ");
        }
        notificationRepository.saveAll(unread);
    }
}