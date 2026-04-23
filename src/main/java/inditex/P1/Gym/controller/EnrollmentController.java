package inditex.P1.Gym.controller;

import inditex.P1.Gym.DTO.Enrollment.EnrollmentRequestDTO;
import inditex.P1.Gym.DTO.Enrollment.EnrollmentResponseDTO;
import inditex.P1.Gym.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDTO>> getAll() {
        return ResponseEntity.ok(enrollmentService.getAll());
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<EnrollmentResponseDTO>> getByActivity(@PathVariable Long activityId) {
        return ResponseEntity.ok(enrollmentService.getByActivity(activityId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EnrollmentResponseDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(enrollmentService.getByUser(userId));
    }

    @GetMapping("/{activityId}/{userId}")
    public ResponseEntity<EnrollmentResponseDTO> getOne(@PathVariable Long activityId,
                                                        @PathVariable Long userId) {
        return ResponseEntity.ok(enrollmentService.getOne(activityId, userId));
    }

    @PutMapping("/{activityId}/{userId}")
    public ResponseEntity<EnrollmentResponseDTO> update(@PathVariable Long activityId,
                                                        @PathVariable Long userId,
                                                        @Valid @RequestBody EnrollmentRequestDTO dto) {
        return ResponseEntity.ok(enrollmentService.update(activityId, userId, dto));
    }

    @PatchMapping("/{activityId}/{userId}/cancel")
    public ResponseEntity<EnrollmentResponseDTO> cancel(@PathVariable Long activityId,
                                                        @PathVariable Long userId) {
        return ResponseEntity.ok(enrollmentService.cancel(activityId, userId));
    }
}