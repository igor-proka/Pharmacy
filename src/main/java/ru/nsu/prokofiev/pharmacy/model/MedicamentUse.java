package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
        import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "medicament_use")
@IdClass(MedicamentUse.MedicamentUseId.class)
public class MedicamentUse {

    @Id
    @Column(name = "type_id")
    private Long typeId;

    @Id
    @Column(name = "use_id")
    private Long useId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MedicamentTypes medicamentType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "use_id", referencedColumnName = "id", insertable = false, updatable = false)
    private WaysOfUse wayOfUse;

    public MedicamentUse() {}

    public MedicamentUse(Long typeId, Long useId) {
        this.typeId = typeId;
        this.useId = useId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getUseId() {
        return useId;
    }

    public void setUseId(Long useId) {
        this.useId = useId;
    }

    public MedicamentTypes getMedicamentType() {
        return medicamentType;
    }

    public void setMedicamentType(MedicamentTypes medicamentType) {
        this.medicamentType = medicamentType;
        if (medicamentType != null) {
            this.typeId = medicamentType.getId();
        }
    }

    public WaysOfUse getWayOfUse() {
        return wayOfUse;
    }

    public void setWayOfUse(WaysOfUse wayOfUse) {
        this.wayOfUse = wayOfUse;
        if (wayOfUse != null) {
            this.useId = wayOfUse.getId();
        }
    }

    public static class MedicamentUseId implements Serializable {
        private Long typeId;
        private Long useId;

        public MedicamentUseId() {}

        public MedicamentUseId(Long typeId, Long useId) {
            this.typeId = typeId;
            this.useId = useId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MedicamentUseId)) return false;
            MedicamentUseId that = (MedicamentUseId) o;
            return Objects.equals(typeId, that.typeId) &&
                    Objects.equals(useId, that.useId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(typeId, useId);
        }
    }
}
