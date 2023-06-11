package billennium.faculties.walkadog.presentation;

import billennium.faculties.walkadog.application.PetsService;
import billennium.faculties.walkadog.application.dto.PetsDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("${app.rest.wad-path}/pets")
@AllArgsConstructor
public class PetsController {

    private final PetsService petsService;

    @PostMapping("/add")
    public void createPet(@RequestBody PetsDto petsDto) {
        petsService.createPet(petsDto);
    }

    @GetMapping("/pet")
    public PetsDto getPetById(@RequestParam Long petId) {
        return petsService.findPetById(petId);
    }

    @GetMapping("/archived")
    public List<PetsDto> getAllActivePetsFromUser(@RequestParam Long userId) {
        return petsService.getAllActivePetsFromUser(userId);
    }
    @GetMapping("/active")
    public List<PetsDto> getAllArchivedPetsFromUser(@RequestParam Long userId) {
        return petsService.getAllArchivedPetsFromUser(userId);
    }
    @GetMapping("")
    public List<PetsDto> getAllPetsFromUser(@RequestParam Long userId) {
        return petsService.getAllPetsFromUser(userId);
    }

    @PostMapping("/archive")
    public void archivePet(@RequestParam Long petId) {
        petsService.archivePet(petId);
    }

    @PostMapping("/update")
    public void updatePetById(@RequestBody PetsDto petsDto, @RequestParam Long petId) {
        petsService.updatePet(petsDto, petId);
    }
}
