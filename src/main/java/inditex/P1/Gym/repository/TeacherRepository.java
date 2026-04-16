package inditex.P1.Gym.repository;

import inditex.P1.Gym.model.Teacher;
import inditex.P1.Gym.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher.Long> {
    Optional<Teacher> findByDni(String dnu);
    List<Teacher> findAllByActiveTrue();
}