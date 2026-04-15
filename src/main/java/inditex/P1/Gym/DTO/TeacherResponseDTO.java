package inditex.P1.Gym.DTO;

public class TeacherResponseDTO {

    private long id;
    private String name;
    private String dni;
    private int hireYear;
    private boolean active;
    private String imageUrl;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public int getHireYear() { return hireYear; }
    public void setHireYear(int hireYear) { this.hireYear = hireYear; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

}
