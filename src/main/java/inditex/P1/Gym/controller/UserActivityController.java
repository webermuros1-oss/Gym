package inditex.P1.Gym.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import inditex.P1.Gym.DTO.Activity.ActivityResponseDTO;
import inditex.P1.Gym.service.ActivityService;


@RestController
@RequestMapping("/api/users")
public class UserActivityController {

    private final ActivityService activityService;

    public UserActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/{userId}/activities")
    public ResponseEntity<List<ActivityResponseDTO>> getActivitiesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(activityService.getActivitiesByUser(userId));
    }
}
