package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.config.ZApiConfig;
import br.com.torra.whatsapp.util.LogUtil;

public class WhatsAppSenderService {

    public String sendImage(
            ZApiConfig config,
            String phone,
            String imageBase64,
            String caption) {

        long startTime = System.currentTimeMillis();

        LogUtil.info("Preparando envio MOCK | telefone=" + phone
                + " | caption=" + caption
                + " | base64_length=" + imageBase64.length());

        long endTime = System.currentTimeMillis();

        LogUtil.info("Resposta MOCK | telefone=" + phone
                + " | status=200"
                + " | tempo=" + (endTime - startTime) + "ms");

        LogUtil.info("Body resposta MOCK | telefone=" + phone
                + " | response={\"success\":true,\"mock\":true}");

        return "Status: 200\nResposta: {\"success\":true,\"mock\":true}";
    }
}