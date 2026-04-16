package inditex.P1.Gym.controller;

import inditex.P1.Gym.DTO.TeacherRequestDTO;
import inditex.P1.Gym.DTO.TeacherResponseDTO;
import inditex.P1.Gym.service.CloudinaryService;
import inditex.P1.Gym.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TeacherController {

    private final TeacherService teacherService;
    private final CloudinaryService cloudinaryService;

    @GetMapping
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<TeacherResponseDTO>> getActiveTeachers() {
        return ResponseEntity.ok(teacherService.getActiveTeachers());
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<TeacherResponseDTO> createTeacher(@Valid @ModelAttribute TeacherRequestDTO dto,
                                                            @RequestPart(required = false) MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            dto.setImageUrl(cloudinaryService.uploadImage(image));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.createTeacher(dto));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<TeacherResponseDTO> updateTeacher(@PathVariable Long id,
                                                            @Valid @ModelAttribute TeacherRequestDTO dto,
                                                            @RequestPart(required = false) MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            dto.setImageUrl(cloudinaryService.uploadImage(image));
        }
        return ResponseEntity.ok(teacherService.updateTeacher(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}