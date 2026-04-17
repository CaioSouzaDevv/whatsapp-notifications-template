package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.model.StoreNotificationData;
import br.com.torra.whatsapp.util.LogUtil;

import java.awt.image.BufferedImage;
import java.util.List;

import static br.com.torra.whatsapp.config.AppConfig.*;

public class NotificationProcessorService {

    private final CsvReaderService csvReaderService;
    private final ImageGeneratorService imageGeneratorService;

    public NotificationProcessorService() {
        this.csvReaderService = new CsvReaderService();
        this.imageGeneratorService = new ImageGeneratorService();
    }

    public void process() throws Exception {

        List<StoreNotificationData> list = csvReaderService.read();

        if (list.isEmpty()) {
            LogUtil.info("Nenhum registro encontrado no CSV.");
            return;
        }

        StoreNotificationData item = list.get(0);

        if (item.getPhone() == null || item.getPhone().isBlank()) {
            LogUtil.info("Telefone inválido para a loja: " + item.getStoreName());
            return;
        }

        LogUtil.info("Gerando imagem da loja: " + item.getStoreName());

        BufferedImage image = imageGeneratorService.generateImage(
                TEMPLATE_PATH,
                OUTPUT_PATH,
                TITLE,
                item
        );

        LogUtil.info("Imagem gerada com sucesso.");

        WhatsAppSenderService sender = new WhatsAppSenderService();
        sender.sendImage(item.getPhone(), image);

        LogUtil.info("Envio finalizado.");
    }
}