package inditex.P1.Gym.repository;

import inditex.P1.Gym.model.Enrollment;
import inditex.P1.Gym.model.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {

    Optional<Enrollment> findByActivityIdAndUserId(Long activityId, Long userId);

    boolean existsByActivityIdAndUserId(Long activityId, Long userId);

    List<Enrollment> findByUserId(Long userId);

    long countByUserIdAndActivityDateAfter(Long userId, LocalDateTime date);
}