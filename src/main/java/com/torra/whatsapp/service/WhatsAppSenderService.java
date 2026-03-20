package com.torra.whatsapp.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.torra.whatsapp.config.ZApiConfig;

public class WhatsAppSenderService {

    private final HttpClient httpClient;

    public WhatsAppSenderService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public String sendImage(ZApiConfig config, String imageBase64, String caption)
            throws IOException, InterruptedException {

        String json = """
                {
                  "phone": "%s",
                  "image": "%s",
                  "caption": "%s"
                }
                """.formatted(config.getPhone(), imageBase64, caption);

        String url = "https://api.z-api.io/instances/%s/token/%s/send-image"
                .formatted(config.getInstanceId(), config.getToken());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Client-Token", config.getClientToken())
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return "Status: " + response.statusCode() + "\nResposta: " + response.body();
    }
}