package inditex.P1.Gym.service;

import inditex.P1.Gym.DTO.user.UserRequestDTO;
import inditex.P1.Gym.DTO.user.UserMapper;
import inditex.P1.Gym.DTO.user.UserResponseDTO;
import inditex.P1.Gym.exception.ObjectNotFoundException;
import inditex.P1.Gym.model.User;
import inditex.P1.Gym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(UserMapper::entity2DTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User", id));
        return UserMapper.entity2DTO(user);
    }

    public List<UserResponseDTO> getActiveUsers() {
        return userRepository.findByActiveTrue()
                .stream()
                .map(UserMapper::entity2DTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO createUser(UserRequestDTO dto, MultipartFile image) {
        if (userRepository.findByDni(dto.getDni()).isPresent()) {
            throw new ObjectNotFoundException("User", dto.getDni());
        }
        if (image != null && !image.isEmpty()) {
            dto.setImageUrl(cloudinaryService.uploadImage(image));
        }
        User user = UserMapper.dto2Entity(dto);
        return UserMapper.entity2DTO(userRepository.save(user));
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto, MultipartFile image) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("User", id));
        if (image != null && !image.isEmpty()) {
            dto.setImageUrl(cloudinaryService.uploadImage(image));
        }
        UserMapper.updateEntityFromDto(user, dto);
        return UserMapper.entity2DTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ObjectNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }
}
