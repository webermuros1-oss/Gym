package inditex.P1.Gym.DTO.Teacher;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String firstName;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String lastName;

    @NotBlank(message = "El DNI no puede estar vacío")
    private String dni;

    @NotNull(message = "El año de contratación no puede ser nulo")
    @Min(value = 2000, message = "El año de contratación no es válido")
    private Integer contractYear;

    private boolean active;

    private String imageUrl;
}