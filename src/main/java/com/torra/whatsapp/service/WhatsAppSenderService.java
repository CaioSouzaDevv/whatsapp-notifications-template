package com.torra.whatsapp.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import com.torra.whatsapp.config.ZApiConfig;

public class WhatsAppSenderService {

        private final HttpClient httpClient;

        public WhatsAppSenderService() {
                this.httpClient = HttpClient.newHttpClient();
        }

        public String sendText(ZApiConfig config, String message)
                        throws IOException, InterruptedException {

                String json = "{\"to_number\":\"" + config.getPhone() + "\","
                                + "\"type\":\"text\","
                                + "\"message\":\"" + message + "\"}";

                String url = "https://api.maytapi.com/api/%s/%s/sendMessage"
                                .formatted(config.getProductId(), config.getPhoneId());

                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .header("Content-Type", "application/json")
                                .header("Accept", "application/json")
                                .header("x-maytapi-key", config.getToken())
                                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                                .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                return "Status: " + response.statusCode() + "\nResposta: " + response.body();
        }

        public String sendImage(ZApiConfig config, String imageBase64, String caption)
                        throws IOException, InterruptedException {

                String json = "{\"to_number\":\"" + config.getPhone() + "\","
                                + "\"type\":\"media\","
                                + "\"message\":\"" + imageBase64 + "\","
                                + "\"text\":\"" + caption + "\"}";

                String url = "https://api.maytapi.com/api/%s/%s/sendMessage"
                                .formatted(config.getProductId(), config.getPhoneId());

                System.out.println("URL: " + url);
                System.out.println("PHONE: " + config.getPhone());
                System.out.println("BASE64 PREFIX: " + imageBase64.substring(0, 40));
                System.out.println("BASE64 LENGTH: " + imageBase64.length());
                System.out.println("CAPTION: " + caption);

                HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .header("Content-Type", "application/json")
                                .header("Accept", "application/json")
                                .header("x-maytapi-key", config.getToken())
                                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                                .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                return "Status: " + response.statusCode() + "\nResposta: " + response.body();
        }
}