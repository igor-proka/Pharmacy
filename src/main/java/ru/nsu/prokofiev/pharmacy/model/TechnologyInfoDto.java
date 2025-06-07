// src/main/java/ru/nsu/prokofiev/pharmacy/model/TechnologyInfoDto.java
package ru.nsu.prokofiev.pharmacy.model;

/**
 * DTO для отчёта «Информация о технологиях приготовления».
 */
public class TechnologyInfoDto {
    private Long technologyId;
    private String preparationMethod;
    private Long packageId;
    private String medicamentName;
    private String medicamentType;
    private Integer prepTimeMinutes;
    private Integer amountPerBatch;

    public TechnologyInfoDto() { }

    public TechnologyInfoDto(Long technologyId, String preparationMethod, Long packageId,
                             String medicamentName, String medicamentType,
                             Integer prepTimeMinutes, Integer amountPerBatch) {
        this.technologyId        = technologyId;
        this.preparationMethod   = preparationMethod;
        this.packageId           = packageId;
        this.medicamentName      = medicamentName;
        this.medicamentType      = medicamentType;
        this.prepTimeMinutes     = prepTimeMinutes;
        this.amountPerBatch      = amountPerBatch;
    }

    public Long getTechnologyId() {
        return technologyId;
    }
    public void setTechnologyId(Long technologyId) {
        this.technologyId = technologyId;
    }

    public String getPreparationMethod() {
        return preparationMethod;
    }
    public void setPreparationMethod(String preparationMethod) {
        this.preparationMethod = preparationMethod;
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

    public String getMedicamentType() {
        return medicamentType;
    }
    public void setMedicamentType(String medicamentType) {
        this.medicamentType = medicamentType;
    }

    public Integer getPrepTimeMinutes() {
        return prepTimeMinutes;
    }
    public void setPrepTimeMinutes(Integer prepTimeMinutes) {
        this.prepTimeMinutes = prepTimeMinutes;
    }

    public Integer getAmountPerBatch() {
        return amountPerBatch;
    }
    public void setAmountPerBatch(Integer amountPerBatch) {
        this.amountPerBatch = amountPerBatch;
    }
}
