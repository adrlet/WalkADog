package billennium.faculties.walkadog.infrastructure;

import billennium.faculties.walkadog.domain.Notification;
import billennium.faculties.walkadog.domain.Trainer;
import billennium.faculties.walkadog.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationsRepository extends JpaRepository<Notification, Long> {
    List<Notification> getAllByUser(Users user);
    List<Notification> getAllByTrainer(Trainer trainer);
    @Override
    Optional<Notification> findById(Long aLong);
}
