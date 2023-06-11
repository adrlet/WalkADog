package billennium.faculties.walkadog.presentation;

import billennium.faculties.walkadog.application.WalkSlotsService;
import billennium.faculties.walkadog.application.dto.WalkSlotsDto;
import billennium.faculties.walkadog.application.dto.WalkSlotsTrainerDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "${app.rest.wad-path}/slots")
@AllArgsConstructor
public class WalkSlotsController {
    private final WalkSlotsService walkSlotsService;

    @PostMapping("/add")
    public String addWalkSlot(@RequestBody WalkSlotsDto walkSlotsDto) {

        return walkSlotsService.insertWalkSlots(walkSlotsDto);
    }
    @PostMapping("/delete")
    public String addWalkSlot(@RequestParam Long slotId) {

        return walkSlotsService.deleteWalkSlots(slotId);
    }
    @GetMapping("/all")
    public List<WalkSlotsTrainerDto> findAllWalks() {

        return walkSlotsService.readPresentWalkSlots();
    }
    @GetMapping("")
    public List<WalkSlotsDto> findAll(@RequestParam Long trainerId) {

        return walkSlotsService.readTrainerPresentWalkSlots(trainerId);
    }
    @GetMapping("/checkraw")
    public int checkPassWalkSlotsRaw(@RequestParam Long slotId, @RequestParam Long trainerId) {

        return walkSlotsService.checkWalkSlots(slotId, trainerId);
    }
    @GetMapping("/check")
    public String checkPassWalkSlots(@RequestParam Long slotId, @RequestParam Long trainerId) {

        return walkSlotsService.checkWalkSlotsTranslate(slotId, trainerId);
    }
    @PostMapping("/pass")
    public String passPassWalkSlots(@RequestParam Long slotId, @RequestParam Long trainerId) {

        return walkSlotsService.passWalkSlot(slotId, trainerId);
    }

    //todo poprawic responsy stringi
}
