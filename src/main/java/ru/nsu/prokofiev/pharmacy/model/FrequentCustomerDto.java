// src/main/java/ru/nsu/prokofiev/pharmacy/model/FrequentCustomerDto.java
package ru.nsu.prokofiev.pharmacy.model;

/**
 * DTO для отчёта «Часто делающие заказы клиенты».
 */
public class FrequentCustomerDto {
    private Long customerId;
    private String customerName;
    private String phoneNumber;
    private String address;
    private Integer ordersCount;
    private Long totalCustomers;

    public FrequentCustomerDto() { }

    public FrequentCustomerDto(Long customerId, String customerName, String phoneNumber,
                               String address, Integer ordersCount, Long totalCustomers) {
        this.customerId     = customerId;
        this.customerName   = customerName;
        this.phoneNumber    = phoneNumber;
        this.address        = address;
        this.ordersCount    = ordersCount;
        this.totalCustomers = totalCustomers;
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

    public Integer getOrdersCount() {
        return ordersCount;
    }
    public void setOrdersCount(Integer ordersCount) {
        this.ordersCount = ordersCount;
    }

    public Long getTotalCustomers() {
        return totalCustomers;
    }
    public void setTotalCustomers(Long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
}
