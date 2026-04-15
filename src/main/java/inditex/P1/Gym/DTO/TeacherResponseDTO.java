package inditex.P1.Gym.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private Integer contractYear;
    private boolean active;
    private String imageUrl;
}