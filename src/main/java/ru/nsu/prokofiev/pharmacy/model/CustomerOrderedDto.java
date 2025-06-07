// src/main/java/ru/nsu/prokofiev/pharmacy/model/CustomerOrderedDto.java
package ru.nsu.prokofiev.pharmacy.model;

/**
 * DTO для отчёта «Клиенты, заказывавшие лекарства».
 */
public class CustomerOrderedDto {
    private Long customerId;
    private String customerName;
    private String phoneNumber;
    private String address;
    private Long totalCustomers;

    public CustomerOrderedDto() { }

    public CustomerOrderedDto(Long customerId, String customerName, String phoneNumber,
                              String address, Long totalCustomers) {
        this.customerId      = customerId;
        this.customerName    = customerName;
        this.phoneNumber     = phoneNumber;
        this.address         = address;
        this.totalCustomers  = totalCustomers;
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

    public Long getTotalCustomers() {
        return totalCustomers;
    }
    public void setTotalCustomers(Long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
}
