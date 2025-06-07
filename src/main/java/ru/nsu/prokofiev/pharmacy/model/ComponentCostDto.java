// src/main/java/ru/nsu/prokofiev/pharmacy/model/ComponentCostDto.java
package ru.nsu.prokofiev.pharmacy.model;

import java.math.BigDecimal;

/**
 * DTO для отчёта «Стоимость компонентов для изготовления пакета».
 */
public class ComponentCostDto {
    private Long technologyId;
    private Long componentPackageId;
    private String componentMedicamentName;
    private Integer componentAmount;
    private BigDecimal componentPrice;
    private BigDecimal componentTotalCost;
    private Integer producedPackages;
    private BigDecimal costPerOnePackage;

    public ComponentCostDto() { }

    public ComponentCostDto(Long technologyId, Long componentPackageId, String componentMedicamentName,
                            Integer componentAmount, BigDecimal componentPrice, BigDecimal componentTotalCost,
                            Integer producedPackages, BigDecimal costPerOnePackage) {
        this.technologyId            = technologyId;
        this.componentPackageId      = componentPackageId;
        this.componentMedicamentName = componentMedicamentName;
        this.componentAmount         = componentAmount;
        this.componentPrice          = componentPrice;
        this.componentTotalCost      = componentTotalCost;
        this.producedPackages        = producedPackages;
        this.costPerOnePackage       = costPerOnePackage;
    }

    public Long getTechnologyId() {
        return technologyId;
    }
    public void setTechnologyId(Long technologyId) {
        this.technologyId = technologyId;
    }

    public Long getComponentPackageId() {
        return componentPackageId;
    }
    public void setComponentPackageId(Long componentPackageId) {
        this.componentPackageId = componentPackageId;
    }

    public String getComponentMedicamentName() {
        return componentMedicamentName;
    }
    public void setComponentMedicamentName(String componentMedicamentName) {
        this.componentMedicamentName = componentMedicamentName;
    }

    public Integer getComponentAmount() {
        return componentAmount;
    }
    public void setComponentAmount(Integer componentAmount) {
        this.componentAmount = componentAmount;
    }

    public BigDecimal getComponentPrice() {
        return componentPrice;
    }
    public void setComponentPrice(BigDecimal componentPrice) {
        this.componentPrice = componentPrice;
    }

    public BigDecimal getComponentTotalCost() {
        return componentTotalCost;
    }
    public void setComponentTotalCost(BigDecimal componentTotalCost) {
        this.componentTotalCost = componentTotalCost;
    }

    public Integer getProducedPackages() {
        return producedPackages;
    }
    public void setProducedPackages(Integer producedPackages) {
        this.producedPackages = producedPackages;
    }

    public BigDecimal getCostPerOnePackage() {
        return costPerOnePackage;
    }
    public void setCostPerOnePackage(BigDecimal costPerOnePackage) {
        this.costPerOnePackage = costPerOnePackage;
    }
}
