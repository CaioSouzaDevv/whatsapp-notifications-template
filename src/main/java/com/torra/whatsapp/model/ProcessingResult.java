package com.torra.whatsapp.model;

public class ProcessingResult {

    private final String storeName;
    private final String phone;
    private final boolean success;
    private final String message;
    private final long executionTimeMs;

    public ProcessingResult(
            String storeName,
            String phone,
            boolean success,
            String message,
            long executionTimeMs
    ) {
        this.storeName = storeName;
        this.phone = phone;
        this.success = success;
        this.message = message;
        this.executionTimeMs = executionTimeMs;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }
}