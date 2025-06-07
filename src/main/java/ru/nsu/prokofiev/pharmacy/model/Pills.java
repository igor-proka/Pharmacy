package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "pills")
public class Pills {

    @Id
    @Column(name = "medicament_id")
    private Long medicamentId;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "medicament_id", foreignKey = @ForeignKey(name = "pills_fk_medicament"))
    private MedicamentPackages medicamentPackage;

    @Column(name = "mass_per_pill", nullable = false)
    private Integer massPerPill;

    @Column(name = "pills_per_pack", nullable = false)
    private Integer pillsPerPack;

    public Pills() {}

    // Геттеры и сеттеры

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

    public Integer getMassPerPill() {
        return massPerPill;
    }

    public void setMassPerPill(Integer massPerPill) {
        this.massPerPill = massPerPill;
    }

    public Integer getPillsPerPack() {
        return pillsPerPack;
    }

    public void setPillsPerPack(Integer pillsPerPack) {
        this.pillsPerPack = pillsPerPack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pills)) return false;
        Pills pills = (Pills) o;
        return Objects.equals(medicamentId, pills.medicamentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicamentId);
    }
}
