package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReservedMedicamentsId implements Serializable {

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "storage_id")
    private Long storageId;

    public ReservedMedicamentsId() {}

    public ReservedMedicamentsId(Long orderId, Long storageId) {
        this.orderId = orderId;
        this.storageId = storageId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservedMedicamentsId)) return false;
        ReservedMedicamentsId that = (ReservedMedicamentsId) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(storageId, that.storageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, storageId);
    }
}
