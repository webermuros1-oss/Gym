package inditex.P1.Gym.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import inditex.P1.Gym.DTO.ActivityDTO;
import inditex.P1.Gym.service.ActivityService;

@RestController
@RequestMapping("/users")
public class UserActivityController {

    private final ActivityService activityService;

    public UserActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/{userId}/activities")
    public List<ActivityDTO> getActivitiesByUser(@PathVariable Long userId) {
        return activityService.getActivitiesByUser(userId);
    }
}
