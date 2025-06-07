package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "doctors", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"full_name", "position"})
})
public class Doctors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 256)
    private String fullName;

    @Column(nullable = false, length = 128)
    private String position;

    public Doctors() {}

    public Doctors(String fullName, String position) {
        this.fullName = fullName;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
