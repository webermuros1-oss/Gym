package inditex.P1.Gym.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import inditex.P1.Gym.DTO.Activity.ActivityDetailResponseDTO;
import inditex.P1.Gym.DTO.Activity.ActivityRequestDTO;
import inditex.P1.Gym.DTO.Activity.ActivityResponseDTO;
import inditex.P1.Gym.DTO.Activity.ActivityMapper;
import inditex.P1.Gym.exception.ObjectNotFoundException;
import inditex.P1.Gym.model.Activity;
import inditex.P1.Gym.model.Enrollment;
import inditex.P1.Gym.model.Teacher;
import inditex.P1.Gym.model.User;
import inditex.P1.Gym.repository.ActivityRepository;
import inditex.P1.Gym.repository.EnrollmentRepository;
import inditex.P1.Gym.repository.TeacherRepository;
import inditex.P1.Gym.repository.UserRepository;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CloudinaryService cloudinaryService;

    public ActivityService(ActivityRepository activityRepository, TeacherRepository teacherRepository,
                           UserRepository userRepository, EnrollmentRepository enrollmentRepository,
                           CloudinaryService cloudinaryService) {
        this.activityRepository = activityRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.cloudinaryService = cloudinaryService;
    }


    public ActivityResponseDTO create(ActivityRequestDTO dto, MultipartFile image) {
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ObjectNotFoundException("Teacher", dto.getTeacherId()));

        if (!teacher.isActive()) {
            throw new ObjectNotFoundException("Teacher", dto.getTeacherId(), false);
        }

        if (image != null && !image.isEmpty()) {
            dto.setImageUrl(cloudinaryService.uploadImage(image));
        }

        Activity activity = ActivityMapper.dto2Entity(dto, teacher);
        return ActivityMapper.entity2DTO(activityRepository.save(activity));
    }

    public ActivityResponseDTO update(Long id, ActivityRequestDTO dto, MultipartFile image) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Activity", id));

        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ObjectNotFoundException("Teacher", dto.getTeacherId()));

        if (!teacher.isActive()) {
            throw new ObjectNotFoundException("Teacher", dto.getTeacherId(), false);
        }

        if (image != null && !image.isEmpty()) {
            dto.setImageUrl(cloudinaryService.uploadImage(image));
        }

        ActivityMapper.updateEntityFromDto(activity, dto, teacher);
        return ActivityMapper.entity2DTO(activityRepository.save(activity));
    }

    public void delete(Long id) {
        if (!activityRepository.existsById(id)) {
            throw new ObjectNotFoundException("Activity", id);
        }
        activityRepository.deleteById(id);
    }

    public ActivityDetailResponseDTO getActivityDetail(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Activity", id));
        return ActivityMapper.entity2DetailDTO(activity);
    }

    public ActivityResponseDTO unregisterUser(Long activityId, Long userId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ObjectNotFoundException("Activity", activityId));

        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User", userId);
        }

        Enrollment enrollment = enrollmentRepository.findByActivityIdAndUserId(activityId, userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario con id: " + userId + " no está inscrito en esta actividad"));

        enrollmentRepository.delete(enrollment);
        activity.getEnrollments().remove(enrollment);
        return ActivityMapper.entity2DTO(activity);
    }

    public ActivityResponseDTO registerUser(Long activityId, Long userId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ObjectNotFoundException("Activity", activityId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User", userId));

        if (!user.isActive()) {
            throw new ObjectNotFoundException("User", userId, false);
        }

        if (enrollmentRepository.existsByActivityIdAndUserId(activityId, userId)) {
            throw new IllegalArgumentException("El usuario con id: " + userId + " ya está inscrito en esta actividad");
        }

        long futureActivitiesCount = enrollmentRepository
                .countByUserIdAndActivityDateAfter(userId, LocalDateTime.now());

        if (futureActivitiesCount >= 3) {
            throw new IllegalArgumentException("El usuario con id: " + userId + " no puede inscribirse en más de 3 actividades futuras");
        }

        Enrollment enrollment = new Enrollment(activity, user);
        enrollmentRepository.save(enrollment);
        activity.getEnrollments().add(enrollment);
        return ActivityMapper.entity2DTO(activity);
    }

    public List<ActivityResponseDTO> getFutureActivities() {
        return activityRepository.findAll().stream()
                .filter(activity -> activity.getDate() != null)
                .filter(activity -> activity.getDate().isAfter(LocalDateTime.now()))
                .map(ActivityMapper::entity2DTO)
                .toList();
    }

    public List<ActivityResponseDTO> getActivitiesByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User", userId);
        }

        return enrollmentRepository.findByUserId(userId).stream()
                .map(Enrollment::getActivity)
                .map(ActivityMapper::entity2DTO)
                .toList();
    }

    public List<ActivityResponseDTO> getActivitiesByTeacher(Long teacherId) {
        if (!teacherRepository.existsById(teacherId)) {
            throw new ObjectNotFoundException("Teacher", teacherId);
        }
        return activityRepository.findAll().stream()
                .filter(activity -> activity.getTeacher() != null)
                .filter(activity -> activity.getTeacher().getId().equals(teacherId))
                .map(ActivityMapper::entity2DTO)
                .toList();
    }

}