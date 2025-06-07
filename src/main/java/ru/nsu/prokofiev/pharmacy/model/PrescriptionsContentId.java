// src/main/java/ru/nsu/prokofiev/pharmacy/model/PrescriptionsContentId.java
package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PrescriptionsContentId implements Serializable {

    @Column(name = "prescription_id")
    private Long prescriptionId;

    @Column(name = "medicament_id")
    private Long medicamentId;

    @Column(name = "amount")
    private Long amount;      // изменено с Integer на Long

    @Column(name = "way_to_use")
    private Long wayToUse;    // уже Long

    public PrescriptionsContentId() {}

    public PrescriptionsContentId(Long prescriptionId, Long medicamentId, Long amount, Long wayToUse) {
        this.prescriptionId = prescriptionId;
        this.medicamentId = medicamentId;
        this.amount = amount;
        this.wayToUse = wayToUse;
    }

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Long getMedicamentId() {
        return medicamentId;
    }

    public void setMedicamentId(Long medicamentId) {
        this.medicamentId = medicamentId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getWayToUse() {
        return wayToUse;
    }

    public void setWayToUse(Long wayToUse) {
        this.wayToUse = wayToUse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrescriptionsContentId)) return false;
        PrescriptionsContentId that = (PrescriptionsContentId) o;
        return Objects.equals(prescriptionId, that.prescriptionId) &&
                Objects.equals(medicamentId, that.medicamentId) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(wayToUse, that.wayToUse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prescriptionId, medicamentId, amount, wayToUse);
    }
}
