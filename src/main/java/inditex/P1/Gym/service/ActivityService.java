package inditex.P1.Gym.service;

import org.springframework.stereotype.Service;

import inditex.P1.Gym.dto.ActivityDTO;
import inditex.P1.Gym.entity.Activity;
import inditex.P1.Gym.entity.Teacher;
import inditex.P1.Gym.mapper.ActivityMapper;
import inditex.P1.Gym.repository.ActivityRepository;
import inditex.P1.Gym.repository.TeacherRepository;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final TeacherRepository teacherRepository;

    public ActivityService(ActivityRepository activityRepository, TeacherRepository teacherRepository) {
        this.activityRepository = activityRepository;
        this.teacherRepository = teacherRepository;
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
}
