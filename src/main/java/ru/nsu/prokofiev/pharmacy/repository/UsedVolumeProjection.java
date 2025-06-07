// src/main/java/ru/nsu/prokofiev/pharmacy/repository/UsedVolumeProjection.java
package ru.nsu.prokofiev.pharmacy.repository;

import java.math.BigInteger;

/**
 * Projection-интерфейс для отчёта «Объём использованных пачек за период».
 */
public interface UsedVolumeProjection {
    Long getMedicamentId();
    String getMedicamentName();
    BigInteger getTotalPackagesUsed();
}
