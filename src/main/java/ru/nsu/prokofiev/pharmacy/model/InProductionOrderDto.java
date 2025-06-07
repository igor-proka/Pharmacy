// src/main/java/ru/nsu/prokofiev/pharmacy/model/InProductionOrderDto.java
package ru.nsu.prokofiev.pharmacy.model;

import java.time.LocalDateTime;

/**
 * DTO для отчёта «Заказы в производстве».
 */
public class InProductionOrderDto {
    private Long orderId;
    private LocalDateTime registrationDatetime;
    private Long customerId;
    private String customerName;
    private Long productionId;
    private LocalDateTime startTime;
    private Long totalOrdersInProduction;

    public InProductionOrderDto() { }

    public InProductionOrderDto(Long orderId, LocalDateTime registrationDatetime,
                                Long customerId, String customerName,
                                Long productionId, LocalDateTime startTime,
                                Long totalOrdersInProduction) {
        this.orderId                  = orderId;
        this.registrationDatetime     = registrationDatetime;
        this.customerId               = customerId;
        this.customerName             = customerName;
        this.productionId             = productionId;
        this.startTime                = startTime;
        this.totalOrdersInProduction  = totalOrdersInProduction;
    }

    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getRegistrationDatetime() {
        return registrationDatetime;
    }
    public void setRegistrationDatetime(LocalDateTime registrationDatetime) {
        this.registrationDatetime = registrationDatetime;
    }

    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getProductionId() {
        return productionId;
    }
    public void setProductionId(Long productionId) {
        this.productionId = productionId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Long getTotalOrdersInProduction() {
        return totalOrdersInProduction;
    }
    public void setTotalOrdersInProduction(Long totalOrdersInProduction) {
        this.totalOrdersInProduction = totalOrdersInProduction;
    }
}
