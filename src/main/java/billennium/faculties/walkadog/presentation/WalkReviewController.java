package billennium.faculties.walkadog.presentation;

import billennium.faculties.walkadog.application.WalkReviewService;
import billennium.faculties.walkadog.application.dto.WalkReviewDto;
import billennium.faculties.walkadog.domain.WalkReview;
import billennium.faculties.walkadog.infrastructure.WalkReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="${app.rest.wad-path}/walk/review")
@AllArgsConstructor
public class WalkReviewController {
    private final WalkReviewService walkReviewService;


    @PostMapping("/add")
    public String add(@RequestBody WalkReviewDto walkReviewDto) {
        return walkReviewService.add(walkReviewDto);
    }

    @GetMapping("/delete")
    public String delete(@RequestParam  long reviewId) {
        return  walkReviewService.delete(reviewId);
    }
    @PostMapping("/update")
    public String updateReviewById(@RequestParam  long reviewId, @RequestBody WalkReviewDto walkReviewDto) {
        return walkReviewService.updateReviewById(reviewId, walkReviewDto);
    }
    @GetMapping("/reviews")
    public List<WalkReviewDto> getByTrainerId(@RequestParam long trainerId) {
        return  walkReviewService.getAllByTrainerId(trainerId);

    }
}
