package billennium.faculties.walkadog.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class WalkReviewDto {

    private Integer rate;
    private String comment;
    private Long walkId;
    private Long userId;
}
