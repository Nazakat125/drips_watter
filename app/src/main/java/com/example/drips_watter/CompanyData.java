package com.example.drips_watter;

public class CompanyData {
    private String name;
    private String logoUrl;
    private String phone;

    // Empty constructor required for Firebase
    public CompanyData() {
    }

    public CompanyData(String name, String logoUrl,String phone) {
        this.name = name;
        this.logoUrl = logoUrl;
        this.phone = phone;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String logoUrl) {
        this.phone = phone;
    }
}
