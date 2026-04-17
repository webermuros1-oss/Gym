package inditex.P1.Gym.service;
import inditex.P1.Gym.DTO.TeacherRequestDTO;
import inditex.P1.Gym.DTO.TeacherResponseDTO;
import inditex.P1.Gym.exception.ObjectNotFoundException;
import inditex.P1.Gym.model.Teacher;
import inditex.P1.Gym.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombook.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public List<TeacherResponseDTO>getAllTeacher(){
        return teacherRepository.findAll()
                .stream()
                .map(this::ResponseDTO)
                .collect(Collectors.toList())
    }
    public  TeacherResponseDTO getTeacherById(Long id){
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow() -> new org.hibernate.ObjectNotFoundException("Teaccher", id));
return toResponseDTO(Teacher);
    }
public List <TeacherResponseDTO> getAtiveTeacher(){
    return teacherRepository.findAllByActiveTrue()
            .stream()
            .map(this::toRaponseDTO
            .collect(Collectors.toList())
}

public TeacherResponseDTO createTeacher(TeacherRequestDTO teacherRequestDTO dto) {
        if (TeacherRepository.findByDni(dto.getDni()).isPresent()) }
    throw new org.hibernate.ObjectNotFoundException("Teacher", DTO.getDni();
}
    return toResponseDTO(teacherRepository.save(toEntity(dto)));
}
public TeacherResponseDTO updateTeacher(Long id, TeacherRequestDTO dto) {
    Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Teacher", id));
    teacher.setFirstName(dto.getFirstName());
    teacher.setLastName(dto.getLastName());
    teacher.setDni(dto.getDni());
    teacher.setContractYear(dto.getContractYear());
    teacher.setActive(dto.isActive());
    teacher.setImageUrl(dto.getImageUrl());
    return toResponseDTO(teacherRepository.save(teacher));
}

public void deleteTeacher(Long id) {
    if (!teacherRepository.existsById(id)) {
        throw new ObjectNotFoundException("Teacher", id);
    }
    teacherRepository.deleteById(id);
}

// --- Mapeos ---

public TeacherResponseDTO toResponseDTO(Teacher teacher) {
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

private Teacher toEntity(TeacherRequestDTO dto) {
    return new Teacher(
            null,
            dto.getFirstName(),
            dto.getLastName(),
            dto.getDni(),
            dto.getContractYear(),
            dto.isActive(),
            dto.getImageUrl()
    );
}
}