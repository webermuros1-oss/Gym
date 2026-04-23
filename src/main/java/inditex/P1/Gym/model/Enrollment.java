package inditex.P1.Gym.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    @EmbeddedId
    private EnrollmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("activityId")
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "paid", nullable = false)
    private boolean paid;

    @Column(name = "price_paid", precision = 10, scale = 2)
    private BigDecimal pricePaid;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", length = 20, nullable = false)
    private AttendanceStatus attendanceStatus;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "discount_applied", precision = 5, scale = 2)
    private BigDecimal discountApplied;

    public Enrollment(Activity activity, User user) {
        this.activity = activity;
        this.user = user;
        this.id = new EnrollmentId(activity.getId(), user.getId());
        this.registeredAt = LocalDateTime.now();
        this.paid = false;
        this.attendanceStatus = AttendanceStatus.PENDING;
    }
}