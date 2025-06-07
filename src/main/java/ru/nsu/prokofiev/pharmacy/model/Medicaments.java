package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "medicaments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
public class Medicaments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 256, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "type_id", nullable = false, foreignKey = @ForeignKey(name = "medicament_type_fk"))
    private MedicamentTypes type;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "unit_id", nullable = false, foreignKey = @ForeignKey(name = "medicament_unit_fk"))
    private UnitsOfMeasurement unit;

    @Column(nullable = false)
    private Boolean cookable;

    @Column(name = "prescription_required", nullable = false)
    private Boolean prescriptionRequired = false;

    @Column(nullable = false, length = 256)
    private String description;

    public Medicaments() {}

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MedicamentTypes getType() {
        return type;
    }

    public UnitsOfMeasurement getUnit() {
        return unit;
    }

    public Boolean getCookable() {
        return cookable;
    }

    public Boolean getPrescriptionRequired() {
        return prescriptionRequired;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(MedicamentTypes type) {
        this.type = type;
    }

    public void setUnit(UnitsOfMeasurement unit) {
        this.unit = unit;
    }

    public void setCookable(Boolean cookable) {
        this.cookable = cookable;
    }

    public void setPrescriptionRequired(Boolean prescriptionRequired) {
        this.prescriptionRequired = prescriptionRequired;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
