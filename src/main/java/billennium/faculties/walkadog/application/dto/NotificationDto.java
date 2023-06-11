package billennium.faculties.walkadog.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {
    public long id;
    public long userId;
    public long trainerId;
    public LocalDateTime sendTime;
    public String text;
    public boolean viewed;

}
