package br.com.torra.whatsapp;

import br.com.torra.whatsapp.config.AppConfig;
import br.com.torra.whatsapp.model.StoreNotificationData;
import br.com.torra.whatsapp.service.CsvReaderService;
import br.com.torra.whatsapp.service.ImageGeneratorService;
import br.com.torra.whatsapp.service.WhatsAppSenderService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class WebhookServer {

    public static void start() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/webhook", new WebhookHandler());
        server.setExecutor(null);
        server.start();

        System.out.println("Servidor rodando em http://localhost:8080/webhook");
    }

    static class WebhookHandler implements HttpHandler {

        private final CsvReaderService csvReaderService;
        private final ImageGeneratorService imageGeneratorService;
        private final WhatsAppSenderService whatsAppSenderService;

        public WebhookHandler() {
            this.csvReaderService = new CsvReaderService();
            this.imageGeneratorService = new ImageGeneratorService();
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
                e.printStackTrace();
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

            if ("subscribe".equals(mode) && "teste123".equals(token) && challenge != null) {
                respond(exchange, 200, challenge);
                return;
            }

            respond(exchange, 403, "Token inválido");
        }

        private void handlePost(HttpExchange exchange) throws Exception {
            String body = readBody(exchange);

            System.out.println("Payload recebido:");
            System.out.println(body);

            extractAndProcess(body);

            respond(exchange, 200, "EVENT_RECEIVED");
        }

        private void extractAndProcess(String body) {
            try {
                JSONObject json = new JSONObject(body);

                JSONObject entry = json.getJSONArray("entry").getJSONObject(0);
                JSONObject changes = entry.getJSONArray("changes").getJSONObject(0);
                JSONObject value = changes.getJSONObject("value");

                if (!value.has("messages")) {
                    System.out.println("Sem mensagens no payload.");
                    return;
                }

                JSONObject message = value.getJSONArray("messages").getJSONObject(0);

                String from = message.getString("from");
                String type = message.getString("type");

                System.out.println("Telefone: " + from);
                System.out.println("Tipo da mensagem: " + type);

                if ("button".equals(type)) {
                    JSONObject button = message.getJSONObject("button");

                    String payload = button.optString("payload", "");
                    String text = button.optString("text", "");

                    System.out.println("Botão clicado (payload): " + payload);
                    System.out.println("Botão clicado (text): " + text);

                    if ("Ok".equalsIgnoreCase(payload) || "Ok".equalsIgnoreCase(text)) {
                        processConfirmedClick(from);
                    }

                    return;
                }

                if ("interactive".equals(type)) {
                    JSONObject interactive = message.getJSONObject("interactive");
                    JSONObject buttonReply = interactive.getJSONObject("button_reply");

                    String buttonId = buttonReply.optString("id", "");
                    String buttonTitle = buttonReply.optString("title", "");

                    System.out.println("Botão clicado (id): " + buttonId);
                    System.out.println("Botão clicado (title): " + buttonTitle);

                    if ("ok".equalsIgnoreCase(buttonId) || "Ok".equalsIgnoreCase(buttonTitle)) {
                        processConfirmedClick(from);
                    }

                    return;
                }

                System.out.println("Tipo de mensagem não tratado: " + type);

            } catch (Exception e) {
                System.out.println("Erro ao extrair dados do payload.");
                e.printStackTrace();
            }
        }

        private void processConfirmedClick(String phone) {
            try {
                System.out.println("Processando clique confirmado para o telefone: " + phone);

                StoreNotificationData item = csvReaderService.findByPhone(phone);

                if (item == null) {
                    System.out.println("Nenhuma loja encontrada para o telefone: " + phone);
                    return;
                }

                System.out.println("Loja encontrada: " + item.getStoreName());
                System.out.println("Gerando imagem...");

                BufferedImage image = imageGeneratorService.generateImage(
                        AppConfig.TEMPLATE_PATH,
                        AppConfig.OUTPUT_PATH,
                        AppConfig.TITLE,
                        item);

                System.out.println("Imagem gerada com sucesso.");
                System.out.println("Enviando imagem para o telefone: " + phone);

                whatsAppSenderService.sendImage(phone, image);

                System.out.println("Imagem enviada com sucesso.");

            } catch (Exception e) {
                System.out.println("Erro ao processar clique confirmado.");
                e.printStackTrace();
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