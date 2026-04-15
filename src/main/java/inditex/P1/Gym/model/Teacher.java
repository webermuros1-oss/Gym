package inditex.P1.Gym.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Teacher {

    @Id
    private Long id;

    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
