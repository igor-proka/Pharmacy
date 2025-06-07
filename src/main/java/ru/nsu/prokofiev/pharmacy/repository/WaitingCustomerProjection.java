// src/main/java/ru/nsu/prokofiev/pharmacy/repository/WaitingCustomerProjection.java
package ru.nsu.prokofiev.pharmacy.repository;

import java.math.BigInteger;

/**
 * Projection-интерфейс для отчёта «Ожидающие поступления медикаментов».
 */
public interface WaitingCustomerProjection {
    Long getCustomerId();
    String getCustomerName();
    String getPhoneNumber();
    String getAddress();
    Long getPackageId();               // может быть null, если без детализации
    String getMedicamentName();        // может быть null
    Integer getWaitingAmount();        // может быть null
    BigInteger getTotalWaitingCustomers();
}
