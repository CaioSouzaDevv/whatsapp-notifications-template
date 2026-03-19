package com.torra.whatsapp.config;

public class ZApiConfig {
    private final String instanceId;
    private final String token;
    private final String clientToken;
    private final String phone;

    public ZApiConfig(String instanceId, String token, String clientToken, String phone) {
        this.instanceId = instanceId;
        this.token = token;
        this.clientToken = clientToken;
        this.phone = phone;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getToken() {
        return token;
    }

    public String getClientToken() {
        return clientToken;
    }

    public String getPhone() {
        return phone;
    }
}