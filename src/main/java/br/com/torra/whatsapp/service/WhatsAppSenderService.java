package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.util.LogUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static br.com.torra.whatsapp.config.AppConfig.META_ACCESS_TOKEN;
import static br.com.torra.whatsapp.config.AppConfig.META_API_VERSION;
import static br.com.torra.whatsapp.config.AppConfig.META_PHONE_NUMBER_ID;

public class WhatsAppSenderService {

    private final HttpClient client = HttpClient.newHttpClient();

    // ===================== TEMPLATE =====================
    public void sendTemplate(String phone) {
        try {
            String url = "https://graph.facebook.com/"
                    + META_API_VERSION + "/"
                    + META_PHONE_NUMBER_ID + "/messages";

            String body = "{"
                    + "\"messaging_product\":\"whatsapp\","
                    + "\"to\":\"" + phone + "\","
                    + "\"type\":\"template\","
                    + "\"template\":{"
                    + "\"name\":\"dados_hora\","
                    + "\"language\":{\"code\":\"pt_BR\"},"
                    + "\"components\":["
                    + "{"
                    + "\"type\":\"body\","
                    + "\"parameters\":["
                    + "{\"type\":\"text\",\"text\":\"Caio\"},"
                    + "{\"type\":\"text\",\"text\":\"04/05/2026\"}"
                    + "]"
                    + "}"
                    + "]"
                    + "}"
                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + META_ACCESS_TOKEN)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            LogUtil.info("Enviando template...");

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            LogUtil.info("Template Status: " + response.statusCode());
            LogUtil.info("Template Response: " + response.body());

        } catch (Exception e) {
            LogUtil.error("Erro ao enviar template", e);
        }
    }

    // ===================== IMAGE =====================
    public void sendImage(String phone, BufferedImage image) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);

            String mediaId = uploadImage(baos.toByteArray());

            String url = "https://graph.facebook.com/"
                    + META_API_VERSION + "/"
                    + META_PHONE_NUMBER_ID + "/messages";

            String body = "{"
                    + "\"messaging_product\":\"whatsapp\","
                    + "\"to\":\"" + phone + "\","
                    + "\"type\":\"image\","
                    + "\"image\":{\"id\":\"" + mediaId + "\"}"
                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + META_ACCESS_TOKEN)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            LogUtil.info("Enviando imagem para WhatsApp...");

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            LogUtil.info("Send Status: " + response.statusCode());
            LogUtil.info("Send Response: " + response.body());

        } catch (Exception e) {
            LogUtil.error("Erro ao enviar imagem", e);
        }
    }

    private String uploadImage(byte[] imageBytes) throws Exception {

        String url = "https://graph.facebook.com/"
                + META_API_VERSION + "/"
                + META_PHONE_NUMBER_ID + "/media";

        String boundary = "----JavaBoundary";

        String bodyStart = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"messaging_product\"\r\n\r\n"
                + "whatsapp\r\n"
                + "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"file\"; filename=\"image.png\"\r\n"
                + "Content-Type: image/png\r\n\r\n";

        String bodyEnd = "\r\n--" + boundary + "--";

        byte[] bodyBytesStart = bodyStart.getBytes();
        byte[] bodyBytesEnd = bodyEnd.getBytes();

        byte[] finalBody = new byte[bodyBytesStart.length + imageBytes.length + bodyBytesEnd.length];

        System.arraycopy(bodyBytesStart, 0, finalBody, 0, bodyBytesStart.length);
        System.arraycopy(imageBytes, 0, finalBody, bodyBytesStart.length, imageBytes.length);
        System.arraycopy(bodyBytesEnd, 0, finalBody, bodyBytesStart.length + imageBytes.length, bodyBytesEnd.length);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + META_ACCESS_TOKEN)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(finalBody))
                .build();

        LogUtil.info("Fazendo upload da imagem...");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        LogUtil.info("Upload Status: " + response.statusCode());
        LogUtil.info("Upload Response: " + response.body());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Erro no upload da imagem");
        }

        return response.body().split("\"id\":\"")[1].split("\"")[0];
    }
}