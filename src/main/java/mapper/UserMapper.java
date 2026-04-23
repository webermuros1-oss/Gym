package mapper;

import inditex.P1.Gym.DTO.UserRequestDTO;
import inditex.P1.Gym.DTO.UserResponseDTO;
import inditex.P1.Gym.model.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDni(user.getDni());
        dto.setRegistrationYear(user.getRegistrationYear());
        dto.setActive(user.isActive());
        dto.setImageUrl(user.getImageUrl());
        return dto;
    }

    public static User toEntity(UserRequestDTO dto) {

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDni(dto.getDni());
        user.setRegistrationYear(dto.getRegistrationYear());
        user.setActive(dto.isActive());
        user.setImageUrl(dto.getImageUrl());
        return user;
    }

    public static void updateEntity(User user, UserRequestDTO dto) {

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDni(dto.getDni());
        user.setRegistrationYear(dto.getRegistrationYear());
        user.setActive(dto.isActive());
        user.setImageUrl(dto.getImageUrl());
    }
}
