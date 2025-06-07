// src/main/java/ru/nsu/prokofiev/pharmacy/model/PreparedPackageDto.java
package ru.nsu.prokofiev.pharmacy.model;

import java.math.BigDecimal;

/**
 * DTO для отчёта «Цена готового лекарства (упаковка)».
 */
public class PreparedPackageDto {
    private Long packageId;
    private String medicamentName;
    private BigDecimal price;

    public PreparedPackageDto() { }

    public PreparedPackageDto(Long packageId, String medicamentName, BigDecimal price) {
        this.packageId        = packageId;
        this.medicamentName   = medicamentName;
        this.price            = price;
    }

    public Long getPackageId() {
        return packageId;
    }
    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getMedicamentName() {
        return medicamentName;
    }
    public void setMedicamentName(String medicamentName) {
        this.medicamentName = medicamentName;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
