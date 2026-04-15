package inditex.P1.Gym.DTO;
import jakarta . validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TeacherRequestDTO {
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 9, min = 9)
    private String dni;

    @NotNull
    @Min(1900)
    private Integer hireYear;

    private boolean active;

    private String getName() { return name; }
    public void setDni (String dni) { this.dni = dni ;}

    public Integer getHireYear() { return hireYear; }
    public void setHireYear(Integer hireYear) { this.hireYear = hireYear; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

}
