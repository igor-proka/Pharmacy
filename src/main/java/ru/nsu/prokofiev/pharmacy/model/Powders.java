package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "powders")
public class Powders {

    @Id
    @Column(name = "medicament_id")
    private Long medicamentId;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "medicament_id", foreignKey = @ForeignKey(name = "powders_fk_medicament"))
    private MedicamentPackages medicamentPackage;

    @Column(name = "is_compound", nullable = false)
    private Boolean isCompound;

    @Column(name = "package_weight", nullable = false)
    private Integer packageWeight;

    public Powders() {}

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

    public Boolean getIsCompound() {
        return isCompound;
    }

    public void setIsCompound(Boolean isCompound) {
        this.isCompound = isCompound;
    }

    public Integer getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(Integer packageWeight) {
        this.packageWeight = packageWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Powders)) return false;
        Powders powders = (Powders) o;
        return Objects.equals(medicamentId, powders.medicamentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicamentId);
    }
}
