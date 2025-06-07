package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "prescriptions_content")
public class PrescriptionsContent {

    @EmbeddedId
    private PrescriptionsContentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("prescriptionId")
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescriptions prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("medicamentId")
    @JoinColumn(name = "medicament_id", nullable = false)
    private Medicaments medicament;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("wayToUse")
    @JoinColumn(name = "way_to_use", nullable = false)
    private WaysOfUse wayOfUse;

    public PrescriptionsContent() {}

    public PrescriptionsContent(PrescriptionsContentId id, Prescriptions prescription, Medicaments medicament, WaysOfUse wayOfUse) {
        this.id = id;
        this.prescription = prescription;
        this.medicament = medicament;
        this.wayOfUse = wayOfUse;
    }

    public PrescriptionsContentId getId() {
        return id;
    }

    public void setId(PrescriptionsContentId id) {
        this.id = id;
    }

    public Prescriptions getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescriptions prescription) {
        this.prescription = prescription;
    }

    public Medicaments getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicaments medicament) {
        this.medicament = medicament;
    }

    public WaysOfUse getWayOfUse() {
        return wayOfUse;
    }

    public void setWayOfUse(WaysOfUse wayOfUse) {
        this.wayOfUse = wayOfUse;
    }

    public Long getAmount() {
        return id != null ? id.getAmount() : null;
    }

    public void setAmount(Long amount) {
        if (id == null) {
            id = new PrescriptionsContentId();
        }
        id.setAmount(amount);
    }
}
