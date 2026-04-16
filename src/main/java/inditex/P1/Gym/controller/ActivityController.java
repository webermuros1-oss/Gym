package inditex.P1.Gym.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import inditex.P1.Gym.DTO.ActivityDetailResponseDTO;
import inditex.P1.Gym.DTO.ActivityRequestDTO;
import inditex.P1.Gym.DTO.ActivityResponseDTO;
import inditex.P1.Gym.service.ActivityService;
import inditex.P1.Gym.service.CloudinaryService;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ActivityController {

    private final ActivityService activityService;
    private final CloudinaryService cloudinaryService;

    @GetMapping("/future")
    public ResponseEntity<List<ActivityResponseDTO>> getFutureActivities() {
        return ResponseEntity.ok(activityService.getFutureActivities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDetailResponseDTO> getActivityDetail(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getActivityDetail(id));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<ActivityResponseDTO>> getActivitiesByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(activityService.getActivitiesByTeacher(teacherId));
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ActivityResponseDTO> create(@Valid @ModelAttribute ActivityRequestDTO dto,
                                                      @RequestPart(required = false) MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            dto.setImageUrl(cloudinaryService.uploadImage(image));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.create(dto));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ActivityResponseDTO> update(@PathVariable Long id,
                                                      @Valid @ModelAttribute ActivityRequestDTO dto,
                                                      @RequestPart(required = false) MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            dto.setImageUrl(cloudinaryService.uploadImage(image));
        }
        return ResponseEntity.ok(activityService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{activityId}/users/{userId}")
    public ResponseEntity<ActivityResponseDTO> registerUser(@PathVariable Long activityId,
                                                            @PathVariable Long userId) {
        return ResponseEntity.ok(activityService.registerUser(activityId, userId));
    }
}