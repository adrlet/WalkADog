package billennium.faculties.walkadog.infrastructure;

import billennium.faculties.walkadog.domain.Pets;
import billennium.faculties.walkadog.domain.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetsRepository extends JpaRepository<Pets,Long> {
    @Query(value = "SELECT * FROM PETS WHERE USER_ID = :userId" ,nativeQuery = true)
    List<Pets> findByUsersId(@Param("userId") Long userId);

}
