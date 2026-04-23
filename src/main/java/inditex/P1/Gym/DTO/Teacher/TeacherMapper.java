package inditex.P1.Gym.DTO.Teacher;

import inditex.P1.Gym.model.Teacher;

public class TeacherMapper {

    public static Teacher dto2Entity(TeacherRequestDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setDni(dto.getDni());
        teacher.setContractYear(dto.getContractYear());
        teacher.setActive(dto.isActive());
        teacher.setImageUrl(dto.getImageUrl());
        return teacher;
    }

    public static TeacherResponseDTO entity2DTO(Teacher teacher) {
        return new TeacherResponseDTO(
                teacher.getId(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getDni(),
                teacher.getContractYear(),
                teacher.isActive(),
                teacher.getImageUrl()
        );
    }

    public static void updateEntityFromDto(Teacher teacher, TeacherRequestDTO dto) {
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setDni(dto.getDni());
        teacher.setContractYear(dto.getContractYear());
        teacher.setActive(dto.isActive());
        teacher.setImageUrl(dto.getImageUrl());
    }
}
