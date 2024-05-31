package com.example.drips_watter;

public class CompanyDeliveryBoyData {
    private String image;
    private String name;
    private String number;
    private String address;
    private String age;
    private String vehicle;
    private String currentAddress;
    private double longitude;
    private double latitude;

    public CompanyDeliveryBoyData(String image, String name, String number, String address, String age, String vehicle, String currentAddress, double longitude, double latitude) {
        this.image = image;
        this.name = name;
        this.number = number;
        this.address = address;
        this.age = age;
        this.vehicle = vehicle;
        this.currentAddress = currentAddress;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // Getters and setters
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
