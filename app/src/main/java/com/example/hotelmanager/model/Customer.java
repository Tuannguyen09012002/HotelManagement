package com.example.hotelmanager.model;

public class Customer {
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String customerCCCD;
    private String customerBirthday;

    public Customer(String customerId, String customerName, String customerAddress, String customerPhone, String customerCCCD, String customerBirthday) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
        this.customerCCCD = customerCCCD;
        this.customerBirthday = customerBirthday;
    }

    public Customer() {
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerCCCD() {
        return customerCCCD;
    }

    public void setCustomerCCCD(String customerCCCD) {
        this.customerCCCD = customerCCCD;
    }

    public String getCustomerBirthday() {
        return customerBirthday;
    }

    public void setCustomerBirthday(String customerBirthday) {
        this.customerBirthday = customerBirthday;
    }
}
