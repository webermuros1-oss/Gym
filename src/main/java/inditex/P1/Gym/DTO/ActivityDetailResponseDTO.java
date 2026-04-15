package inditex.P1.Gym.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDetailResponseDTO {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private LocalDateTime date;
    private String imageUrl;
    private TeacherResponseDTO teacher;
    private List<UserResponseDTO> users;
}