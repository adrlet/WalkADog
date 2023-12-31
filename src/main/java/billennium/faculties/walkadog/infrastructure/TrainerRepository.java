package billennium.faculties.walkadog.infrastructure;

import billennium.faculties.walkadog.domain.Trainer;
import billennium.faculties.walkadog.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByEmail(String email);
    Optional<Trainer> findByUsername(String username);
}
