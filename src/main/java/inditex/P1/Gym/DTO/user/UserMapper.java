package inditex.P1.Gym.DTO.user;

import inditex.P1.Gym.model.User;

public class UserMapper {

    public static User dto2Entity(UserRequestDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDni(dto.getDni());
        user.setRegistrationYear(dto.getRegistrationYear());
        user.setActive(dto.isActive());
        user.setImageUrl(dto.getImageUrl());
        return user;
    }

    public static UserResponseDTO entity2DTO(User user) {
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

    public static void updateEntityFromDto(User user, UserRequestDTO dto) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDni(dto.getDni());
        user.setRegistrationYear(dto.getRegistrationYear());
        user.setActive(dto.isActive());
        user.setImageUrl(dto.getImageUrl());
    }
}
