package billennium.faculties.walkadog.presentation;

import billennium.faculties.walkadog.application.WalkService;
import billennium.faculties.walkadog.application.dto.WalkDto;
import billennium.faculties.walkadog.application.dto.WalkHistoryTrainerDto;
import billennium.faculties.walkadog.application.dto.WalkHistoryUserDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${app.rest.wad-path}/walks")
@AllArgsConstructor
public class WalkController {

    private final WalkService walkService;

    @PostMapping("/add")
    public String addWalk(@RequestBody WalkDto walkDto) {

        return walkService.insertWalk(walkDto);
    }
    @PostMapping("/delete")
    public String deleteWalk(@RequestParam Long slotId) {

        return walkService.deleteWalk(slotId);
    }
    @PostMapping("/start")
    public String startWalk(@RequestParam Long slotId) {

        return walkService.startWalk(slotId);
    }
    @PostMapping("/cancel")
    public String cancelWalk(@RequestParam Long slotId) {

        return walkService.cancelWalk(slotId);
    }
    @PostMapping("/finish")
    public String finishWalk(@RequestParam Long slotId) {

        return walkService.finishWalk(slotId);
    }
    @GetMapping("")
    public WalkDto readWalk(@RequestParam Long slotId) {

        return walkService.readWalk(slotId);
    }

    @GetMapping("/trainer/last")
    public List<WalkHistoryTrainerDto> lastTrainerWalks(@RequestParam Long trainerId) {

        return walkService.readTrainerLastMonthWalks(trainerId);
        //todo dodac dto trenera
    }

    @GetMapping("/user/last")
    public List<WalkHistoryUserDto> lastUserWalks(@RequestParam Long userId) {

        return walkService.readUserLastMonthWalks(userId);
        //todo dodac dto uzywtkownika
    }


    //todo dodac przekazywanie spaceru
}
//todo poprawic komunikaty bo są pomieszane
