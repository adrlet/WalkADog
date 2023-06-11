package billennium.faculties.walkadog.presentation;

import billennium.faculties.walkadog.application.NotificationsService;
import billennium.faculties.walkadog.application.dto.NotificationDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.rest.wad-path}/notifications")
@AllArgsConstructor
public class NotificationsController {

    private final NotificationsService notificationsService;

    @GetMapping("/notify")
    public String notifyUser(@RequestParam("user-id") Long userId, @RequestParam("walk-id") Long walkId) {
        return notificationsService.notifyUser(userId, walkId);
    }

    @GetMapping("/user/get")
    public List<NotificationDto> getUserNotifications(@RequestParam long userId)
    {
        return  notificationsService.getUserNotifications(userId);
    }

    @GetMapping("/user/delete")
    public String deleteUserNotification(@RequestParam long userId, @RequestParam long notificationId){
        return notificationsService.deleteUserNotification(userId, notificationId);
    }

    @GetMapping("/trainer/get")
    public List<NotificationDto> getTrainerNotifications(@RequestParam long trainerId)
    {
        return  notificationsService.getTrainerNotifications(trainerId);
    }
    @GetMapping("/trainer/delete")
    public String deleteTrainerNotification(@RequestParam long trainerId, @RequestParam long notificationId){
        return notificationsService.deleteTrainerNotification(trainerId, notificationId);
    }
}
