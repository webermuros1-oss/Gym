package inditex.P1.Gym.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName", length = 100)
    private String firstName;

    @Column(name = "lastName", length = 100)
    private String lastName;

    @Column(unique = true, length = 20)
    private String dni;

    @Column(name = "contractYear")
    private Integer contractYear;

    private boolean active;

    @Column(name = "imageUrl", length = 255)
    private String imageUrl;
}