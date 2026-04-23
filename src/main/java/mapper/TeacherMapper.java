package mapper;

import inditex.P1.Gym.DTO.TeacherRequestDTO;
import inditex.P1.Gym.DTO.TeacherResponseDTO;
import inditex.P1.Gym.model.Teacher;

public final class TeacherMapper {

    private TeacherMapper() {
    }

    public static TeacherResponseDTO toDTO(Teacher teacher) {

        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto.setId(teacher.getId());
        dto.setFirstName(teacher.getFirstName());
        dto.setLastName(teacher.getLastName());
        dto.setDni(teacher.getDni());
        dto.setContractYear(teacher.getContractYear());
        dto.setActive(teacher.isActive());
        dto.setImageUrl(teacher.getImageUrl());
        return dto;
    }

    public static Teacher toEntity(TeacherRequestDTO dto) {

        Teacher teacher = new Teacher();
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setDni(dto.getDni());
        teacher.setContractYear(dto.getContractYear());
        teacher.setActive(dto.isActive());
        teacher.setImageUrl(dto.getImageUrl());
        return teacher;
    }

    public static void updateEntity(Teacher teacher, TeacherRequestDTO dto) {
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setDni(dto.getDni());
        teacher.setContractYear(dto.getContractYear());
        teacher.setActive(dto.isActive());
        teacher.setImageUrl(dto.getImageUrl());
    }
}
