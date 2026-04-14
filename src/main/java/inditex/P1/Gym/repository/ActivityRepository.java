package inditex.P1.Gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import inditex.P1.Gym.entity.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
