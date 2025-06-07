// src/main/java/ru/nsu/prokofiev/pharmacy/repository/InProductionOrderProjection.java
package ru.nsu.prokofiev.pharmacy.repository;

import java.sql.Timestamp;
import java.math.BigInteger;

/**
 * Projection-интерфейс для отчёта «Заказы в производстве».
 */
public interface InProductionOrderProjection {
    Long getOrderId();
    Timestamp getRegistrationDatetime();
    Long getCustomerId();
    String getCustomerName();
    Long getProductionId();
    Timestamp getStartTime();
    BigInteger getTotalOrdersInProduction();
}
