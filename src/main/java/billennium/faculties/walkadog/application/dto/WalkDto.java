package billennium.faculties.walkadog.application.dto;

import billennium.faculties.walkadog.domain.Pets;
import billennium.faculties.walkadog.domain.WalkSlots;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class WalkDto {

    private List<Long> walkId;
    private Long walkSlotsId;
    private List<Long> petsId;
    private String walkStatus;
    private String startPoint;
}
