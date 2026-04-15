package inditex.P1.Gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import inditex.P1.Gym.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
