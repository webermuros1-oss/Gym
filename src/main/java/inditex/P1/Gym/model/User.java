package inditex.P1.Gym.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "firstName", length = 100)
    private String firstName;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Column(name = "lastName", length = 100)
    private String lastName;

    @NotBlank(message = "El DNI no puede estar vacío")
    @Column(unique = true, length = 20)
    private String dni;

    @NotNull(message = "El año de alta no puede ser nulo")
    @Min(value = 2000, message = "El año de alta no es válido")
    @Column(name = "registrationYear")
    private Integer registrationYear;

    private boolean active;

    @Column(name = "imageUrl", length = 255)
    private String imageUrl;
}
