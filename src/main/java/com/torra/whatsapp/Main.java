package com.torra.whatsapp;

import com.torra.whatsapp.service.CsvReaderService;
import com.torra.whatsapp.model.StoreNotificationData;
import java.util.List;
import java.awt.image.BufferedImage;

import com.torra.whatsapp.config.ZApiConfig;
import com.torra.whatsapp.service.ImageGeneratorService;
import com.torra.whatsapp.service.WhatsAppSenderService;
import com.torra.whatsapp.util.ImageBase64Util;

public class Main {

    public static void main(String[] args) {
        try {
            CsvReaderService csvReaderService = new CsvReaderService();
            List<StoreNotificationData> list = csvReaderService.read();

            for (StoreNotificationData item : list) {
                System.out.println(item.getCoordinatorName() + " - " + item.getStoreName() + " - " + item.getPhone());
            }

            String title = "Meta HORA";
            String name = "NomeCoordenador";
            String message = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
            String templatePath = "resources/templates/template.png";
            String outputPath = "resultado.png";

            ZApiConfig config = new ZApiConfig(
                    "8f8a084a-b27f-4d7c-9b13-436887d5eebf",
                    "137677",
                    "c214868f-cdb7-4bc8-b300-99e94200a61e",
                    "5511984696489"
            );

            ImageGeneratorService imageGeneratorService = new ImageGeneratorService();
            WhatsAppSenderService whatsAppSenderService = new WhatsAppSenderService();

            BufferedImage image = imageGeneratorService.generateImage(
                    templatePath,
                    outputPath,
                    title,
                    name,
                    message
            );

            String imageBase64 = ImageBase64Util.toBase64Png(image);

            String response = whatsAppSenderService.sendImage(
                    config,
                    imageBase64,
                    "Teste real Java"
            );

            System.out.println("Imagem gerada com sucesso.");
            System.out.println(response);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}