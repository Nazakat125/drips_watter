package com.example.drips_watter;

public class UserOrderListData {
    private String image;
    private String name;
    private String key;
    private String number;

    // Default constructor required for calls to DataSnapshot.getValue(UserOrderListData.class)
    public UserOrderListData() {}

    public UserOrderListData(String image, String name, String key, String number) {
        this.image = image;
        this.name = name;
        this.key = key;
        this.number = number;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
