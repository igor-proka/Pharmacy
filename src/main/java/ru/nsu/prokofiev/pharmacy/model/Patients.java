package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "patients", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"full_name", "birthday"})
})
public class Patients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 256)
    private String fullName;

    @Column(nullable = false)
    private LocalDate birthday;

    public Patients() {}

    public Patients(String fullName, LocalDate birthday) {
        this.fullName = fullName;
        this.birthday = birthday;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
