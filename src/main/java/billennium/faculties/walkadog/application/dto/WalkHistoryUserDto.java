package billennium.faculties.walkadog.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class WalkHistoryUserDto {
    private WalkDto walkDto;
    private List<PetsDto> petsDto;
    private WalkSlotsDto walkSlotsDto;
    private WalkReviewDto walkReviewDto;
    private TrainerDto trainerDto;
}