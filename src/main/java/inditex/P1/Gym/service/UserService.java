package inditex.P1.Gym.service;

import inditex.P1.Gym.DTO.UserRequestDTO;
import inditex.P1.Gym.DTO.UserResponseDTO;
import inditex.P1.Gym.exception.ObjectNotFoundException;
import inditex.P1.Gym.model.User;
import inditex.P1.Gym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User", id));
        return toResponseDTO(user);
    }

    public List<UserResponseDTO> getActiveUsers() {
        return userRepository.findByActiveTrue()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepository.findByDni(dto.getDni()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el DNI: " + dto.getDni());
        }
        User user = toEntity(dto);
        return toResponseDTO(userRepository.save(user));
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User", id));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDni(dto.getDni());
        user.setRegistrationYear(dto.getRegistrationYear());
        user.setActive(dto.isActive());
        user.setImageUrl(dto.getImageUrl());
        return toResponseDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ObjectNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }

    // ---Mapeos ----

    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getDni(),
                user.getRegistrationYear(),
                user.isActive(),
                user.getImageUrl()
        );
    }

    private User toEntity(UserRequestDTO dto) {
        return new User(
                null,
                dto.getFirstName(),
                dto.getLastName(),
                dto.getDni(),
                dto.getRegistrationYear(),
                dto.isActive(),
                dto.getImageUrl()
        );
    }
}
