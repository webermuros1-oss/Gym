package inditex.P1.Gym.controller;


import inditex.P1.Gym.DTO.UserRequestDTO;
import inditex.P1.Gym.DTO.UserResponseDTO;
import inditex.P1.Gym.service.CloudinaryService;
import inditex.P1.Gym.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins= "*")
public class UserController {

    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserResponseDTO>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @ModelAttribute UserRequestDTO dto,
                                                      @RequestPart(required = false) MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            dto.setImageUrl(cloudinaryService.uploadImage(image));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                      @Valid @ModelAttribute UserRequestDTO dto,
                                                      @RequestPart(required = false) MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            dto.setImageUrl(cloudinaryService.uploadImage(image));
        }
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
