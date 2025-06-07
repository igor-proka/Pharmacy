// src/main/java/ru/nsu/prokofiev/pharmacy/repository/MinStockProjection.java
package ru.nsu.prokofiev.pharmacy.repository;

import java.math.BigInteger;

/**
 * Projection-интерфейс для отчёта «Лекарства с минимальным запасом (общий)».
 */
public interface MinStockProjection {
    Long getMedicamentId();
    String getMedicamentName();
    Integer getAvailableAmount();
    Integer getMinimalOverall();
    BigInteger getCountAtMinimum();
}
