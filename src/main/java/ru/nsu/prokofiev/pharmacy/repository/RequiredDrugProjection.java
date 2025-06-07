// src/main/java/ru/nsu/prokofiev/pharmacy/repository/RequiredDrugProjection.java
package ru.nsu.prokofiev.pharmacy.repository;

import java.math.BigInteger;

/**
 * Projection-интерфейс для отчёта «Препараты, требуемые для заказов в производстве».
 */
public interface RequiredDrugProjection {
    Long getMedicamentId();
    String getMedicamentName();
    BigInteger getTotalRequiredPacks();
    BigInteger getTotalRequiredMedications();
}
