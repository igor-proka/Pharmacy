// src/main/java/ru/nsu/prokofiev/pharmacy/model/RequiredDrugDto.java
package ru.nsu.prokofiev.pharmacy.model;

import java.math.BigInteger;

/**
 * DTO для отчёта «Препараты, требуемые для заказов в производстве».
 */
public class RequiredDrugDto {
    private Long medicamentId;
    private String medicamentName;
    private BigInteger totalRequiredPacks;
    private Long totalRequiredMedications;

    public RequiredDrugDto() { }

    public RequiredDrugDto(Long medicamentId, String medicamentName,
                           BigInteger totalRequiredPacks, Long totalRequiredMedications) {
        this.medicamentId             = medicamentId;
        this.medicamentName           = medicamentName;
        this.totalRequiredPacks       = totalRequiredPacks;
        this.totalRequiredMedications = totalRequiredMedications;
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

    public BigInteger getTotalRequiredPacks() {
        return totalRequiredPacks;
    }
    public void setTotalRequiredPacks(BigInteger totalRequiredPacks) {
        this.totalRequiredPacks = totalRequiredPacks;
    }

    public Long getTotalRequiredMedications() {
        return totalRequiredMedications;
    }
    public void setTotalRequiredMedications(Long totalRequiredMedications) {
        this.totalRequiredMedications = totalRequiredMedications;
    }
}
