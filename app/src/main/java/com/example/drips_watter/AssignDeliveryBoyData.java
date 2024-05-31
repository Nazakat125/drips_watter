package com.example.drips_watter;

public class AssignDeliveryBoyData {
    private String image;
    private String name;
    private String number;
    private String age;
    private String vehicle;
    private String address;
    private String currentAddress;
    private String token;
    private boolean selected;

    public AssignDeliveryBoyData(String image, String name, String number, String age, String vehicle, String address, String currentAddress, boolean isSelected,String token) {
        this.image = image;
        this.name = name;
        this.number = number;
        this.age = age;
        this.vehicle = vehicle;
        this.address = address;
        this.currentAddress = currentAddress;
        this.selected = false;
        this.token = token;
    }

    // Getters and setters
    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getAge() {
        return age;
    }

    public String getVehicle() {
        return vehicle;
    }

    public String getAddress() {
        return address;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public String getToken() {
        return token;
    }
}
