// src/main/java/ru/nsu/prokofiev/pharmacy/model/UsedVolumeDto.java
package ru.nsu.prokofiev.pharmacy.model;

/**
 * DTO для отчёта «Объём использованных пачек за период».
 */
public class UsedVolumeDto {
    private Long medicamentId;
    private String medicamentName;
    private Long totalPackagesUsed;

    public UsedVolumeDto() { }

    public UsedVolumeDto(Long medicamentId, String medicamentName, Long totalPackagesUsed) {
        this.medicamentId       = medicamentId;
        this.medicamentName     = medicamentName;
        this.totalPackagesUsed  = totalPackagesUsed;
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

    public Long getTotalPackagesUsed() {
        return totalPackagesUsed;
    }
    public void setTotalPackagesUsed(Long totalPackagesUsed) {
        this.totalPackagesUsed = totalPackagesUsed;
    }
}
