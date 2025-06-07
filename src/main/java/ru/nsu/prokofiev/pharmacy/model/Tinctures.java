package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "tinctures")
public class Tinctures {

    @Id
    @Column(name = "medicament_id")
    private Long medicamentId;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "medicament_id", foreignKey = @ForeignKey(name = "tinctures_fk_medicament"))
    private MedicamentPackages medicamentPackage;

    @Column(name = "alcohol_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal alcoholPercentage;

    @Column(name = "material", nullable = false, length = 256)
    private String material;

    @Column(name = "volume_ml", nullable = false)
    private Integer volumeMl;

    public Tinctures() {}

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

    public BigDecimal getAlcoholPercentage() {
        return alcoholPercentage;
    }

    public void setAlcoholPercentage(BigDecimal alcoholPercentage) {
        this.alcoholPercentage = alcoholPercentage;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
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
        if (!(o instanceof Tinctures)) return false;
        Tinctures tinctures = (Tinctures) o;
        return Objects.equals(medicamentId, tinctures.medicamentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicamentId);
    }
}
