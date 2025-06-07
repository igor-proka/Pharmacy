// src/main/java/ru/nsu/prokofiev/pharmacy/repository/FrequentCustomerProjection.java
package ru.nsu.prokofiev.pharmacy.repository;

import java.math.BigInteger;

/**
 * Projection-интерфейс для отчёта «Часто делающие заказы клиенты».
 */
public interface FrequentCustomerProjection {
    Long getCustomerId();
    String getCustomerName();
    String getPhoneNumber();
    String getAddress();
    Integer getOrdersCount();
    BigInteger getTotalCustomers();
}
