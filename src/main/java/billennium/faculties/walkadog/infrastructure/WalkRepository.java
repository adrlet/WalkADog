package billennium.faculties.walkadog.infrastructure;

import billennium.faculties.walkadog.domain.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WalkRepository extends JpaRepository<Walk,Long> {

    @Query(value = "SELECT * FROM WALK WHERE WALK_SLOTS_ID = ?1", nativeQuery = true)
    List<Walk> findByWalkSlotsId(long id);

    /*@Query(value = "SELECT * FROM WALK WHERE WALK_SLOTS_ID = ?1 AND " +
            "(WALK_STATUS = 'FINISHED' OR WALK_STATUS = 'CANCELLED')", nativeQuery = true)*/
    @Query(value = "SELECT * FROM WALK WHERE WALK_SLOTS_ID = ?1", nativeQuery = true)
    List<Walk> findByWalkSlotsIdAndDone(long id);
}
