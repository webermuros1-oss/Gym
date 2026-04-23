package inditex.P1.Gym.service;

import inditex.P1.Gym.DTO.ActivityDetailResponseDTO;
import inditex.P1.Gym.DTO.ActivityRequestDTO;
import inditex.P1.Gym.DTO.ActivityResponseDTO;
import inditex.P1.Gym.model.Activity;
import inditex.P1.Gym.model.Teacher;
import inditex.P1.Gym.model.User;
import inditex.P1.Gym.repository.ActivityRepository;
import inditex.P1.Gym.repository.TeacherRepository;
import inditex.P1.Gym.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private ActivityService activityService;

    @Test
    void createActivity_shouldAssignTeacherCorrectly() {
        // Given
        ActivityRequestDTO dto = new ActivityRequestDTO(
                "Yoga",
                "Clase",
                new BigDecimal("15.00"),
                LocalDateTime.now().plusDays(2),
                "img",
                7L
        );
        Teacher teacher = new Teacher();
        teacher.setId(7L);
        teacher.setActive(true);

        when(teacherRepository.findById(7L)).thenReturn(Optional.of(teacher));
        when(activityRepository.save(any(Activity.class))).thenAnswer(invocation -> {
            Activity saved = invocation.getArgument(0);
            saved.setId(100L);
            return saved;
        });

        // When
        ActivityResponseDTO result = activityService.create(dto, (MultipartFile) null);

        // Then
        ArgumentCaptor<Activity> captor = ArgumentCaptor.forClass(Activity.class);
        verify(activityRepository).save(captor.capture());
        Activity saved = captor.getValue();
        assertSame(teacher, saved.getTeacher());
        assertEquals("Yoga", saved.getTitle());
        assertEquals(7L, result.getTeacherId());
        assertEquals(0, result.getEnrolledCount());
    }

    @Test
    void updateActivity_shouldKeepSameEntityAndUpdateFields() {
        // Given
        Activity existing = new Activity();
        existing.setId(1L);
        existing.setTitle("Old");
        existing.setDescription("Old desc");
        existing.setPrice(new BigDecimal("10.00"));
        existing.setDate(LocalDateTime.now().plusDays(1));
        existing.setImageUrl("old-img");
        User enrolled = new User();
        enrolled.setId(50L);
        existing.getUsers().add(enrolled);

        Teacher newTeacher = new Teacher();
        newTeacher.setId(2L);
        newTeacher.setActive(true);

        ActivityRequestDTO dto = new ActivityRequestDTO(
                "New",
                "New desc",
                new BigDecimal("20.00"),
                LocalDateTime.now().plusDays(5),
                "new-img",
                2L
        );

        when(activityRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(teacherRepository.findById(2L)).thenReturn(Optional.of(newTeacher));
        when(activityRepository.save(any(Activity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ActivityResponseDTO result = activityService.update(1L, dto, null);

        // Then
        ArgumentCaptor<Activity> captor = ArgumentCaptor.forClass(Activity.class);
        verify(activityRepository).save(captor.capture());
        assertSame(existing, captor.getValue());
        assertEquals(1L, existing.getId());
        assertEquals("New", existing.getTitle());
        assertEquals("New desc", existing.getDescription());
        assertEquals(new BigDecimal("20.00"), existing.getPrice());
        assertEquals("new-img", existing.getImageUrl());
        assertSame(newTeacher, existing.getTeacher());
        assertEquals(1, existing.getUsers().size());
        assertEquals(2L, result.getTeacherId());
        assertEquals(1, result.getEnrolledCount());
    }

    @Test
    void getFutureActivities_shouldReturnList() {
        // Given
        Activity future = new Activity();
        future.setId(1L);
        future.setTitle("Future");
        future.setDate(LocalDateTime.now().plusDays(1));

        Activity past = new Activity();
        past.setId(2L);
        past.setTitle("Past");
        past.setDate(LocalDateTime.now().minusDays(1));

        Activity noDate = new Activity();
        noDate.setId(3L);
        noDate.setTitle("NoDate");
        noDate.setDate(null);

        when(activityRepository.findAll()).thenReturn(List.of(future, past, noDate));

        // When
        List<ActivityResponseDTO> result = activityService.getFutureActivities();

        // Then
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Future", result.get(0).getTitle());
    }

    @Test
    void registerUser_shouldAddUser() {
        // Given
        Activity activity = new Activity();
        activity.setId(10L);

        User user = new User();
        user.setId(20L);
        user.setActive(true);

        when(activityRepository.findById(10L)).thenReturn(Optional.of(activity));
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));
        when(activityRepository.save(any(Activity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ActivityResponseDTO result = activityService.registerUser(10L, 20L);

        // Then
        assertTrue(activity.getUsers().contains(user));
        assertEquals(1, activity.getUsers().size());
        assertEquals(1, result.getEnrolledCount());
    }

    @Test
    void unregisterUser_shouldRemoveUser() {
        // Given
        Activity activity = new Activity();
        activity.setId(10L);

        User user = new User();
        user.setId(20L);
        activity.getUsers().add(user);

        when(activityRepository.findById(10L)).thenReturn(Optional.of(activity));
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));
        when(activityRepository.save(any(Activity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ActivityResponseDTO result = activityService.unregisterUser(10L, 20L);

        // Then
        assertFalse(activity.getUsers().contains(user));
        assertEquals(0, activity.getUsers().size());
        assertEquals(0, result.getEnrolledCount());
    }

    @Test
    void getActivityDetail_shouldReturnTeacherUsersAndEnrolledCount() {
        // Given
        Teacher teacher = new Teacher();
        teacher.setId(3L);
        teacher.setFirstName("Laura");
        teacher.setLastName("Diaz");
        teacher.setDni("T-33");
        teacher.setContractYear(2020);
        teacher.setActive(true);

        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Ana");
        user1.setLastName("A");
        user1.setDni("U1");
        user1.setRegistrationYear(2024);
        user1.setActive(true);

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Juan");
        user2.setLastName("B");
        user2.setDni("U2");
        user2.setRegistrationYear(2023);
        user2.setActive(true);

        Activity activity = new Activity();
        activity.setId(99L);
        activity.setTitle("Pilates");
        activity.setDescription("Detail");
        activity.setPrice(new BigDecimal("12.50"));
        activity.setDate(LocalDateTime.now().plusDays(3));
        activity.setImageUrl("img");
        activity.setTeacher(teacher);
        activity.getUsers().add(user1);
        activity.getUsers().add(user2);

        when(activityRepository.findById(99L)).thenReturn(Optional.of(activity));

        // When
        ActivityDetailResponseDTO result = activityService.getActivityDetail(99L);

        // Then
        assertNotNull(result);
        assertNotNull(result.getTeacher());
        assertEquals(3L, result.getTeacher().getId());
        assertEquals(2, result.getUsers().size());
        assertEquals(2, result.getEnrolledCount());
        Set<Long> returnedUserIds = result.getUsers().stream()
                .map(u -> u.getId())
                .collect(Collectors.toSet());
        assertEquals(Set.of(1L, 2L), returnedUserIds);
    }
}
