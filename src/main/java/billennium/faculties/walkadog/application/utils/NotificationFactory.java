package billennium.faculties.walkadog.application.utils;

import billennium.faculties.walkadog.domain.Notification;
import billennium.faculties.walkadog.domain.Trainer;
import billennium.faculties.walkadog.domain.Users;
import billennium.faculties.walkadog.domain.WalkSlots;

import java.time.LocalDateTime;

public class NotificationFactory {
    public NotificationFactory() {
    }

    static public Notification getUserNotification(Users user, String text) {
        Notification notification = getEmptyNotification(text);
        notification.setUser(user);
        return notification;

    }
    static public Notification getTrainerNotification(Trainer trainer, String text) {
        Notification notification = getEmptyNotification(text);
        notification.setTrainer(trainer);
        return notification;
    }

    private static Notification getEmptyNotification(String text) {
        return Notification.builder()
                .sendTime(LocalDateTime.now())
                .viewed(false)
                .text(text)
                .build();
    }
}