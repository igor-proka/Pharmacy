package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "supplies")
public class Supplies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medicament_id", nullable = false)
    private MedicamentPackages medicament;

    @Column(name = "medicament_amount", nullable = false)
    private Integer medicamentAmount;

    @Column(name = "cost", nullable = false)
    private Integer cost;

    @Column(name = "assigned_datetime", nullable = false)
    private LocalDateTime assignedDatetime;

    @Column(name = "delivery_datetime")
    private LocalDateTime deliveryDatetime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Suppliers supplier;

    // Getters and setters

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

    public Integer getMedicamentAmount() {
        return medicamentAmount;
    }

    public void setMedicamentAmount(Integer medicamentAmount) {
        this.medicamentAmount = medicamentAmount;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public LocalDateTime getAssignedDatetime() {
        return assignedDatetime;
    }

    public void setAssignedDatetime(LocalDateTime assignedDatetime) {
        this.assignedDatetime = assignedDatetime;
    }

    public LocalDateTime getDeliveryDatetime() {
        return deliveryDatetime;
    }

    public void setDeliveryDatetime(LocalDateTime deliveryDatetime) {
        this.deliveryDatetime = deliveryDatetime;
    }

    public Suppliers getSupplier() {
        return supplier;
    }

    public void setSupplier(Suppliers supplier) {
        this.supplier = supplier;
    }
}
