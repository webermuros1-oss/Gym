package inditex.P1.Gym.DTO.Enrollment;

import inditex.P1.Gym.model.Activity;
import inditex.P1.Gym.model.Enrollment;
import inditex.P1.Gym.model.User;

public class EnrollmentMapper {

    public static EnrollmentResponseDTO entity2DTO(Enrollment enrollment) {
        Activity activity = enrollment.getActivity();
        User user = enrollment.getUser();
        return new EnrollmentResponseDTO(
                activity != null ? activity.getId() : null,
                activity != null ? activity.getTitle() : null,
                user != null ? user.getId() : null,
                user != null ? (user.getFirstName() + " " + user.getLastName()) : null,
                enrollment.getRegisteredAt(),
                enrollment.isPaid(),
                enrollment.getPricePaid(),
                enrollment.getAttendanceStatus(),
                enrollment.getCancelledAt(),
                enrollment.getNotes(),
                enrollment.getDiscountApplied()
        );
    }

    public static void updateEntityFromDto(Enrollment enrollment, EnrollmentRequestDTO dto) {
        if (dto.getPaid() != null) {
            enrollment.setPaid(dto.getPaid());
        }
        if (dto.getPricePaid() != null) {
            enrollment.setPricePaid(dto.getPricePaid());
        }
        if (dto.getAttendanceStatus() != null) {
            enrollment.setAttendanceStatus(dto.getAttendanceStatus());
        }
        if (dto.getNotes() != null) {
            enrollment.setNotes(dto.getNotes());
        }
        if (dto.getDiscountApplied() != null) {
            enrollment.setDiscountApplied(dto.getDiscountApplied());
        }
    }
}