package billennium.faculties.walkadog.application;

import billennium.faculties.walkadog.application.dto.WalkReviewDto;
import billennium.faculties.walkadog.domain.Users;
import billennium.faculties.walkadog.domain.Walk;
import billennium.faculties.walkadog.domain.WalkReview;
import billennium.faculties.walkadog.infrastructure.TrainerRepository;
import billennium.faculties.walkadog.infrastructure.UsersRepository;
import billennium.faculties.walkadog.infrastructure.WalkRepository;
import billennium.faculties.walkadog.infrastructure.WalkReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class WalkReviewService {
    private final WalkReviewRepository walkReviewRepository;
    private final WalkRepository walkRepository;
    private final UsersRepository usersRepository;
    private final TrainerRepository trainerRepository;


    public String add(WalkReviewDto walkReviewDto){
        //TODO: sprawdzenie, czy add woła osoba która jest w spacerze

        verifyDto(walkReviewDto);


        Walk walk = walkRepository.getById(walkReviewDto.getWalkId());
        boolean ifWalkReview = walkReviewRepository.findByWalk(walk).isPresent();
        if(ifWalkReview) {
            throw new IllegalStateException("Walk reviewed");
        }


        Users user  = usersRepository.getById(walkReviewDto.getUserId());


        WalkReview walkReview = WalkReview.builder()
                .walk(walk)
                .comment(walkReviewDto.getComment())
                .rate(walkReviewDto.getRate())
                .user(user)
                .build();

        walkReviewRepository.save(walkReview);




        return "Review added";

    }

    private void verifyDto(WalkReviewDto walkReviewDto) {
        boolean ifWalkExists = walkRepository.findById(walkReviewDto.getWalkId()).isPresent();
        boolean ifUserExists = usersRepository.findById(walkReviewDto.getUserId()).isPresent();

        if( walkReviewDto.getRate() < 1 || walkReviewDto.getRate() > 5) {
            throw new IllegalStateException("Invalid rate passed");
        }
        if(!ifWalkExists) {
            throw new IllegalStateException("Specified walk doesn't exist");
        }
        if(!ifUserExists) {
            throw new IllegalStateException("Specified user doesn't exist");
        }
    }

    public String delete(long reviewId) {
        //TODO: ograniczenie metody do użytkoenika wywołującego metodę
        var review  = walkReviewRepository.findById(reviewId);
        if(review.isEmpty())
        {
            return "Deleting not existing review";
        }

        WalkReview walkReview = review.get();
        walkReviewRepository.delete(walkReview);
        return "Review deleted";


    }

    public String updateReviewById(long reviewId, @NotNull  WalkReviewDto walkReviewDto) {
        verifyDto(walkReviewDto);
        var result = walkReviewRepository.findById(reviewId);
        if(result.isEmpty()) {
            return "Updating not existing review";
        }

        WalkReview walkReview = result.get();

        if(!Objects.equals(walkReview.getWalk().getId(), walkReviewDto.getWalkId())) {
            return  "Updating walkId isn't supported";
        }
        if(!Objects.equals(walkReview.getUser().getId(), walkReviewDto.getUserId())) {
            return  "Updating userId isn't supported";
        }

        walkReview.setComment(walkReviewDto.getComment());
        walkReview.setRate(walkReviewDto.getRate());
        walkReviewRepository.save(walkReview);
        return "review updated";
    }

    public List<WalkReviewDto> getAllByTrainerId(long trainerId) {
        var result = trainerRepository.findById(trainerId);
        if(result.isEmpty()) {
            throw new IllegalStateException("Requested not existing trainer reviews");
        }

        var reviews =  walkReviewRepository.findAllByWalk_WalkSlots_Trainer(result.get());
        WalkReviewDto walkReviewDto;
        List<WalkReviewDto> walkReviewDtoList = new ArrayList<>();
        for (WalkReview walk:
             reviews) {
            walkReviewDto = WalkReviewDto.builder()
                    .comment(walk.getComment())
                    .rate(walk.getRate())
                    .walkId(walk.getId())
                    .userId(walk.getUser().getId())
                    .build();
            walkReviewDtoList.add(walkReviewDto);
        }
        return walkReviewDtoList;


    }
}
