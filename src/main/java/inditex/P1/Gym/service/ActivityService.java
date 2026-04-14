package inditex.P1.Gym.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import inditex.P1.Gym.dto.ActivityDTO;
import inditex.P1.Gym.entity.Activity;
import inditex.P1.Gym.entity.Teacher;
import inditex.P1.Gym.entity.User;
import inditex.P1.Gym.mapper.ActivityMapper;
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

    public ActivityDTO create(ActivityDTO dto) {
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
        return ActivityMapper.toDTO(savedActivity);
    }

    public ActivityDTO update(Long id, ActivityDTO dto) {
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
        return ActivityMapper.toDTO(updatedActivity);
    }

    public void delete(Long id) {
        if (!activityRepository.existsById(id)) {
            throw new RuntimeException("Activity not found");
        }

        activityRepository.deleteById(id);
    }

    public ActivityDTO registerUser(Long activityId, Long userId) {
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
        return ActivityMapper.toDTO(savedActivity);
    }

    public List<ActivityDTO> getFutureActivities() {
        return activityRepository.findAll().stream()
                .filter(activity -> activity.getDate() != null)
                .filter(activity -> activity.getDate().isAfter(LocalDateTime.now()))
                .map(ActivityMapper::toDTO)
                .toList();
    }
}
