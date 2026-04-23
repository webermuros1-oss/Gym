package inditex.P1.Gym.service;

import inditex.P1.Gym.DTO.Enrollment.EnrollmentMapper;
import inditex.P1.Gym.DTO.Enrollment.EnrollmentRequestDTO;
import inditex.P1.Gym.DTO.Enrollment.EnrollmentResponseDTO;
import inditex.P1.Gym.exception.ObjectNotFoundException;
import inditex.P1.Gym.model.AttendanceStatus;
import inditex.P1.Gym.model.Enrollment;
import inditex.P1.Gym.repository.ActivityRepository;
import inditex.P1.Gym.repository.EnrollmentRepository;
import inditex.P1.Gym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public List<EnrollmentResponseDTO> getAll() {
        return enrollmentRepository.findAll().stream()
                .map(EnrollmentMapper::entity2DTO)
                .toList();
    }

    public EnrollmentResponseDTO getOne(Long activityId, Long userId) {
        return EnrollmentMapper.entity2DTO(findOrThrow(activityId, userId));
    }

    public List<EnrollmentResponseDTO> getByActivity(Long activityId) {
        if (!activityRepository.existsById(activityId)) {
            throw new ObjectNotFoundException("Activity", activityId);
        }
        return enrollmentRepository.findAll().stream()
                .filter(e -> e.getActivity() != null && activityId.equals(e.getActivity().getId()))
                .map(EnrollmentMapper::entity2DTO)
                .toList();
    }

    public List<EnrollmentResponseDTO> getByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User", userId);
        }
        return enrollmentRepository.findByUserId(userId).stream()
                .map(EnrollmentMapper::entity2DTO)
                .toList();
    }

    public EnrollmentResponseDTO update(Long activityId, Long userId, EnrollmentRequestDTO dto) {
        Enrollment enrollment = findOrThrow(activityId, userId);
        EnrollmentMapper.updateEntityFromDto(enrollment, dto);
        return EnrollmentMapper.entity2DTO(enrollmentRepository.save(enrollment));
    }

    public EnrollmentResponseDTO cancel(Long activityId, Long userId) {
        Enrollment enrollment = findOrThrow(activityId, userId);
        enrollment.setAttendanceStatus(AttendanceStatus.CANCELLED);
        enrollment.setCancelledAt(LocalDateTime.now());
        return EnrollmentMapper.entity2DTO(enrollmentRepository.save(enrollment));
    }

    private Enrollment findOrThrow(Long activityId, Long userId) {
        return enrollmentRepository.findByActivityIdAndUserId(activityId, userId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Enrollment (activityId=" + activityId + ")", userId));
    }
}