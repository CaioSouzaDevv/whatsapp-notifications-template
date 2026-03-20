package com.torra.whatsapp;

import java.awt.image.BufferedImage;

import com.torra.whatsapp.config.ZApiConfig;
import com.torra.whatsapp.service.ImageGeneratorService;
import com.torra.whatsapp.service.WhatsAppSenderService;
import com.torra.whatsapp.util.ImageBase64Util;

public class Main {

    public static void main(String[] args) {
        try {
            String title = "Meta HORA";
            String name = "NomeCoordenador";
            String message = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
            String templatePath = "../resources/templates/template.png";
            String outputPath = "resultado.png";

            ZApiConfig config = new ZApiConfig(
                    "3F0533A91EA4823D95E8022729239B50",
                    "09AFB49A9EAE10B5BFFB358E",
                    "Fab3e500b5ba14ba7a8ab4016102f8be0S",
                    "5511984696489");

            ImageGeneratorService imageGeneratorService = new ImageGeneratorService();
            WhatsAppSenderService whatsAppSenderService = new WhatsAppSenderService();

            BufferedImage image = imageGeneratorService.generateImage(
                    templatePath,
                    outputPath,
                    title,
                    name,
                    message);

            String imageBase64 = ImageBase64Util.toBase64Png(image);

            String response = whatsAppSenderService.sendImage(
                    config,
                    imageBase64,
                    "Teste real Java");

            System.out.println("Imagem gerada com sucesso.");
            System.out.println(response);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}