package inditex.P1.Gym.DTO.Activity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequestDTO {

    @NotBlank(message = "El titulo no puede estar vacio")
    private String title;

    @NotBlank(message = "La descripcion no puede estar vacia")
    private String description;

    @NotNull(message = "El precio no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    private BigDecimal price;

    @NotNull(message = "La fecha no puede ser nula")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime date;

    private String imageUrl;

    @NotNull(message = "El id del profesor no puede ser nulo")
    @Min(value = 1, message = "El id del profesor no es valido")
    private Long teacherId;
}
