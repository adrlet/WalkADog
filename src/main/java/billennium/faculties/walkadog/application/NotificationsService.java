package billennium.faculties.walkadog.application;

import billennium.faculties.walkadog.application.dto.NotificationDto;
import billennium.faculties.walkadog.application.utils.NotificationFactory;
import billennium.faculties.walkadog.domain.Notification;
import billennium.faculties.walkadog.infrastructure.NotificationsRepository;
import billennium.faculties.walkadog.infrastructure.TrainerRepository;
import billennium.faculties.walkadog.infrastructure.UsersRepository;
import billennium.faculties.walkadog.infrastructure.WalkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationsService {

    private final UsersRepository usersRepository;
    private final WalkRepository walkRepository;
    private final NotificationsRepository notificationsRepository;
    private final TrainerRepository trainerRepository;

    public String notifyUser(Long userId, Long walkId) {
        var user = usersRepository.findById(userId);
        var walk = walkRepository.findById(walkId);

        if(user.isEmpty()) {
            return "Powiadamiany użytkownik nie istnieje";
        }
        if(walk.isEmpty()) {
            return "Spacer na temat którego chcesz powiadomić nie istnieje";
        }

        String sb = user.get().getName() +
                ", przypominamy o spacerze" +
                " który jest zaplanowany na " +
                walk.get().getWalkSlots().getStartDate().toString();

        Notification notification = NotificationFactory.getUserNotification(
                user.get(),
                sb
        );
        notificationsRepository.save(notification);
        return "Wysłano";
    }

    public List<NotificationDto> getUserNotifications(long userId) {
        var user = usersRepository.findById(userId);
        if(user.isEmpty()) {
            throw new IllegalStateException("Get invalid user exceptions");
        }

        var notifications = notificationsRepository.getAllByUser(user.get());

        List<NotificationDto> notificationDtos = new ArrayList<>();
        for (Notification n:
                notifications) {
            NotificationDto notificationDto = NotificationDto.builder()
                    .text(n.getText())
                    .sendTime(n.getSendTime())
                    .viewed(n.isViewed())
                    .userId(n.getUser().getId())
                    .id(n.getId())
                    .build();
            notificationDtos.add(notificationDto);
            n.setViewed(true);
        }
        notificationsRepository.saveAllAndFlush(notifications);

        return notificationDtos;

    }

    public String deleteUserNotification(long userId, long notificationId) {
        var user = usersRepository.findById(userId);
        if(user.isEmpty()) {
            return "nie ma takiego użytkownika";
        }

        var notification = notificationsRepository.findById(notificationId);
        if(notification.isEmpty()) {
            return "nie ma takiego powiadomienia";
        }

        if(notification.get().getUser() != user.get()) {
            return "Zgloszenie do prokuratury za hacking zostało wysłane";
        }

        notificationsRepository.delete(notification.get());
        return "Powiadomienie zostało usunięte";
    }

    public List<NotificationDto> getTrainerNotifications(long trainerId) {
        var trainer = trainerRepository.findById(trainerId);
        if(trainer.isEmpty()) {
            throw new IllegalStateException("Invalid trainer");
        }

        var notifications = notificationsRepository.getAllByTrainer(trainer.get());

        List<NotificationDto> notificationDtos = new ArrayList<>();
        for (Notification n:
                notifications) {
            NotificationDto notificationDto = NotificationDto.builder()
                    .text(n.getText())
                    .sendTime(n.getSendTime())
                    .viewed(n.isViewed())
                    .trainerId(n.getTrainer().getId())
                    .id(n.getId())
                    .build();
            notificationDtos.add(notificationDto);
            n.setViewed(true);
        }
        notificationsRepository.saveAllAndFlush(notifications);

        return notificationDtos;
    }

    public String deleteTrainerNotification(long trainerId, long notificationId) {
        var trainer = trainerRepository.findById(trainerId);
        if(trainer.isEmpty()) {
            throw new IllegalStateException("Invalid trainer");
        }
        var notification = notificationsRepository.findById(notificationId);
        if(notification.isEmpty()) {
            return "nie ma takiego powiadomienia";
        }

        if(notification.get().getTrainer() != trainer.get()) {
            return "Zgloszenie do prokuratury za hacking zostało wysłane";
        }

        notificationsRepository.delete(notification.get());
        return "Powiadomienie zostało usunięte";
    }
}
