package com.torra.whatsapp.config;

public class ZApiConfig {
    private final String productId;
    private final String phoneId;
    private final String token;
    private final String phone;

    public ZApiConfig(String productId, String phoneId, String token, String phone) {
        this.productId = productId;
        this.phoneId = phoneId;
        this.token = token;
        this.phone = phone;
    }

    public String getProductId() {
        return productId;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public String getToken() {
        return token;
    }

    public String getPhone() {
        return phone;
    }
}