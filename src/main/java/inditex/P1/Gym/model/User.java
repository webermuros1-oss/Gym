package inditex.P1.Gym.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    @Column(name = "first_name", length = 100)
    private String firstName;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Column(name = "last_name", length = 100)
    private String lastName;

    @NotBlank(message = "El DNI no puede estar vacío")
    @Column(unique = true, length = 20)
    private String dni;

    @NotNull(message = "El año de alta no puede ser nulo")
    @Min(value = 2000, message = "El año de alta no es válido")
    @Column(name = "registration_year")
    private Integer registrationYear;

    private boolean active;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @ManyToMany(mappedBy = "users")
    private Set<Activity> activities = new HashSet<>();
}
