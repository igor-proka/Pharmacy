// src/main/java/ru/nsu/prokofiev/pharmacy/repository/TopUsedDrugProjection.java
package ru.nsu.prokofiev.pharmacy.repository;

import java.math.BigInteger;

/**
 * Projection-интерфейс для отчёта «10 наиболее часто используемых медикаментов».
 */
public interface TopUsedDrugProjection {
    Long getMedicamentId();
    String getMedicamentName();
    BigInteger getUsageScore();
}
