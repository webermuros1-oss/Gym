package inditex.P1.Gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import inditex.P1.Gym.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
