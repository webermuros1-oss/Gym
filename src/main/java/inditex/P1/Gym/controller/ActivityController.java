package inditex.P1.Gym.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
