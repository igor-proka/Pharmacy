package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "waiting_medicaments")
public class WaitingMedicaments {

    @EmbeddedId
    private WaitingMedicamentsId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("medicamentId")
    @JoinColumn(name = "medicament_id", nullable = false)
    private MedicamentPackages medicament;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    public WaitingMedicaments() {}

    public WaitingMedicaments(WaitingMedicamentsId id, Orders order, MedicamentPackages medicament, Integer amount) {
        this.id = id;
        this.order = order;
        this.medicament = medicament;
        this.amount = amount;
    }

    public WaitingMedicamentsId getId() {
        return id;
    }

    public void setId(WaitingMedicamentsId id) {
        this.id = id;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public MedicamentPackages getMedicament() {
        return medicament;
    }

    public void setMedicament(MedicamentPackages medicament) {
        this.medicament = medicament;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
