// src/main/java/ru/nsu/prokofiev/pharmacy/model/WaitingCustomerDto.java
package ru.nsu.prokofiev.pharmacy.model;

/**
 * DTO для отчёта «Ожидающие поступления медикаментов».
 */
public class WaitingCustomerDto {
    private Long   customerId;
    private String customerName;
    private String phoneNumber;
    private String address;
    private Long   packageId;        // может быть null
    private String medicamentName;   // может быть null
    private Integer waitingAmount;   // может быть null
    private Long   totalWaiting;

    public WaitingCustomerDto() { }

    public WaitingCustomerDto(Long customerId, String customerName, String phoneNumber,
                              String address, Long packageId, String medicamentName,
                              Integer waitingAmount, Long totalWaiting) {
        this.customerId      = customerId;
        this.customerName    = customerName;
        this.phoneNumber     = phoneNumber;
        this.address         = address;
        this.packageId       = packageId;
        this.medicamentName  = medicamentName;
        this.waitingAmount   = waitingAmount;
        this.totalWaiting    = totalWaiting;
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

    public Long getPackageId() {
        return packageId;
    }
    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getMedicamentName() {
        return medicamentName;
    }
    public void setMedicamentName(String medicamentName) {
        this.medicamentName = medicamentName;
    }

    public Integer getWaitingAmount() {
        return waitingAmount;
    }
    public void setWaitingAmount(Integer waitingAmount) {
        this.waitingAmount = waitingAmount;
    }

    public Long getTotalWaiting() {
        return totalWaiting;
    }
    public void setTotalWaiting(Long totalWaiting) {
        this.totalWaiting = totalWaiting;
    }
}
