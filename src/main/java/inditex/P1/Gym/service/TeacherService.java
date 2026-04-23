package inditex.P1.Gym.service;

import inditex.P1.Gym.DTO.Teacher.TeacherRequestDTO;
import inditex.P1.Gym.DTO.Teacher.TeacherResponseDTO;
import inditex.P1.Gym.exception.ObjectNotFoundException;
import inditex.P1.Gym.model.Teacher;
import inditex.P1.Gym.repository.TeacherRepository;
import inditex.P1.Gym.DTO.Teacher.TeacherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final CloudinaryService cloudinaryService;

    public List<TeacherResponseDTO> getAllTeachers() {
        return teacherRepository.findAll()
                .stream()
                .map(TeacherMapper::entity2DTO)
                .collect(Collectors.toList());
    }

    public TeacherResponseDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Teacher", id));
        return TeacherMapper.entity2DTO(teacher);
    }

    public List<TeacherResponseDTO> getActiveTeachers() {
        return teacherRepository.findByActiveTrue()
                .stream()
                .map(TeacherMapper::entity2DTO)
                .collect(Collectors.toList());
    }

    public TeacherResponseDTO createTeacher(TeacherRequestDTO dto, MultipartFile image) {
        if (teacherRepository.findByDni(dto.getDni()).isPresent()) {
            throw new ObjectNotFoundException("Teacher", dto.getDni());
        }
        if (image != null && !image.isEmpty()) {
            dto.setImageUrl(cloudinaryService.uploadImage(image));
        }
        return TeacherMapper.entity2DTO(teacherRepository.save(TeacherMapper.dto2Entity(dto)));
    }

    public TeacherResponseDTO updateTeacher(Long id, TeacherRequestDTO dto, MultipartFile image) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Teacher", id));
        if (image != null && !image.isEmpty()) {
            dto.setImageUrl(cloudinaryService.uploadImage(image));
        }
        TeacherMapper.updateEntityFromDto(teacher, dto);
        return TeacherMapper.entity2DTO(teacherRepository.save(teacher));
    }

    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new ObjectNotFoundException("Teacher", id);
        }
        teacherRepository.deleteById(id);
    }

}