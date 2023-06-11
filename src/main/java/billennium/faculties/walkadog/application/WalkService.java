package billennium.faculties.walkadog.application;

import billennium.faculties.walkadog.application.dto.*;
import billennium.faculties.walkadog.application.utils.NotificationFactory;
import billennium.faculties.walkadog.domain.Notification;
import billennium.faculties.walkadog.domain.Pets;
import billennium.faculties.walkadog.domain.Trainer;
import billennium.faculties.walkadog.domain.Users;
import billennium.faculties.walkadog.domain.Walk;
import billennium.faculties.walkadog.domain.WalkReview;
import billennium.faculties.walkadog.domain.WalkSlots;
import billennium.faculties.walkadog.domain.enums.WalkStatus;
import billennium.faculties.walkadog.infrastructure.NotificationsRepository;
import billennium.faculties.walkadog.infrastructure.PetsRepository;
import billennium.faculties.walkadog.infrastructure.TrainerRepository;
import billennium.faculties.walkadog.infrastructure.UsersRepository;
import billennium.faculties.walkadog.infrastructure.WalkRepository;
import billennium.faculties.walkadog.infrastructure.WalkReviewRepository;
import billennium.faculties.walkadog.infrastructure.WalkSlotsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class WalkService {

    private final WalkRepository walkRepository;
    private final PetsRepository petsRepository;
    private final WalkSlotsRepository walkSlotsRepository;
    private final WalkReviewRepository walkReviewRepository;
    private final TrainerRepository trainerRepository;
    private final UsersRepository usersRepository;

    private final NotificationsRepository notificationsRepository;


    public String insertWalk(WalkDto walkDto) {

        List<Long> petsId = walkDto.getPetsId();
        List<Long> petsIdDup = new ArrayList<>();
        for (Long petId : petsId) {
            if (petsRepository.findById(petId).orElse(null) == null) {
                return "Wskazany pies nie istnieje";
            }
            if (petsIdDup.contains(petId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,("Psy nie mogą się powtarzać"));
            }
            petsIdDup.add(petId);
        }

        if (petsId.size() > 3)
            return "Możesz dodać co najwyżej do trzech psów";

        WalkSlots walkSlots = walkSlotsRepository.findById(walkDto.getWalkSlotsId()).orElse(null);

        if (walkSlots != null) {

            if (walkRepository.findByWalkSlotsId(walkSlots.getId()).size() == 0) {

                for (Long petId : petsId) {

                    Walk walk = Walk.builder()
                            .walkSlots(walkSlots)
                            .pets(petsRepository.findById(petId).orElse(null))
                            .startPoint(walkDto.getStartPoint())
                            .walkStatus(WalkStatus.PLANNED)
                            .build();
                    walkRepository.save(walk);
                }

                return "Spacer został dodany";
            }

            return "Ten slot został już zajęty";
        }

        return "Wskazany slot nie istnieje";
    }

    public String deleteWalk(Long slotId) {

        LocalDateTime localDateTime = LocalDateTime.now().plusDays(1);
        List<Walk> walks = walkRepository.findByWalkSlotsId(slotId);


        if (walks.size() > 0) {

            WalkSlots walkSlots = walkSlotsRepository.findById(slotId).orElse(null);
            if (walkSlots != null) {

                if (localDateTime.isBefore(walkSlots.getStartDate())) {
                    for (Walk walk : walks)
                        walkRepository.delete(walk);
                    Notification notification = NotificationFactory
                            .getTrainerNotification(walkSlots.getTrainer(),
                                    "Spacer z " + walkSlots.getStartDate().toString() + " został odwołany przez klienta.");

                    notificationsRepository.save(notification);

                    return "Spacer został usunięty";
                }

                return "Możesz usunąć spacer co najmniej 24 godziny przed jego rozpoczęciem";
            }

            return "Wskazany slot nie istnieje";
        }

        return "Wskazany spacer nie istnieje";
    }


    public String startWalk(Long slotId) {

        LocalDateTime localDateTime = LocalDateTime.now();
        List<Walk> walks = walkRepository.findByWalkSlotsId(slotId);

        if (walks.size() > 0) {

            WalkSlots walkSlots = walkSlotsRepository.findById(slotId).orElse(null);
            if (walkSlots != null) {

                if (walks.get(0).getWalkStatus() != WalkStatus.PLANNED)
                    return "Nieprawidłowe działanie";

                if (!localDateTime.isBefore(walkSlots.getStartDate()) &&
                        !localDateTime.isAfter(walkSlots.getStartDate().plusHours(1))) {

                    for (Walk walk : walks) {
                        walk.setWalkStatus(WalkStatus.IN_PROGRESS);
                        walkRepository.save(walk);
                    }

                    return "Spacer został rozpoczęty";
                }

                return "Jest zbyt wcześnie by rozpocząć spacer";
            }

            return "Wskazany slot nie istnieje";
        }

        return "Wskazany spacer nie istnieje";
    }

    public String cancelWalk(Long slotId) {

        List<Walk> walks = walkRepository.findByWalkSlotsId(slotId);

        if (walks.size() > 0) {

            if (walks.get(0).getWalkStatus() != WalkStatus.IN_PROGRESS)
                return "Nieprawidłowe działanie";

            for (Walk walk : walks) {
                walk.setWalkStatus(WalkStatus.CANCELLED);
                walkRepository.save(walk);
            }

            return "Spacer został przerwany";
        }

        return "Wskazany spacer nie istnieje";
    }

    public String finishWalk(Long slotId) {

        List<Walk> walks = walkRepository.findByWalkSlotsId(slotId);
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(1);

        if (walks.size() > 0) {

            WalkSlots walkSlots = walkSlotsRepository.findById(slotId).orElse(null);
            if (walkSlots != null) {

                if (walks.get(0).getWalkStatus() != WalkStatus.IN_PROGRESS)
                    return "Nieprawidłowe działanie";

                if (!localDateTime.isBefore(walkSlots.getStartDate())) {

                    for (Walk walk : walks) {
                        walk.setWalkStatus(WalkStatus.FINISHED);
                        walkRepository.save(walk);
                    }

                    return "Spacer został ukończony";
                }

                return "Jest zbyt wcześnie by zakończyć spacer";
            }

            return "Wskazany slot nie istnieje";
        }

        return "Wskazany spacer nie istnieje";
    }

    public List<WalkHistoryTrainerDto> readTrainerLastMonthWalks(Long trainerId) {

        if (trainerRepository.findById(trainerId).orElse(null) == null)
            return null;

        LocalDateTime localDateTime = LocalDateTime.now().minusDays(30);
        List<WalkHistoryTrainerDto> walkHistoryTrainerDtos = new ArrayList<>();
        List<WalkSlots> walkSlots = walkSlotsRepository.findAllByTrainerIdAndNewerThan(trainerId, localDateTime);

        for (WalkSlots walkSlot : walkSlots) {

            List<Walk> walks = walkRepository.findByWalkSlotsIdAndDone(walkSlot.getId());

            if (walks.size() == 0)
                continue;

            List<PetsDto> petsDtos = createPetsDtoFromWalk(walks);
            WalkReview walkReview = walkReviewRepository.findByWalk(walks.get(0)).orElse(null);

            Users user = walks.get(0).getPets().getUsers();

            WalkDto walkDto = convertToDto(walks);
            WalkSlotsDto walkSlotsDto = convertToWalkSlotsDto(walkSlot);
            UserDto userDto = UserDto.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .firstName(user.getName())
                    .lastName(user.getLastName())
                    .phoneNumber(user.getPhoneNumber())
                    .userId(user.getId())
                    .avatar(user.getAvatar())
                    .build();

            WalkReviewDto walkReviewDto = null;
            if (walkReview != null)
                walkReviewDto = WalkReviewDto.builder()
                        .rate(walkReview.getRate())
                        .comment(walkReview.getComment())
                        .walkId(walkReview.getWalk().getId())
                        .userId(walkReview.getUser().getId())
                        .build();

            walkHistoryTrainerDtos.add(WalkHistoryTrainerDto.builder()
                    .walkSlotsDto(walkSlotsDto)
                    .walkDto(walkDto)
                    .petsDto(petsDtos)
                    .walkReviewDto(walkReviewDto)
                            .userDto(userDto)
                    .build());
        }

        return walkHistoryTrainerDtos;
    }

    public List<WalkHistoryUserDto> readUserLastMonthWalks(Long userId) {

        if (usersRepository.findById(userId).orElse(null) == null)
            return null;

        LocalDateTime localDateTime = LocalDateTime.now().minusDays(30);
        List<WalkHistoryUserDto> walkHistoryUserDtos = new ArrayList<>();
        List<WalkSlots> walkSlots = walkSlotsRepository.findAllByUserIdAndNewerThan(userId, localDateTime);

        for (WalkSlots walkSlot : walkSlots) {

            List<Walk> walks = walkRepository.findByWalkSlotsIdAndDone(walkSlot.getId());

            if (walks.size() == 0)
                continue;

            List<PetsDto> petsDtos = createPetsDtoFromWalk(walks);

            Trainer trainer = walkSlot.getTrainer();

            WalkDto walkDto = convertToDto(walks);
            WalkSlotsDto walkSlotsDto = convertToWalkSlotsDto(walkSlot);
            TrainerDto trainerDto = TrainerDto.builder()
                    .username(trainer.getUsername())
                    .email(trainer.getEmail())
                    .firstName(trainer.getName())
                    .lastName(trainer.getLastName())
                    .phoneNumber(trainer.getPhoneNumber())
                    .trainerId(trainer.getId())
                    .avatar(trainer.getAvatar())
                    .experience(trainer.getExperience())
                    .build();

            WalkReview walkReview = walkReviewRepository.findByWalk(walks.get(0)).orElse(null);
            WalkReviewDto walkReviewDto = null;
            if (walkReview != null)
                walkReviewDto = WalkReviewDto.builder()
                        .rate(walkReview.getRate())
                        .comment(walkReview.getComment())
                        .walkId(walkReview.getWalk().getId())
                        .userId(walkReview.getUser().getId())
                        .build();

            walkHistoryUserDtos.add(WalkHistoryUserDto.builder()
                    .walkSlotsDto(walkSlotsDto)
                    .walkDto(walkDto)
                    .petsDto(petsDtos)
                    .walkReviewDto(walkReviewDto)
                            .trainerDto(trainerDto)
                    .build());
        }

        return walkHistoryUserDtos;
    }

    public WalkDto readWalk(Long slotId) {
        List<Walk> walks = walkRepository.findByWalkSlotsId(slotId);
        if (walks.size() == 0)
            return null;

        return convertToDto(walks);
    }

    public WalkDto convertToDto(List<Walk> walks) {

        List<Long> petsId = new ArrayList<>();
        List<Long> walksId = new ArrayList<>();

        for (Walk walk : walks) {
            petsId.add(walk.getPets().getId());
            walksId.add(walk.getId());
        }

        return WalkDto.builder()
                .walkId(walksId)
                .walkSlotsId(walks.get(0).getWalkSlots().getId())
                .petsId(petsId)
                .walkStatus(walks.get(0).getWalkStatus().toString())
                .startPoint(walks.get(0).getStartPoint())
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

    public WalkSlotsDto convertToWalkSlotsDto(WalkSlots walkSlot) {

        WalkSlotsDto walkSlotsDto = WalkSlotsDto.builder()
                .walkSlotsId(walkSlot.getId())
                .realDay(walkSlot.getRealDay())
                .startDate(walkSlot.getStartDate())
                .trainerId(walkSlot.getTrainer().getId())
                .testStringDate(dateFormatter(walkSlot.getStartDate()))
                .build();
        return walkSlotsDto;
    }

    public PetsDto convertToPetsDto(Pets pet) {

        return PetsDto.builder()
                .petName(pet.getPetName())
                .race(pet.getRace())
                .description(pet.getDescription())
                .preferences(pet.getPreferences())
                .restrictions(pet.getRestrictions())
                .avatar(pet.getAvatar())
                .petStatus(pet.getPetStatus().toString())
                .userId(pet.getUsers().getId())
                .build();
    }

    public List<PetsDto> createPetsDtoFromWalk(List<Walk> walks) {

        List<PetsDto> petsDtos = new ArrayList<>();
        for (Walk walk : walks) {
            Pets pet = walk.getPets();
            petsDtos.add(convertToPetsDto(pet));
        }
        return petsDtos;
    }
}
