package inditex.P1.Gym.DTO.Enrollment;

import inditex.P1.Gym.model.AttendanceStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequestDTO {

    private Boolean paid;

    @DecimalMin(value = "0.0", message = "El precio pagado no puede ser negativo")
    private BigDecimal pricePaid;

    private AttendanceStatus attendanceStatus;

    @Size(max = 500, message = "Las notas no pueden superar 500 caracteres")
    private String notes;

    @DecimalMin(value = "0.0", message = "El descuento no puede ser negativo")
    @DecimalMax(value = "100.0", message = "El descuento no puede superar 100")
    private BigDecimal discountApplied;
}