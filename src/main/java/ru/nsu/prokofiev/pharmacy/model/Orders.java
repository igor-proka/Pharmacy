package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id")
    private Prescriptions prescription;

    @Column(name = "registration_datetime", nullable = false)
    private LocalDateTime registrationDatetime;

    @Column(name = "appointed_datetime")
    private LocalDateTime appointedDatetime;

    @Column(name = "obtaining_datetime")
    private LocalDateTime obtainingDatetime;

    @Column(nullable = false)
    private Integer cost;

    @Column(nullable = false)
    private Boolean paid = false;

    @ManyToOne(fetch = FetchType.EAGER) // вместо LAZY
    @JoinColumn(name = "customer_id", nullable = false)
    private Customers customer;

    public Orders() {}

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prescriptions getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescriptions prescription) {
        this.prescription = prescription;
    }

    public LocalDateTime getRegistrationDatetime() {
        return registrationDatetime;
    }

    public void setRegistrationDatetime(LocalDateTime registrationDatetime) {
        this.registrationDatetime = registrationDatetime;
    }

    public LocalDateTime getAppointedDatetime() {
        return appointedDatetime;
    }

    public void setAppointedDatetime(LocalDateTime appointedDatetime) {
        this.appointedDatetime = appointedDatetime;
    }

    public LocalDateTime getObtainingDatetime() {
        return obtainingDatetime;
    }

    public void setObtainingDatetime(LocalDateTime obtainingDatetime) {
        this.obtainingDatetime = obtainingDatetime;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }
}
