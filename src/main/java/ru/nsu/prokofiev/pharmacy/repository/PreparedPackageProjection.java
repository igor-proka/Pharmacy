// src/main/java/ru/nsu/prokofiev/pharmacy/repository/PreparedPackageProjection.java
package ru.nsu.prokofiev.pharmacy.repository;

/**
 * Projection-интерфейс для отчёта «Цена готового лекарства (упаковка)».
 */
public interface PreparedPackageProjection {
    Long getPackageId();
    String getMedicamentName();
    java.math.BigDecimal getPrice();
}
