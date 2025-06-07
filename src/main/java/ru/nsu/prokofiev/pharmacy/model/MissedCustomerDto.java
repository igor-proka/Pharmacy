// src/main/java/ru/nsu/prokofiev/pharmacy/model/MissedCustomerDto.java
package ru.nsu.prokofiev.pharmacy.model;

import java.time.LocalDateTime;

/**
 * DTO для отчёта «Просроченные клиенты».
 */
public class MissedCustomerDto {
    private Long customerId;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Long orderId;
    private LocalDateTime appointedDatetime;

    public MissedCustomerDto() { }

    public MissedCustomerDto(Long customerId, String fullName, String phoneNumber,
                             String address, Long orderId, LocalDateTime appointedDatetime) {
        this.customerId        = customerId;
        this.fullName          = fullName;
        this.phoneNumber       = phoneNumber;
        this.address           = address;
        this.orderId           = orderId;
        this.appointedDatetime = appointedDatetime;
    }

    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getAppointedDatetime() {
        return appointedDatetime;
    }
    public void setAppointedDatetime(LocalDateTime appointedDatetime) {
        this.appointedDatetime = appointedDatetime;
    }
}
