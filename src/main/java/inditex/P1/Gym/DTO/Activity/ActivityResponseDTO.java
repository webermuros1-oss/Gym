package inditex.P1.Gym.DTO.Activity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponseDTO {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private LocalDateTime date;
    private String imageUrl;
    private Long teacherId;
    private int enrolledCount;
}
