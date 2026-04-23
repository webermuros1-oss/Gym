package inditex.P1.Gym.DTO.Activity;

import java.util.List;
import java.util.stream.Collectors;

import inditex.P1.Gym.DTO.Teacher.TeacherMapper;
import inditex.P1.Gym.DTO.Teacher.TeacherResponseDTO;
import inditex.P1.Gym.DTO.user.UserMapper;
import inditex.P1.Gym.DTO.user.UserResponseDTO;
import inditex.P1.Gym.model.Activity;
import inditex.P1.Gym.model.Teacher;

public class ActivityMapper {

    public static Activity dto2Entity(ActivityRequestDTO dto, Teacher teacher) {
        Activity activity = new Activity();
        activity.setTitle(dto.getTitle());
        activity.setDescription(dto.getDescription());
        activity.setPrice(dto.getPrice());
        activity.setDate(dto.getDate());
        activity.setImageUrl(dto.getImageUrl());
        activity.setTeacher(teacher);
        return activity;
    }

    public static ActivityResponseDTO entity2DTO(Activity activity) {
        Long teacherId = activity.getTeacher() != null ? activity.getTeacher().getId() : null;
        return new ActivityResponseDTO(
                activity.getId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getPrice(),
                activity.getDate(),
                activity.getImageUrl(),
                teacherId,
                activity.getEnrollments().size()
        );
    }

    public static void updateEntityFromDto(Activity activity, ActivityRequestDTO dto, Teacher teacher) {
        activity.setTitle(dto.getTitle());
        activity.setDescription(dto.getDescription());
        activity.setPrice(dto.getPrice());
        activity.setDate(dto.getDate());
        activity.setImageUrl(dto.getImageUrl());
        activity.setTeacher(teacher);
    }

    public static ActivityDetailResponseDTO entity2DetailDTO(Activity activity) {
        TeacherResponseDTO teacherDTO = activity.getTeacher() != null
                ? TeacherMapper.entity2DTO(activity.getTeacher())
                : null;

        List<UserResponseDTO> userDTOs = activity.getEnrollments().stream()
                .map(enrollment -> UserMapper.entity2DTO(enrollment.getUser()))
                .collect(Collectors.toList());

        return new ActivityDetailResponseDTO(
                activity.getId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getPrice(),
                activity.getDate(),
                activity.getImageUrl(),
                teacherDTO,
                userDTOs,
                userDTOs.size()
        );
    }
}


