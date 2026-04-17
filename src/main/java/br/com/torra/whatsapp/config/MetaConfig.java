package br.com.torra.whatsapp.config;

public class MetaConfig {

    private final String apiVersion;
    private final String phoneNumberId;
    private final String accessToken;
    private final String toPhone;
    private final String templateName;
    private final String templateLanguage;

    public MetaConfig(
            String apiVersion,
            String phoneNumberId,
            String accessToken,
            String toPhone,
            String templateName,
            String templateLanguage
    ) {
        this.apiVersion = apiVersion;
        this.phoneNumberId = phoneNumberId;
        this.accessToken = accessToken;
        this.toPhone = toPhone;
        this.templateName = templateName;
        this.templateLanguage = templateLanguage;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getPhoneNumberId() {
        return phoneNumberId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getToPhone() {
        return toPhone;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getTemplateLanguage() {
        return templateLanguage;
    }
}