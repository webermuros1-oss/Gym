package inditex.P1.Gym.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import inditex.P1.Gym.dto.ActivityDTO;
import inditex.P1.Gym.service.ActivityService;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public ActivityDTO create(@RequestBody ActivityDTO dto) {
        return activityService.create(dto);
    }

    @PostMapping("/{activityId}/users/{userId}")
    public ActivityDTO registerUser(@PathVariable Long activityId, @PathVariable Long userId) {
        return activityService.registerUser(activityId, userId);
    }

    @GetMapping("/future")
    public List<ActivityDTO> getFutureActivities() {
        return activityService.getFutureActivities();
    }

    @PutMapping("/{id}")
    public ActivityDTO update(@PathVariable Long id, @RequestBody ActivityDTO dto) {
        return activityService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        activityService.delete(id);
    }
}
