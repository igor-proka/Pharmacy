// src/main/java/ru/nsu/prokofiev/pharmacy/model/CriticalMedicationDto.java
package ru.nsu.prokofiev.pharmacy.model;

/**
 * DTO для отчёта «Лекарства на критическом уровне или закончились».
 */
public class CriticalMedicationDto {
    private Long medicamentId;
    private String medicamentName;
    private String medicamentType;
    private Integer availableAmount;
    private Integer criticalAmount;
    private Long totalFlaggedMedications;

    public CriticalMedicationDto() { }

    public CriticalMedicationDto(Long medicamentId, String medicamentName, String medicamentType,
                                 Integer availableAmount, Integer criticalAmount, Long totalFlaggedMedications) {
        this.medicamentId            = medicamentId;
        this.medicamentName          = medicamentName;
        this.medicamentType          = medicamentType;
        this.availableAmount         = availableAmount;
        this.criticalAmount          = criticalAmount;
        this.totalFlaggedMedications = totalFlaggedMedications;
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

    public String getMedicamentType() {
        return medicamentType;
    }
    public void setMedicamentType(String medicamentType) {
        this.medicamentType = medicamentType;
    }

    public Integer getAvailableAmount() {
        return availableAmount;
    }
    public void setAvailableAmount(Integer availableAmount) {
        this.availableAmount = availableAmount;
    }

    public Integer getCriticalAmount() {
        return criticalAmount;
    }
    public void setCriticalAmount(Integer criticalAmount) {
        this.criticalAmount = criticalAmount;
    }

    public Long getTotalFlaggedMedications() {
        return totalFlaggedMedications;
    }
    public void setTotalFlaggedMedications(Long totalFlaggedMedications) {
        this.totalFlaggedMedications = totalFlaggedMedications;
    }
}
