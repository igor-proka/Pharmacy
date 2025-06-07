// src/main/java/ru/nsu/prokofiev/pharmacy/repository/CriticalMedicationProjection.java
package ru.nsu.prokofiev.pharmacy.repository;

import java.math.BigInteger;

/**
 * Projection-интерфейс для отчёта «Лекарства на критическом уровне или закончились».
 */
public interface CriticalMedicationProjection {
    Long getMedicamentId();
    String getMedicamentName();
    String getMedicamentType();
    Integer getAvailableAmount();
    Integer getCriticalAmount();
    BigInteger getTotalFlaggedMedications();
}
