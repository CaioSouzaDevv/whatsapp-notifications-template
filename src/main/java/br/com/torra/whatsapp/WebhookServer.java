package br.com.torra.whatsapp;

import br.com.torra.whatsapp.config.AppConfig;
import br.com.torra.whatsapp.service.DashboardProcessorService;
import br.com.torra.whatsapp.service.WhatsAppSenderService;
import br.com.torra.whatsapp.util.LogUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class WebhookServer {

    private static final int PORT = 8080;
    private static final String VERIFY_TOKEN = "teste123";

    public static void main(String[] args) throws Exception {
        start();
    }

    public static void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/webhook", new WebhookHandler());
        server.setExecutor(null);
        server.start();

        LogUtil.info("Webhook rodando na porta " + PORT);
    }

    static class WebhookHandler implements HttpHandler {

        private final DashboardProcessorService dashboardProcessorService;
        private final WhatsAppSenderService whatsAppSenderService;

        public WebhookHandler() {
            this.dashboardProcessorService = new DashboardProcessorService();
            this.whatsAppSenderService = new WhatsAppSenderService();
        }

        @Override
        public void handle(HttpExchange exchange) {
            try {
                String method = exchange.getRequestMethod();

                if ("GET".equalsIgnoreCase(method)) {
                    handleGet(exchange);
                    return;
                }

                if ("POST".equalsIgnoreCase(method)) {
                    handlePost(exchange);
                    return;
                }

                respond(exchange, 405, "Método não permitido");

            } catch (Exception e) {
                LogUtil.error("Erro ao processar webhook", e);

                try {
                    respond(exchange, 500, "Erro interno");
                } catch (Exception ignored) {
                }
            }
        }

        private void handleGet(HttpExchange exchange) throws Exception {
            String query = exchange.getRequestURI().getQuery();

            if (query == null) {
                respond(exchange, 200, "Webhook GET ok");
                return;
            }

            String mode = getQueryParam(query, "hub.mode");
            String token = getQueryParam(query, "hub.verify_token");
            String challenge = getQueryParam(query, "hub.challenge");

            if ("subscribe".equals(mode) && VERIFY_TOKEN.equals(token) && challenge != null) {
                LogUtil.info("Webhook verificado com sucesso.");
                respond(exchange, 200, challenge);
                return;
            }

            respond(exchange, 403, "Token inválido");
        }

        private void handlePost(HttpExchange exchange) throws Exception {
            String body = readBody(exchange);

            LogUtil.info("Webhook POST recebido:");
            System.out.println(body);

            processPayload(body);

            respond(exchange, 200, "EVENT_RECEIVED");
        }

        private void processPayload(String body) {
            try {
                JSONObject json = new JSONObject(body);

                JSONObject entry = json.getJSONArray("entry").getJSONObject(0);
                JSONObject changes = entry.getJSONArray("changes").getJSONObject(0);
                JSONObject value = changes.getJSONObject("value");

                if (!value.has("messages")) {
                    LogUtil.info("Payload sem messages. Provavelmente status de envio/entrega.");
                    return;
                }

                JSONObject message = value.getJSONArray("messages").getJSONObject(0);

                String from = message.getString("from");
                String type = message.getString("type");

                LogUtil.info("Telefone origem: " + from);
                LogUtil.info("Tipo mensagem: " + type);

                if ("button".equalsIgnoreCase(type)) {
                    JSONObject button = message.getJSONObject("button");

                    String payload = button.optString("payload", "");
                    String text = button.optString("text", "");

                    LogUtil.info("Botão clicado payload: " + payload);
                    LogUtil.info("Botão clicado text: " + text);

                    if ("Ok".equalsIgnoreCase(payload) || "Ok".equalsIgnoreCase(text)) {
                        processConfirmedClick(from);
                    }

                    return;
                }

                if ("interactive".equalsIgnoreCase(type)) {
                    JSONObject interactive = message.getJSONObject("interactive");

                    if (!interactive.has("button_reply")) {
                        LogUtil.info("Interactive sem button_reply.");
                        return;
                    }

                    JSONObject buttonReply = interactive.getJSONObject("button_reply");

                    String buttonId = buttonReply.optString("id", "");
                    String buttonTitle = buttonReply.optString("title", "");

                    LogUtil.info("Botão interativo id: " + buttonId);
                    LogUtil.info("Botão interativo title: " + buttonTitle);

                    if ("ok".equalsIgnoreCase(buttonId) || "Ok".equalsIgnoreCase(buttonTitle)) {
                        processConfirmedClick(from);
                    }

                    return;
                }

                LogUtil.info("Tipo de mensagem ignorado: " + type);

            } catch (Exception e) {
                LogUtil.error("Erro ao interpretar payload do webhook", e);
            }
        }

        private void processConfirmedClick(String phone) {
            try {
                LogUtil.info("Clique confirmado. Gerando dashboard...");

                dashboardProcessorService.process();

                File imageFile = new File(AppConfig.OUTPUT_PATH);

                if (!imageFile.exists()) {
                    throw new RuntimeException("Imagem não encontrada em: " + AppConfig.OUTPUT_PATH);
                }

                BufferedImage image = ImageIO.read(imageFile);

                if (image == null) {
                    throw new RuntimeException("Erro ao carregar imagem gerada: " + AppConfig.OUTPUT_PATH);
                }

                LogUtil.info("Imagem carregada: " + AppConfig.OUTPUT_PATH);
                LogUtil.info("Enviando imagem para: " + phone);

                whatsAppSenderService.sendImage(phone, image);

                LogUtil.info("Fluxo de envio da imagem finalizado.");

            } catch (Exception e) {
                LogUtil.error("Erro ao processar clique confirmado", e);
            }
        }

        private String getQueryParam(String query, String key) {
            String[] pairs = query.split("&");

            for (String pair : pairs) {
                String[] parts = pair.split("=", 2);

                if (parts.length == 2 && key.equals(parts[0])) {
                    return parts[1];
                }
            }

            return null;
        }

        private String readBody(HttpExchange exchange) throws Exception {
            InputStream inputStream = exchange.getRequestBody();
            byte[] bytes = inputStream.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        }

        private void respond(HttpExchange exchange, int statusCode, String response) throws Exception {
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, bytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}