package billennium.faculties.walkadog.infrastructure;

import billennium.faculties.walkadog.domain.Trainer;
import billennium.faculties.walkadog.domain.Walk;
import billennium.faculties.walkadog.domain.WalkReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalkReviewRepository extends JpaRepository<WalkReview,Long> {
    Optional<WalkReview> findById(Long id);
    Optional<WalkReview> findByWalk(Walk walk);
    List<WalkReview>  findAllByWalk_WalkSlots_Trainer(Trainer trainer);
    WalkReview getById(Long id);
;}
