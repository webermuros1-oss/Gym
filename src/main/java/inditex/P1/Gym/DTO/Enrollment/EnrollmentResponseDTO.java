package inditex.P1.Gym.DTO.Enrollment;

import inditex.P1.Gym.model.AttendanceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponseDTO {

    private Long activityId;
    private String activityTitle;
    private Long userId;
    private String userFullName;
    private LocalDateTime registeredAt;
    private boolean paid;
    private BigDecimal pricePaid;
    private AttendanceStatus attendanceStatus;
    private LocalDateTime cancelledAt;
    private String notes;
    private BigDecimal discountApplied;
}