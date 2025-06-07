// src/main/java/ru/nsu/prokofiev/pharmacy/model/MinStockDto.java
package ru.nsu.prokofiev.pharmacy.model;

/**
 * DTO для отчёта «Лекарства с минимальным запасом (общий)».
 */
public class MinStockDto {
    private Long medicamentId;
    private String medicamentName;
    private Integer availableAmount;
    private Integer minimalOverall;
    private Long countAtMinimum;

    public MinStockDto() { }

    public MinStockDto(Long medicamentId, String medicamentName, Integer availableAmount,
                       Integer minimalOverall, Long countAtMinimum) {
        this.medicamentId    = medicamentId;
        this.medicamentName  = medicamentName;
        this.availableAmount = availableAmount;
        this.minimalOverall  = minimalOverall;
        this.countAtMinimum  = countAtMinimum;
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

    public Integer getMinimalOverall() {
        return minimalOverall;
    }
    public void setMinimalOverall(Integer minimalOverall) {
        this.minimalOverall = minimalOverall;
    }

    public Long getCountAtMinimum() {
        return countAtMinimum;
    }
    public void setCountAtMinimum(Long countAtMinimum) {
        this.countAtMinimum = countAtMinimum;
    }
}
