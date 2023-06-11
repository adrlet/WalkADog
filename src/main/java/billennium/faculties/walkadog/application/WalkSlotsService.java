package billennium.faculties.walkadog.application;

import billennium.faculties.walkadog.application.dto.WalkSlotsDto;
import billennium.faculties.walkadog.application.dto.WalkSlotsTrainerDto;
import billennium.faculties.walkadog.domain.Trainer;
import billennium.faculties.walkadog.domain.Walk;
import billennium.faculties.walkadog.domain.WalkSlots;
import billennium.faculties.walkadog.infrastructure.TrainerRepository;
import billennium.faculties.walkadog.infrastructure.WalkRepository;
import billennium.faculties.walkadog.infrastructure.WalkSlotsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class WalkSlotsService {
    private WalkSlotsRepository walkSlotsRepository;
    private TrainerRepository trainerRepository;
    private WalkRepository walkRepository;

    public String insertWalkSlots(WalkSlotsDto walkSlotsDto) {
        Long trainerId = walkSlotsDto.getTrainerId();
        Trainer trainer = trainerRepository.findById(trainerId).orElse(null);
        if (trainer == null)
            return "Wskazany trener nie istnieje";

        if (!walkSlotsDto.getStartDate().toLocalDate().isEqual(walkSlotsDto.getRealDay()))
            return "Błąd zapytania";

        LocalDate realDay = walkSlotsDto.getRealDay();
        if (countWalkSlotsInDay(trainerId, realDay) < 5) {

            LocalDateTime startDate = walkSlotsDto.getStartDate();
            if (checkWalkSlotsCross(startDate, startDate.plusHours(1), trainerId) == 1) {

                if (checkDateTimeRules(realDay, startDate)) {

                    WalkSlots walkSlots = WalkSlots.builder()
                            .realDay(walkSlotsDto.getRealDay())
                            .startDate(walkSlotsDto.getStartDate())
                            .trainer(trainer)
                            .build();
                    walkSlotsRepository.save(walkSlots);

                    return "Dodano slot do kalendarza";
                }

                return "Zły termin slotu";
            }

            return "Sloty nie mogą nachodzić na siebie";
        }

        return "Nie możesz wydać więcej slotów";
    }

    private Boolean checkDateTimeRules(LocalDate date, LocalDateTime dateTime) {

        return (isWeekend(date) &&
                !dateTime.isAfter(LocalDateTime.of(date, LocalTime.of(15, 0))) &&
                !dateTime.isBefore(LocalDateTime.of(date, LocalTime.of(9, 0)))) ||
                (!dateTime.isAfter(LocalDateTime.of(date, LocalTime.of(17, 0))) &&
                        !dateTime.isBefore(LocalDateTime.of(date, LocalTime.of(8, 0))));
    }

    private Long countWalkSlotsInDay(Long trainerId, LocalDate day) {
        boolean ifTrainerExist = trainerRepository.findById(trainerId).isPresent();
        if (!ifTrainerExist)
            return (long) -1;

        return (long) walkSlotsRepository.countWalkSlotsWhereDayAndTrainerId(day, trainerId);
    }

    private Long checkWalkSlotsCross(LocalDateTime start, LocalDateTime end, Long trainerId) {
        boolean ifTrainerExist = trainerRepository.findById(trainerId).isPresent();
        if (!ifTrainerExist)
            return (long) -1;

        if (walkSlotsRepository.checkWalkSlotsCrossingWalkSlotsWhereTrenerId(start, end, trainerId) == 0)
            return (long) 1;
        else
            return (long) 0;
    }

    private static boolean isWeekend(final LocalDate ld) {
        DayOfWeek day = DayOfWeek.of(ld.get(ChronoField.DAY_OF_WEEK));
        return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY;
    }

    public String deleteWalkSlots(Long slotId) {

        WalkSlots walkSlots = walkSlotsRepository.findById(slotId).orElse(null);

        if (walkSlots != null) {

            List<Walk> walks = walkRepository.findByWalkSlotsId(walkSlots.getId());
            if (walks.size() == 0) {
                walkSlotsRepository.delete(walkSlots);
                return "Slot został usunięty";
            }

            return "Na slocie został zaplanowany spacer, nie można go usunąć";
        }

        return "Wskazany slot nie istnieje";
    }

    public List<WalkSlotsTrainerDto> readPresentWalkSlots() {

        LocalDateTime localDateTime = LocalDateTime.now();
        List<WalkSlotsTrainerDto> walkSlotsTrainerDto = new ArrayList<>();
        List<WalkSlots> walkSlots = walkSlotsRepository.findAllNewerThan(localDateTime);
        List<WalkSlots> walkSlots2 = new ArrayList<>();

        for(WalkSlots walkSlot : walkSlots) {
            List<Walk> walks = walkRepository.findByWalkSlotsId(walkSlot.getId());
            if(walks.size() == 0)
                walkSlots2.add(walkSlot);
        }

        Trainer trainer;

        for (WalkSlots walkSlot : walkSlots2) {
            trainer = walkSlot.getTrainer();
            walkSlotsTrainerDto.add(WalkSlotsTrainerDto.builder()
                    .walkSlotsId(walkSlot.getId())
                    .realDay(walkSlot.getRealDay())
                    .startDate(walkSlot.getStartDate())
                    .trainerId(trainer.getId())
                    .testStringDate(dateFormatter(walkSlot.getStartDate()))
                    .trainerName(trainer.getName())
                    .build());
        }

        return walkSlotsTrainerDto;
    }

    public List<WalkSlotsDto> readTrainerPresentWalkSlots(Long trainerId) {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<WalkSlotsDto> walkSlotsDtos = new ArrayList<>();
        List<WalkSlots> walkSlots = walkSlotsRepository.findAllByTrainerIdAndNewerThan(trainerId, localDateTime);
        List<WalkSlots> walkSlots2 = new ArrayList<>();

        for(WalkSlots walkSlot : walkSlots) {
            List<Walk> walks = walkRepository.findByWalkSlotsId(walkSlot.getId());
            if(walks.size() == 0)
                walkSlots2.add(walkSlot);
        }

        for (WalkSlots walkSlot : walkSlots2)
            walkSlotsDtos.add(convertToDto(walkSlot));

        return walkSlotsDtos;
    }

    public int checkWalkSlots(Long slotId, Long trainerId) {

        WalkSlots walkSlots = walkSlotsRepository.findById(slotId).orElse(null);
        if (walkSlots != null) {

            Trainer trainer = trainerRepository.findById(trainerId).orElse(null);
            if (trainer != null) {

                LocalDateTime localDateTime = walkSlots.getStartDate();
                LocalDate localDate = walkSlots.getRealDay();
                if (checkWalkSlotsCross(localDateTime, localDateTime.plusHours(1), trainerId) == 1 &&
                        countWalkSlotsInDay(trainerId, localDate) < 5) {

                    return 3;
                }

                return 2;
            }

            return 1;
        }

        return 0;
    }

    public String checkWalkSlotsTranslate(Long slotId, Long trainerId) {

        switch (checkWalkSlots(slotId, trainerId)) {
            case 0:
                return "Wskazany slot nie istnieje";
            case 1:
                return "Wskazany trener nie istnieje";
            case 2:
                return "Wskazany slot nie może być przekazany wskazanemu trenerowi";
            default:
        }

        return "Slot może zostać przekazany";
    }

    public String passWalkSlot(Long slotId, Long trainerId) {

        switch (checkWalkSlots(slotId, trainerId)) {
            case 0:
                return "Wskazany slot nie istnieje";
            case 1:
                return "Wskazany trener nie istnieje";
            case 2:
                return "Wskazany slot nie może być przekazany wskazanemu trenerowi";
            default:
        }

        WalkSlots walkSlots = walkSlotsRepository.findById(slotId).orElse(null);
        walkSlots.setTrainer(trainerRepository.findById(trainerId).orElse(null));
        walkSlotsRepository.save(walkSlots);

        return "Slot został przekazany";
    }

    private WalkSlotsDto convertToDto(WalkSlots walkSlots) {

        return WalkSlotsDto.builder()
                .walkSlotsId(walkSlots.getId())
                .realDay(walkSlots.getRealDay())
                .startDate(walkSlots.getStartDate())
                .trainerId(walkSlots.getTrainer().getId())
                .testStringDate(dateFormatter(walkSlots.getStartDate()))
                .build();
    }

    public String dateFormatter(LocalDateTime localDateTime) {
        String hours = String.valueOf(localDateTime.getHour());
        String minutes = String.valueOf(localDateTime.getMinute());
        String localDate = localDateTime.toLocalDate().toString();
        if (localDateTime.getMinute() < 10) {
            String date = localDate + " " + hours + ":" + minutes + "0";
            return date;
        }
        String date = localDate + " " + hours + ":" + minutes;
        return date;
    }
}
