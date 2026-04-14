package inditex.P1.Gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import inditex.P1.Gym.entity.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
