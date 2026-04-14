package inditex.P1.Gym.mapper;

import inditex.P1.Gym.dto.ActivityDTO;
import inditex.P1.Gym.entity.Activity;
import inditex.P1.Gym.entity.Teacher;

public final class ActivityMapper {

    private ActivityMapper() {
    }

    public static ActivityDTO toDTO(Activity activity) {
        if (activity == null) {
            return null;
        }

        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setId(activity.getId());
        activityDTO.setTitle(activity.getTitle());
        activityDTO.setDescription(activity.getDescription());
        activityDTO.setPrice(activity.getPrice());
        activityDTO.setDate(activity.getDate());
        activityDTO.setImageUrl(activity.getImageUrl());

        Teacher teacher = activity.getTeacher();
        activityDTO.setTeacherId(teacher != null ? teacher.getId() : null);

        return activityDTO;
    }
}
