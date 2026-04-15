package inditex.P1.Gym.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import inditex.P1.Gym.DTO.ActivityDTO;
import inditex.P1.Gym.service.ActivityService;

@RestController
@RequestMapping("/teachers")
public class TeacherActivityController {

    private final ActivityService activityService;

    public TeacherActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/{teacherId}/activities")
    public List<ActivityDTO> getActivitiesByTeacher(@PathVariable Long teacherId) {
        return activityService.getActivitiesByTeacher(teacherId);
    }
}
