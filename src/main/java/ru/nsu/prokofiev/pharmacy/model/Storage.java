package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "storage")
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicament_id", nullable = false)
    private MedicamentPackages medicamentPackage;

    @Column(name = "available_amount", nullable = false)
    private Integer availableAmount;

    @Column(name = "original_amount", nullable = false)
    private Integer originalAmount;

    @Column(name = "receipt_datetime", nullable = false)
    private LocalDateTime receiptDatetime;

    @Column(name = "manufacture_date")
    private LocalDate manufactureDate;

    @Column(name = "shelf_life")
    private Integer shelfLife;

    public Storage() {}

    // геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MedicamentPackages getMedicamentPackage() { return medicamentPackage; }
    public void setMedicamentPackage(MedicamentPackages medicamentPackage) { this.medicamentPackage = medicamentPackage; }

    public Integer getAvailableAmount() { return availableAmount; }
    public void setAvailableAmount(Integer availableAmount) { this.availableAmount = availableAmount; }

    public Integer getOriginalAmount() { return originalAmount; }
    public void setOriginalAmount(Integer originalAmount) { this.originalAmount = originalAmount; }

    public LocalDateTime getReceiptDatetime() { return receiptDatetime; }
    public void setReceiptDatetime(LocalDateTime receiptDatetime) { this.receiptDatetime = receiptDatetime; }

    public LocalDate getManufactureDate() { return manufactureDate; }
    public void setManufactureDate(LocalDate manufactureDate) { this.manufactureDate = manufactureDate; }

    public Integer getShelfLife() { return shelfLife; }
    public void setShelfLife(Integer shelfLife) { this.shelfLife = shelfLife; }

    @PrePersist
    @PreUpdate
    private void validate() {
        if (manufactureDate != null && manufactureDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("manufactureDate cannot be in the future");
        }
        if (receiptDatetime == null) {
            throw new IllegalArgumentException("receiptDatetime cannot be null");
        }
        if (manufactureDate != null && receiptDatetime.toLocalDate().isBefore(manufactureDate)) {
            throw new IllegalArgumentException("receiptDatetime cannot be earlier than manufactureDate");
        }
        if (availableAmount != null && originalAmount != null && availableAmount > originalAmount) {
            throw new IllegalArgumentException("availableAmount cannot be greater than originalAmount");
        }
        if (availableAmount != null && availableAmount < 0) {
            throw new IllegalArgumentException("availableAmount must be >= 0");
        }
        if (originalAmount != null && originalAmount <= 0) {
            throw new IllegalArgumentException("originalAmount must be > 0");
        }
        if (shelfLife != null && shelfLife <= 0) {
            throw new IllegalArgumentException("shelfLife must be > 0");
        }
    }
}
