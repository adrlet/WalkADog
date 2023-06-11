package billennium.faculties.walkadog.infrastructure;

import billennium.faculties.walkadog.application.dto.WalkSlotsTrainerDto;
import billennium.faculties.walkadog.domain.Walk;
import billennium.faculties.walkadog.domain.WalkSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WalkSlotsRepository extends JpaRepository<WalkSlots,Long> {

    @Query(value = "SELECT COUNT(*) FROM WALK_SLOTS WHERE " +
            "(?1 BETWEEN START_DATE AND CAST(START_DATE + INTERVAL '3600' SECOND as DATETIME) OR " +
            "?2 BETWEEN START_DATE AND CAST(START_DATE + INTERVAL '3600' SECOND as DATETIME)) AND " +
            "TRAINER_ID = ?3 LIMIT 1", nativeQuery = true)
    int checkWalkSlotsCrossingWalkSlotsWhereTrenerId(LocalDateTime start, LocalDateTime end, Long id);

    @Query(value = "SELECT COUNT(*) FROM WALK_SLOTS WHERE REAL_DAY = ?1 AND TRAINER_ID = ?2", nativeQuery = true)
    int countWalkSlotsWhereDayAndTrainerId(LocalDate day, Long id);

    /*@Query(value = "SELECT WALK_SLOTS.*, TRAINER.NAME FROM WALK_SLOTS " +
            "LEFT JOIN TRAINER ON WALK_SLOTS.TRAINER_ID = TRAINER.ID " +
            "WHERE WALK_SLOTS.START_DATE > ?1 ORDER BY WALK_SLOTS.START_DATE DESC", nativeQuery = true)*/
    @Query(value = "SELECT * FROM WALK_SLOTS " +
            "WHERE START_DATE > ?1 ORDER BY START_DATE DESC", nativeQuery = true)
    List<WalkSlots> findAllNewerThan(LocalDateTime localDateTime);

    @Query(value = "SELECT * FROM WALK_SLOTS WHERE TRAINER_ID = ?1 AND START_DATE > ?2 ORDER BY START_DATE DESC", nativeQuery = true)
    List<WalkSlots> findAllByTrainerIdAndNewerThan(Long id, LocalDateTime localDateTime);

    @Query(value = "SELECT DISTINCT x.* " +
            "FROM (SELECT  WALK_SLOTS.* FROM WALK " +
            "LEFT JOIN WALK_SLOTS ON WALK_SLOTS.ID = WALK.WALK_SLOTS_ID " +
            "LEFT JOIN PETS ON PETS.ID = WALK.PET_ID " +
            "LEFT JOIN USERS ON USERS.ID = PETS.USER_ID " +
            "WHERE USERS.ID = ?1 AND " +
            "WALK_SLOTS.START_DATE > ?2) as x", nativeQuery = true)
    List<WalkSlots> findAllByUserIdAndNewerThan(Long id, LocalDateTime localDateTime);
}
