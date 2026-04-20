package inditex.P1.Gym.DTO;


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
public class UserRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String firstName;

    @NotBlank(message = "Los apellidos no pueden estar vacios")
    private String lastName;

    @NotBlank(message = "El DNI no puede estar vacio")
    private String dni;

    @NotNull(message = "El año de alta no puede ser nulo")
    @Min(value = 2000, message = "El año de alta no es válido")
    private Integer registrationYear;

    private boolean active;

    private String imageUrl;

}
