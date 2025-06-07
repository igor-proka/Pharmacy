package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WaitingMedicamentsId implements Serializable {

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "medicament_id")
    private Long medicamentId;

    public WaitingMedicamentsId() {}

    public WaitingMedicamentsId(Long orderId, Long medicamentId) {
        this.orderId = orderId;
        this.medicamentId = medicamentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getMedicamentId() {
        return medicamentId;
    }

    public void setMedicamentId(Long medicamentId) {
        this.medicamentId = medicamentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WaitingMedicamentsId)) return false;
        WaitingMedicamentsId that = (WaitingMedicamentsId) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(medicamentId, that.medicamentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, medicamentId);
    }
}
