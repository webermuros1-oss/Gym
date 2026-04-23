package inditex.P1.Gym.service;

import inditex.P1.Gym.DTO.TeacherRequestDTO;
import inditex.P1.Gym.DTO.TeacherResponseDTO;
import inditex.P1.Gym.model.Teacher;
import inditex.P1.Gym.repository.TeacherRepository;
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
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void createTeacher_shouldSaveCorrectly() {
        // Given
        TeacherRequestDTO dto = new TeacherRequestDTO("Carlos", "Ruiz", "12345678T", 2021, true, "img");
        when(teacherRepository.findByDni("12345678T")).thenReturn(Optional.empty());
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> {
            Teacher saved = invocation.getArgument(0);
            saved.setId(11L);
            return saved;
        });

        // When
        TeacherResponseDTO result = teacherService.createTeacher(dto, (MultipartFile) null);

        // Then
        assertNotNull(result);
        assertEquals(11L, result.getId());
        assertEquals("Carlos", result.getFirstName());
        assertEquals("Ruiz", result.getLastName());
        assertEquals("12345678T", result.getDni());
        assertEquals(2021, result.getContractYear());
        assertTrue(result.isActive());
        assertEquals("img", result.getImageUrl());
    }

    @Test
    void updateTeacher_shouldUpdateWithoutCreatingNewEntity() {
        // Given
        Teacher existing = new Teacher();
        existing.setId(20L);
        existing.setFirstName("Old");
        existing.setLastName("Teacher");
        existing.setDni("OLD-DNI");
        existing.setContractYear(2020);
        existing.setActive(true);
        existing.setImageUrl("old-img");

        TeacherRequestDTO dto = new TeacherRequestDTO("New", "Data", "NEW-DNI", 2025, false, "new-img");

        when(teacherRepository.findById(20L)).thenReturn(Optional.of(existing));
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        TeacherResponseDTO result = teacherService.updateTeacher(20L, dto, null);

        // Then
        ArgumentCaptor<Teacher> captor = ArgumentCaptor.forClass(Teacher.class);
        verify(teacherRepository).save(captor.capture());
        assertSame(existing, captor.getValue());
        assertEquals(20L, existing.getId());
        assertEquals("New", existing.getFirstName());
        assertEquals("Data", existing.getLastName());
        assertEquals("NEW-DNI", existing.getDni());
        assertEquals(2025, existing.getContractYear());
        assertFalse(existing.isActive());
        assertEquals("new-img", existing.getImageUrl());
        assertEquals(20L, result.getId());
    }

    @Test
    void getAllTeachers_shouldReturnList() {
        // Given
        Teacher t1 = new Teacher();
        t1.setId(1L);
        t1.setFirstName("A");
        t1.setLastName("AA");
        t1.setDni("T1");
        t1.setContractYear(2020);
        t1.setActive(true);

        Teacher t2 = new Teacher();
        t2.setId(2L);
        t2.setFirstName("B");
        t2.setLastName("BB");
        t2.setDni("T2");
        t2.setContractYear(2022);
        t2.setActive(false);

        when(teacherRepository.findAll()).thenReturn(List.of(t1, t2));

        // When
        List<TeacherResponseDTO> result = teacherService.getAllTeachers();

        // Then
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void getActiveTeachers_shouldFilterCorrectly() {
        // Given
        Teacher active = new Teacher();
        active.setId(5L);
        active.setFirstName("Active");
        active.setLastName("Teacher");
        active.setDni("ACT");
        active.setContractYear(2023);
        active.setActive(true);

        when(teacherRepository.findByActiveTrue()).thenReturn(List.of(active));

        // When
        List<TeacherResponseDTO> result = teacherService.getActiveTeachers();

        // Then
        assertEquals(1, result.size());
        assertEquals(5L, result.get(0).getId());
        assertTrue(result.get(0).isActive());
    }
}
