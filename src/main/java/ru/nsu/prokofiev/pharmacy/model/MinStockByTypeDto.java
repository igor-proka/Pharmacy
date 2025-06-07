// src/main/java/ru/nsu/prokofiev/pharmacy/model/MinStockByTypeDto.java
package ru.nsu.prokofiev.pharmacy.model;

/**
 * DTO для отчёта «Лекарства с минимальным запасом (по типу)».
 */
public class MinStockByTypeDto {
    private Long medicamentId;
    private String medicamentName;
    private Integer availableAmount;
    private Integer minimalInType;
    private Long countAtMinimumInType;

    public MinStockByTypeDto() { }

    public MinStockByTypeDto(Long medicamentId, String medicamentName, Integer availableAmount,
                             Integer minimalInType, Long countAtMinimumInType) {
        this.medicamentId          = medicamentId;
        this.medicamentName        = medicamentName;
        this.availableAmount       = availableAmount;
        this.minimalInType         = minimalInType;
        this.countAtMinimumInType  = countAtMinimumInType;
    }

    public Long getMedicamentId() {
        return medicamentId;
    }
    public void setMedicamentId(Long medicamentId) {
        this.medicamentId = medicamentId;
    }

    public String getMedicamentName() {
        return medicamentName;
    }
    public void setMedicamentName(String medicamentName) {
        this.medicamentName = medicamentName;
    }

    public Integer getAvailableAmount() {
        return availableAmount;
    }
    public void setAvailableAmount(Integer availableAmount) {
        this.availableAmount = availableAmount;
    }

    public Integer getMinimalInType() {
        return minimalInType;
    }
    public void setMinimalInType(Integer minimalInType) {
        this.minimalInType = minimalInType;
    }

    public Long getCountAtMinimumInType() {
        return countAtMinimumInType;
    }
    public void setCountAtMinimumInType(Long countAtMinimumInType) {
        this.countAtMinimumInType = countAtMinimumInType;
    }
}
