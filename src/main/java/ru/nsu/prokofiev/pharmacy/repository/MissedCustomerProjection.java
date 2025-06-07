// src/main/java/ru/nsu/prokofiev/pharmacy/repository/MissedCustomerProjection.java
package ru.nsu.prokofiev.pharmacy.repository;

/**
 * Projection-интерфейс для отчёта «Просроченные клиенты».
 */
public interface MissedCustomerProjection {
    Long getCustomerId();
    String getFullName();
    String getPhoneNumber();
    String getAddress();
    Long getOrderId();
    java.sql.Timestamp getAppointedDatetime();
}
