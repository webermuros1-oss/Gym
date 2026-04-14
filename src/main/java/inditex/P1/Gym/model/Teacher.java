package inditex.P1.Gym.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(min = 9, max = 9)
    @Column(unique = true)
    private String dni;

    @NotNull
    private Integer hireYear;

    private boolean active;

    private String ImageUrl;

    @OneToMany(mappedBy = "teacher")
    private List<Activity> activities = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
