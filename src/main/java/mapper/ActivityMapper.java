package mapper;

import inditex.P1.Gym.DTO.ActivityRequestDTO;
import inditex.P1.Gym.DTO.ActivityResponseDTO;
import inditex.P1.Gym.model.Activity;
import inditex.P1.Gym.model.Teacher;

public final class ActivityMapper {

    private ActivityMapper() {
    }

    public static ActivityResponseDTO toDTO(Activity activity) {
        if (activity == null) {
            return null;
        }

        Long teacherId = null;
        if (activity.getTeacher() != null) {
            teacherId = activity.getTeacher().getId();
        }

        int enrolledCount = 0;
        if (activity.getUsers() != null) {
            enrolledCount = activity.getUsers().size();
        }

        ActivityResponseDTO dto = new ActivityResponseDTO();
        dto.setId(activity.getId());
        dto.setTitle(activity.getTitle());
        dto.setDescription(activity.getDescription());
        dto.setPrice(activity.getPrice());
        dto.setDate(activity.getDate());
        dto.setImageUrl(activity.getImageUrl());
        dto.setTeacherId(teacherId);
        dto.setEnrolledCount(enrolledCount);
        return dto;
    }

    public static Activity toEntity(ActivityRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Activity activity = new Activity();
        activity.setTitle(dto.getTitle());
        activity.setDescription(dto.getDescription());
        activity.setPrice(dto.getPrice());
        activity.setDate(dto.getDate());
        activity.setImageUrl(dto.getImageUrl());
        return activity;
    }

    public static void updateEntity(Activity activity, ActivityRequestDTO dto) {
        if (activity == null || dto == null) {
            return;
        }

        activity.setTitle(dto.getTitle());
        activity.setDescription(dto.getDescription());
        activity.setPrice(dto.getPrice());
        activity.setDate(dto.getDate());
        activity.setImageUrl(dto.getImageUrl());
    }

    public static void setTeacher(Activity activity, Teacher teacher) {
        if (activity == null) {
            return;
        }
        activity.setTeacher(teacher);
    }
}
