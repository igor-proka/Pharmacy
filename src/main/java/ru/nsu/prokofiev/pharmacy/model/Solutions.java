package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "solutions")
public class Solutions {

    @Id
    @Column(name = "medicament_id")
    private Long medicamentId;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "medicament_id", foreignKey = @ForeignKey(name = "solutions_fk_medicament"))
    private MedicamentPackages medicamentPackage;

    @Column(name = "concentration", nullable = false, precision = 5, scale = 2)
    private BigDecimal concentration;

    @Column(name = "ph_level", nullable = false, precision = 3, scale = 2)
    private BigDecimal phLevel;

    @Column(name = "volume_ml", nullable = false)
    private Integer volumeMl;

    public Solutions() {}

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

    public BigDecimal getConcentration() {
        return concentration;
    }

    public void setConcentration(BigDecimal concentration) {
        this.concentration = concentration;
    }

    public BigDecimal getPhLevel() {
        return phLevel;
    }

    public void setPhLevel(BigDecimal phLevel) {
        this.phLevel = phLevel;
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
        if (!(o instanceof Solutions)) return false;
        Solutions solutions = (Solutions) o;
        return Objects.equals(medicamentId, solutions.medicamentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicamentId);
    }
}
