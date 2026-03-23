package com.torra.whatsapp.service;

import com.torra.whatsapp.config.ZApiConfig;
import com.torra.whatsapp.model.StoreNotificationData;
import com.torra.whatsapp.util.ImageBase64Util;
import com.torra.whatsapp.util.LogUtil;

import static com.torra.whatsapp.config.AppConfig.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class NotificationProcessorService {

    private final CsvReaderService csvReaderService;
    private final ImageGeneratorService imageGeneratorService;
    private final WhatsAppSenderService whatsAppSenderService;


    public NotificationProcessorService() {
        this.csvReaderService = new CsvReaderService();
        this.imageGeneratorService = new ImageGeneratorService();
        this.whatsAppSenderService = new WhatsAppSenderService();
    }

    public void process() throws Exception {

        List<StoreNotificationData> list = csvReaderService.read();

        for (StoreNotificationData item : list) {

LogUtil.info("Processando coordenador: " + item.getCoordinatorName()
        + " | loja: " + item.getStoreName()
        + " | telefone: " + item.getPhone());

        
            BufferedImage image = imageGeneratorService.generateImage(
                    TEMPLATE_PATH,
                    OUTPUT_PATH,
                    TITLE,
                    item.getCoordinatorName(),
                    item.getMessage()
            );

            String imageBase64 = ImageBase64Util.toBase64Png(image);

            ZApiConfig config = new ZApiConfig(
                    PRODUCT_ID,
                    PHONE_ID,
                    TOKEN,
                    item.getPhone()
            );

            String response = whatsAppSenderService.sendImage(
                    config,
                    imageBase64,
                    "Meta da loja " + item.getStoreName()
            );

        LogUtil.info("Mensagem adicionada para envio no telefone: " + item.getPhone());
LogUtil.info("Resposta da API: " + response);

            Thread.sleep(2000);
        }
    }
}