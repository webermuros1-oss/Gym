package inditex.P1.Gym.service;

import inditex.P1.Gym.DTO.UserRequestDTO;
import inditex.P1.Gym.DTO.UserResponseDTO;
import inditex.P1.Gym.model.Activity;
import inditex.P1.Gym.model.User;
import inditex.P1.Gym.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_shouldSaveCorrectly() {
        // Given
        UserRequestDTO dto = new UserRequestDTO("Ana", "Lopez", "12345678A", 2024, true, "img");
        when(userRepository.findByDni("12345678A")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        // When
        UserResponseDTO result = userService.createUser(dto, (MultipartFile) null);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ana", result.getFirstName());
        assertEquals("Lopez", result.getLastName());
        assertEquals("12345678A", result.getDni());
        assertEquals(2024, result.getRegistrationYear());
        assertTrue(result.isActive());
        assertEquals("img", result.getImageUrl());
    }

    @Test
    void updateUser_shouldUpdateWithoutLosingData() {
        // Given
        User existing = new User();
        existing.setId(10L);
        existing.setFirstName("Old");
        existing.setLastName("User");
        existing.setDni("11111111A");
        existing.setRegistrationYear(2020);
        existing.setActive(true);
        existing.setImageUrl("old-img");
        existing.getActivities().add(new Activity());

        UserRequestDTO dto = new UserRequestDTO("New", "Name", "22222222B", 2025, false, "new-img");

        when(userRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        UserResponseDTO result = userService.updateUser(10L, dto, null);

        // Then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertSame(existing, captor.getValue());
        assertEquals(1, existing.getActivities().size());
        assertEquals("New", existing.getFirstName());
        assertEquals("Name", existing.getLastName());
        assertEquals("22222222B", existing.getDni());
        assertEquals(2025, existing.getRegistrationYear());
        assertFalse(existing.isActive());
        assertEquals("new-img", existing.getImageUrl());
        assertEquals(10L, result.getId());
    }

    @Test
    void getAllUsers_shouldReturnList() {
        // Given
        User u1 = new User();
        u1.setId(1L);
        u1.setFirstName("A");
        u1.setLastName("AA");
        u1.setDni("A1");
        u1.setRegistrationYear(2022);
        u1.setActive(true);

        User u2 = new User();
        u2.setId(2L);
        u2.setFirstName("B");
        u2.setLastName("BB");
        u2.setDni("B2");
        u2.setRegistrationYear(2023);
        u2.setActive(false);

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        // When
        List<UserResponseDTO> result = userService.getAllUsers();

        // Then
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getActiveUsers_shouldFilterCorrectly() {
        // Given
        User active = new User();
        active.setId(5L);
        active.setFirstName("Active");
        active.setLastName("User");
        active.setDni("99999999Z");
        active.setRegistrationYear(2024);
        active.setActive(true);

        when(userRepository.findByActiveTrue()).thenReturn(List.of(active));

        // When
        List<UserResponseDTO> result = userService.getActiveUsers();

        // Then
        assertEquals(1, result.size());
        assertEquals(5L, result.get(0).getId());
        assertTrue(result.get(0).isActive());
    }
}
