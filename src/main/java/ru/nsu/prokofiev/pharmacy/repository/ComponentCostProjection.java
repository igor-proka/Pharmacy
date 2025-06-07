// src/main/java/ru/nsu/prokofiev/pharmacy/repository/ComponentCostProjection.java
package ru.nsu.prokofiev.pharmacy.repository;

import java.math.BigDecimal;

/**
 * Projection-интерфейс для отчёта «Стоимость компонентов для изготовления пакета».
 */
public interface ComponentCostProjection {
    Long getTechnologyId();
    Long getComponentPackageId();
    String getComponentMedicamentName();
    Integer getComponentAmount();
    BigDecimal getComponentPrice();
    BigDecimal getComponentTotalCost();
    Integer getProducedPackages();
    BigDecimal getCostPerOnePackage();
}
