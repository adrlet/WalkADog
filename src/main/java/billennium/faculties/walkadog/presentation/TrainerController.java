package billennium.faculties.walkadog.presentation;

import billennium.faculties.walkadog.application.TrainerService;
import billennium.faculties.walkadog.domain.Trainer;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "${app.rest.wad-path}/trainers")
@AllArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @GetMapping("/test")
    public List<Trainer> getTrainers() {
        return trainerService.getAll();
    }

}
