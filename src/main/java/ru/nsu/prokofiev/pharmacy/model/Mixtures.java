package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "mixtures")
public class Mixtures {

    @Id
    @Column(name = "medicament_id")
    private Long medicamentId;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "medicament_id", foreignKey = @ForeignKey(name = "mixtures_fk_medicament"))
    private MedicamentPackages medicamentPackage;

    @Column(nullable = false, length = 256)
    private String solvent;

    @Column(name = "sugar_free", nullable = false)
    private Boolean sugarFree;

    @Column(name = "volume_ml", nullable = false)
    private Integer volumeMl;

    public Mixtures() {}

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

    public String getSolvent() {
        return solvent;
    }

    public void setSolvent(String solvent) {
        this.solvent = solvent;
    }

    public Boolean getSugarFree() {
        return sugarFree;
    }

    public void setSugarFree(Boolean sugarFree) {
        this.sugarFree = sugarFree;
    }

    public Integer getVolumeMl() {
        return volumeMl;
    }

    public void setVolumeMl(Integer volumeMl) {
        this.volumeMl = volumeMl;
    }

    // equals и hashCode по medicamentId

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mixtures)) return false;
        Mixtures mixtures = (Mixtures) o;
        return Objects.equals(medicamentId, mixtures.medicamentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicamentId);
    }
}
