package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "medicament_packages")
public class MedicamentPackages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Внешний ключ на Medicaments
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "medicament_id", nullable = false, foreignKey = @ForeignKey(name = "medicament_fk"))
    private Medicaments medicament;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "critical_amount", nullable = false)
    private Integer criticalAmount = 0;

    public MedicamentPackages() {}

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public Medicaments getMedicament() {
        return medicament;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getCriticalAmount() {
        return criticalAmount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMedicament(Medicaments medicament) {
        this.medicament = medicament;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCriticalAmount(Integer criticalAmount) {
        this.criticalAmount = criticalAmount;
    }
}
