package ru.nsu.prokofiev.pharmacy.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "reserved_medicaments")
public class ReservedMedicaments {

    @EmbeddedId
    private ReservedMedicamentsId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("storageId")
    @JoinColumn(name = "storage_id", nullable = false)
    private Storage storage;

    @Column(name = "package_count", nullable = false)
    private Integer packageCount;

    public ReservedMedicaments() {}

    public ReservedMedicaments(ReservedMedicamentsId id, Orders order, Storage storage, Integer packageCount) {
        this.id = id;
        this.order = order;
        this.storage = storage;
        this.packageCount = packageCount;
    }

    public ReservedMedicamentsId getId() {
        return id;
    }

    public void setId(ReservedMedicamentsId id) {
        this.id = id;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Integer getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(Integer packageCount) {
        this.packageCount = packageCount;
    }
}
