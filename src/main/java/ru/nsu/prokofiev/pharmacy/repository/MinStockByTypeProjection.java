// src/main/java/ru/nsu/prokofiev/pharmacy/repository/MinStockByTypeProjection.java
package ru.nsu.prokofiev.pharmacy.repository;

import java.math.BigInteger;

/**
 * Projection-интерфейс для отчёта «Лекарства с минимальным запасом (по типу)».
 */
public interface MinStockByTypeProjection {
    Long getMedicamentId();
    String getMedicamentName();
    Integer getAvailableAmount();
    Integer getMinimalInType();
    BigInteger getCountAtMinimumInType();
}
