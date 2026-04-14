package inditex.P1.Gym.service;

import java.util.List;

import org.springframework.stereotype.Service;

import inditex.P1.Gym.entity.User;
import inditex.P1.Gym.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getActiveUsers() {
        return userRepository.findAll().stream()
                .filter(User::isActive)
                .toList();
    }
}
