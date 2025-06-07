package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "technologies")
public class Technologies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "medicament_id", nullable = false)
    private MedicamentPackages medicament;

    @Column(name = "preparation_time", nullable = false)
    private Integer preparationTime;

    @Column(nullable = false)
    private Integer amount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "preparation_method_id", nullable = false)
    private PreparationMethods preparationMethod;

    public Technologies() {
    }

    public Technologies(MedicamentPackages medicament, Integer preparationTime, Integer amount, PreparationMethods preparationMethod) {
        this.medicament = medicament;
        this.preparationTime = preparationTime;
        this.amount = amount;
        this.preparationMethod = preparationMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MedicamentPackages getMedicament() {
        return medicament;
    }

    public void setMedicament(MedicamentPackages medicament) {
        this.medicament = medicament;
    }

    public Integer getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public PreparationMethods getPreparationMethod() {
        return preparationMethod;
    }

    public void setPreparationMethod(PreparationMethods preparationMethod) {
        this.preparationMethod = preparationMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Technologies)) return false;
        Technologies that = (Technologies) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
