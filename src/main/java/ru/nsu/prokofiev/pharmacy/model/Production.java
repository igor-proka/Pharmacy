package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "production")
public class Production {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "technology_id", nullable = false)
    private Technologies technology;

    @Column(name = "medicament_amount", nullable = false)
    private Integer medicamentAmount;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    public Production() {}

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Technologies getTechnology() {
        return technology;
    }

    public void setTechnology(Technologies technology) {
        this.technology = technology;
    }

    public Integer getMedicamentAmount() {
        return medicamentAmount;
    }

    public void setMedicamentAmount(Integer medicamentAmount) {
        this.medicamentAmount = medicamentAmount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
