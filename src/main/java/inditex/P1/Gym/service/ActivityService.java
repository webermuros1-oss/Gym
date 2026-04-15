package inditex.P1.Gym.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import inditex.P1.Gym.DTO.ActivityRequestDTO;
import inditex.P1.Gym.DTO.ActivityResponseDTO;
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

    public ActivityService(ActivityRepository activityRepository, TeacherRepository teacherRepository,
            UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }

    public ActivityResponseDTO create(ActivityRequestDTO dto) {
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Activity activity = new Activity();
        activity.setTitle(dto.getTitle());
        activity.setDescription(dto.getDescription());
        activity.setPrice(dto.getPrice());
        activity.setDate(dto.getDate());
        activity.setImageUrl(dto.getImageUrl());
        activity.setTeacher(teacher);

        Activity savedActivity = activityRepository.save(activity);
        return toResponseDTO(savedActivity);
    }

    public ActivityResponseDTO update(Long id, ActivityRequestDTO dto) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        activity.setTitle(dto.getTitle());
        activity.setDescription(dto.getDescription());
        activity.setPrice(dto.getPrice());
        activity.setDate(dto.getDate());
        activity.setImageUrl(dto.getImageUrl());
        activity.setTeacher(teacher);

        Activity updatedActivity = activityRepository.save(activity);
        return toResponseDTO(updatedActivity);
    }

    public void delete(Long id) {
        if (!activityRepository.existsById(id)) {
            throw new RuntimeException("Activity not found");
        }

        activityRepository.deleteById(id);
    }

    public ActivityResponseDTO registerUser(Long activityId, Long userId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new RuntimeException("User is not active");
        }

        if (activity.getUsers().contains(user)) {
            throw new RuntimeException("User already registered");
        }

        long futureActivitiesCount = user.getActivities().stream()
                .filter(userActivity -> userActivity.getDate() != null)
                .filter(userActivity -> userActivity.getDate().isAfter(LocalDateTime.now()))
                .count();

        if (futureActivitiesCount >= 3) {
            throw new RuntimeException("User cannot register to more than 3 future activities");
        }

        activity.getUsers().add(user);

        Activity savedActivity = activityRepository.save(activity);
        return toResponseDTO(savedActivity);
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
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getActivities().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<ActivityResponseDTO> getActivitiesByTeacher(Long teacherId) {
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
                teacherId
        );
    }
}
