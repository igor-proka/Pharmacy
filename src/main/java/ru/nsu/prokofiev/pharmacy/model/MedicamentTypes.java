package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "medicament_types")
public class MedicamentTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // <- ОБЯЗАТЕЛЬНО ДОЛЖНО БЫТЬ

    @Column(nullable = false, unique = true)
    private String name;

    public MedicamentTypes() {
    }

    public MedicamentTypes(String name) {
        this.name = name;
    }

    public Long getId() { // <-- ДОЛЖЕН БЫТЬ
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) { // <-- Желательно для корректности PUT
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    // equals() и hashCode() — желательно переопределить, но не обязательно сейчас
}