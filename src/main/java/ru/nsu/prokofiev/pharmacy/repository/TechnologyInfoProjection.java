// src/main/java/ru/nsu/prokofiev/pharmacy/repository/TechnologyInfoProjection.java
package ru.nsu.prokofiev.pharmacy.repository;

/**
 * Projection-интерфейс для отчёта «Информация о технологиях приготовления».
 */
public interface TechnologyInfoProjection {
    Long getTechnologyId();
    String getPreparationMethod();
    Long getPackageId();
    String getMedicamentName();
    String getMedicamentType();
    Integer getPrepTimeMinutes();
    Integer getAmountPerBatch();
}
