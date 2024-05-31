package com.example.drips_watter;

public class DeliveryBoyOrdersData {
    private String image;
    private String name;
    private String number;
    private String address;
    private String liters;
    private String key;
    private String readyForDelivery;
    private String completed;
    private String companyPhone;
    private String companyToken;
    private String customerToken;
    private String deliveryKey;

    public DeliveryBoyOrdersData(String image, String name, String number, String address, String liters, String key, String readyForDelivery, String completed, String companyPhone, String companyToken, String customerToken,String deliveryKey) {
        this.image = image;
        this.name = name;
        this.number = number;
        this.address = address;
        this.liters = liters;
        this.key = key;
        this.readyForDelivery = readyForDelivery;
        this.completed = completed;
        this.companyPhone = companyPhone;
        this.companyToken = companyToken;
        this.customerToken = customerToken;
        this.deliveryKey = deliveryKey;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getAddress() {
        return address;
    }

    public String getLiters() {
        return liters;
    }

    public String getKey() {
        return key;
    }

    public String getReadyForDelivery() {
        return readyForDelivery;
    }

    public String getCompleted() {
        return completed;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyToken() {
        return companyToken;
    }

    public void setCompanyToken(String companyToken) {
        this.companyToken = companyToken;
    }

    public String getCustomerToken() {
        return customerToken;
    }

    public void setCustomerToken(String customerToken) {
        this.customerToken = customerToken;
    }
    public String getDeliveryKey() {
        return deliveryKey;
    }

    public void setDeliveryKey(String deliveryKey) {
        this.deliveryKey = deliveryKey;
    }
}
