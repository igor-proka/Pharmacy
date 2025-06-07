package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "ointments")
public class Ointments {

    @Id
    @Column(name = "medicament_id")
    private Long medicamentId;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "medicament_id", foreignKey = @ForeignKey(name = "ointments_fk_medicament"))
    private MedicamentPackages medicamentPackage;

    @Column(name = "active_ingredient", nullable = false, length = 256)
    private String activeIngredient;

    @Column(name = "volume_ml", nullable = false)
    private Integer volumeMl;

    public Ointments() {}

    public Long getMedicamentId() {
        return medicamentId;
    }

    public void setMedicamentId(Long medicamentId) {
        this.medicamentId = medicamentId;
    }

    public MedicamentPackages getMedicamentPackage() {
        return medicamentPackage;
    }

    public void setMedicamentPackage(MedicamentPackages medicamentPackage) {
        this.medicamentPackage = medicamentPackage;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public Integer getVolumeMl() {
        return volumeMl;
    }

    public void setVolumeMl(Integer volumeMl) {
        this.volumeMl = volumeMl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ointments)) return false;
        Ointments ointments = (Ointments) o;
        return Objects.equals(medicamentId, ointments.medicamentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicamentId);
    }
}
