package billennium.faculties.walkadog.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class WalkSlotsTrainerDto {

    private Long walkSlotsId;
    private LocalDate realDay;
    private LocalDateTime startDate;
    private String testStringDate;
    private Long trainerId;
    private String trainerName;
}
