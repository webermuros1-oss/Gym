package inditex.P1.Gym.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import inditex.P1.Gym.DTO.ActivityDetailResponseDTO;
import inditex.P1.Gym.DTO.ActivityRequestDTO;
import inditex.P1.Gym.DTO.ActivityResponseDTO;
import inditex.P1.Gym.DTO.TeacherResponseDTO;
import inditex.P1.Gym.DTO.UserResponseDTO;
import inditex.P1.Gym.exception.ObjectNotFoundException;
import inditex.P1.Gym.model.Activity;
import inditex.P1.Gym.model.Teacher;
import inditex.P1.Gym.model.User;
import inditex.P1.Gym.repository.ActivityRepository;
import inditex.P1.Gym.repository.TeacherRepository;
import inditex.P1.Gym.repository.UserRepository;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final TeacherService teacherService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    public ActivityService(ActivityRepository activityRepository, TeacherRepository teacherRepository,
            UserRepository userRepository, TeacherService teacherService, UserService userService,
            CloudinaryService cloudinaryService) {
        this.activityRepository = activityRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.teacherService = teacherService;
        this.userService = userService;
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

        Activity activity = new Activity();
        activity.setTitle(dto.getTitle());
        activity.setDescription(dto.getDescription());
        activity.setPrice(dto.getPrice());
        activity.setDate(dto.getDate());
        activity.setImageUrl(dto.getImageUrl());
        activity.setTeacher(teacher);

        return toResponseDTO(activityRepository.save(activity));
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

        activity.setTitle(dto.getTitle());
        activity.setDescription(dto.getDescription());
        activity.setPrice(dto.getPrice());
        activity.setDate(dto.getDate());
        activity.setImageUrl(dto.getImageUrl());
        activity.setTeacher(teacher);

        return toResponseDTO(activityRepository.save(activity));
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

        TeacherResponseDTO teacherDTO = activity.getTeacher() != null
                ? teacherService.toResponseDTO(activity.getTeacher())
                : null;

        List<UserResponseDTO> userDTOs = activity.getUsers().stream()
                .map(userService::toResponseDTO)
                .collect(Collectors.toList());

        return new ActivityDetailResponseDTO(
                activity.getId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getPrice(),
                activity.getDate(),
                activity.getImageUrl(),
                teacherDTO,
                userDTOs,
                userDTOs.size()
        );
    }

    public ActivityResponseDTO unregisterUser(Long activityId, Long userId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ObjectNotFoundException("Activity", activityId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User", userId));

        if (!activity.getUsers().contains(user)) {
            throw new IllegalArgumentException("El usuario con id: " + userId + " no está inscrito en esta actividad");
        }

        activity.getUsers().remove(user);
        return toResponseDTO(activityRepository.save(activity));
    }

    public ActivityResponseDTO registerUser(Long activityId, Long userId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ObjectNotFoundException("Activity", activityId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User", userId));

        if (!user.isActive()) {
            throw new ObjectNotFoundException("User", userId, false);
        }

        if (activity.getUsers().contains(user)) {
            throw new IllegalArgumentException("El usuario con id: " + userId + " ya está inscrito en esta actividad");
        }

        long futureActivitiesCount = user.getActivities().stream()
                .filter(userActivity -> userActivity.getDate() != null)
                .filter(userActivity -> userActivity.getDate().isAfter(LocalDateTime.now()))
                .count();

        if (futureActivitiesCount >= 3) {
            throw new IllegalArgumentException("El usuario con id: " + userId + " no puede inscribirse en más de 3 actividades futuras");
        }

        activity.getUsers().add(user);
        return toResponseDTO(activityRepository.save(activity));
    }

    public List<ActivityResponseDTO> getFutureActivities() {
        return activityRepository.findAll().stream()
                .filter(activity -> activity.getDate() != null)
                .filter(activity -> activity.getDate().isAfter(LocalDateTime.now()))
                .map(this::toResponseDTO)
                .toList();
    }

    public List<ActivityResponseDTO> getActivitiesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User", userId));

        return user.getActivities().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<ActivityResponseDTO> getActivitiesByTeacher(Long teacherId) {
        if (!teacherRepository.existsById(teacherId)) {
            throw new ObjectNotFoundException("Teacher", teacherId);
        }
        return activityRepository.findAll().stream()
                .filter(activity -> activity.getTeacher() != null)
                .filter(activity -> activity.getTeacher().getId().equals(teacherId))
                .map(this::toResponseDTO)
                .toList();
    }

    private ActivityResponseDTO toResponseDTO(Activity activity) {
        Long teacherId = activity.getTeacher() != null ? activity.getTeacher().getId() : null;
        return new ActivityResponseDTO(
                activity.getId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getPrice(),
                activity.getDate(),
                activity.getImageUrl(),
                teacherId,
                activity.getUsers().size()
        );
    }
}