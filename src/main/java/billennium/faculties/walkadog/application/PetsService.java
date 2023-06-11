package billennium.faculties.walkadog.application;

import billennium.faculties.walkadog.application.dto.PetsDto;
import billennium.faculties.walkadog.domain.Pets;
import billennium.faculties.walkadog.domain.enums.PetStatus;
import billennium.faculties.walkadog.infrastructure.PetsRepository;
import billennium.faculties.walkadog.infrastructure.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PetsService {

    private final PetsRepository petsRepository;
    private final UsersRepository usersRepository;

    public void createPet(PetsDto petsDto) {
        Pets pet = Pets.builder()
                .users(usersRepository.getById(petsDto.getUserId()))
                .avatar(petsDto.getAvatar())
                .description(petsDto.getDescription())
                .petName(petsDto.getPetName())
                .petStatus(PetStatus.ACTIVE)
                .preferences(petsDto.getPreferences())
                .race(petsDto.getRace())
                .restrictions(petsDto.getRestrictions())
                .build();
        petsRepository.save(pet);
    }

    public void archivePet(Long petId) {
        Pets pet = petsRepository.getById(petId);
        pet.setPetStatus(PetStatus.ARCHIVED);
        petsRepository.save(pet);
    }

    public void updatePet(PetsDto petsDto, Long petId) {
        Pets pet = petsRepository.getById(petId);
        if (ifParameterIsNotNull(petsDto.getPetName())) {
            pet.setPetName(petsDto.getPetName());
        }
        if (petsDto.getAvatar() != null) {
            pet.setAvatar(petsDto.getAvatar());
        }
        if (ifParameterIsNotNull(petsDto.getRace())) {
            pet.setRace(petsDto.getRace());
        }
        if (ifParameterIsNotNull(petsDto.getDescription())) {
            pet.setDescription(petsDto.getDescription());
        }
        if (ifParameterIsNotNull(petsDto.getPreferences())) {
            pet.setPreferences(pet.getPreferences());
        }
        if (ifParameterIsNotNull(petsDto.getRestrictions())) {
            pet.setRestrictions(pet.getRestrictions());
        }
        petsRepository.save(pet);
    }

    public boolean ifParameterIsNotNull(String parameter) {
        if (!parameter.equals("") && parameter != null) {
            return true;
        }
        return false;
    }

    public List<PetsDto> getAllActivePetsFromUser(Long userId) {
        List<Pets> pets = petsRepository.findByUsersId(userId);
        List<PetsDto> petsDtoList = new ArrayList<>();
        for (Pets pet : pets) {
            if (pet.getPetStatus().equals(PetStatus.ACTIVE)) {
                PetsDto petsDto = convertToDto(pet);
                petsDtoList.add(petsDto);
            }
        }
        return petsDtoList;
    }

    public List<PetsDto> getAllArchivedPetsFromUser(Long userId) {
        List<Pets> pets = petsRepository.findByUsersId(userId);
        List<PetsDto> petsDtoList = new ArrayList<>();
        for (Pets pet : pets) {
            if (pet.getPetStatus().equals(PetStatus.ARCHIVED)) {
                PetsDto petsDto = convertToDto(pet);
                petsDtoList.add(petsDto);
            }
        }
        return petsDtoList;
    }

    public List<PetsDto> getAllPetsFromUser(Long userId) {
        List<Pets> pets = petsRepository.findByUsersId(userId);
        List<PetsDto> petsDtoList = new ArrayList<>();
        for (Pets pet : pets) {
            PetsDto petsDto = convertToDto(pet);
            petsDtoList.add(petsDto);
        }
        return petsDtoList;
    }

    public PetsDto findPetById(Long petId) {
        Pets pet = petsRepository.getById(petId);
        return convertToDto(pet);
    }

    public PetsDto convertToDto(Pets pet) {
        return PetsDto.builder()
                .avatar(pet.getAvatar())
                .petName(pet.getPetName())
                .description(pet.getDescription())
                .petStatus(pet.getPetStatus().toString())
                .userId(pet.getUsers().getId())
                .preferences(pet.getPreferences())
                .race(pet.getRace())
                .restrictions(pet.getRestrictions())
                .petId(pet.getId())
                .build();
    }

}
