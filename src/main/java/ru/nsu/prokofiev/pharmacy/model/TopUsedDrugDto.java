// src/main/java/ru/nsu/prokofiev/pharmacy/model/TopUsedDrugDto.java
package ru.nsu.prokofiev.pharmacy.model;

/**
 * DTO для отчёта «Топ-10 наиболее часто используемых медикаментов».
 */
public class TopUsedDrugDto {
    private Long medicamentId;
    private String medicamentName;
    private Long usageScore;

    public TopUsedDrugDto() { }

    public TopUsedDrugDto(Long medicamentId, String medicamentName, Long usageScore) {
        this.medicamentId   = medicamentId;
        this.medicamentName = medicamentName;
        this.usageScore     = usageScore;
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

    public Long getUsageScore() {
        return usageScore;
    }
    public void setUsageScore(Long usageScore) {
        this.usageScore = usageScore;
    }
}
