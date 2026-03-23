package com.torra.whatsapp.service;

import com.torra.whatsapp.config.ZApiConfig;
import com.torra.whatsapp.model.StoreNotificationData;
import com.torra.whatsapp.util.ImageBase64Util;
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

            System.out.println("Processando: " + item.getCoordinatorName());

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

            System.out.println("Enviado para: " + item.getPhone());
            System.out.println(response);

            Thread.sleep(2000);
        }
    }
}