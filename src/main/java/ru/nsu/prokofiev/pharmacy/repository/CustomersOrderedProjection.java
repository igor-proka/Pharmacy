// src/main/java/ru/nsu/prokofiev/pharmacy/repository/CustomersOrderedProjection.java
package ru.nsu.prokofiev.pharmacy.repository;

import java.math.BigInteger;

/**
 * Projection-интерфейс для отчёта «Клиенты, заказывавшие лекарства».
 */
public interface CustomersOrderedProjection {
    Long getCustomerId();
    String getCustomerName();
    String getPhoneNumber();
    String getAddress();
    BigInteger getTotalCustomers();
}
