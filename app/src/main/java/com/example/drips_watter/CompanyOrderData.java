package com.example.drips_watter;

// CompanyOrderData.java
public class CompanyOrderData {
    private String image;
    private String name;
    private String number;
    private String totalBill;
    private String totalLiter;
    private String address;
    private String key;
    private String token;
    private String accepted;

    public CompanyOrderData(String image, String name, String number, String totalBill, String totalLiter, String address,String key,String token,String accepted) {
        this.image = image;
        this.name = name;
        this.number = number;
        this.totalBill = totalBill;
        this.totalLiter = totalLiter;
        this.address = address;
        this.key = key;
        this.token = token;
        this.accepted = accepted;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(String totalBill) {
        this.totalBill = totalBill;
    }

    public String getTotalLiter() {
        return totalLiter;
    }

    public void setTotalLiter(String totalLiter) {
        this.totalLiter = totalLiter;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }
}
