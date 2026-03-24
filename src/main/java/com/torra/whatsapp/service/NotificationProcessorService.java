package com.torra.whatsapp.service;

import com.torra.whatsapp.config.ZApiConfig;
import com.torra.whatsapp.model.ProcessingResult;
import com.torra.whatsapp.model.StoreNotificationData;
import com.torra.whatsapp.util.ImageBase64Util;
import com.torra.whatsapp.util.LogUtil;

import static com.torra.whatsapp.config.AppConfig.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
        List<ProcessingResult> results = new ArrayList<>();

        int total = list.size();
        int index = 1;

        for (StoreNotificationData item : list) {

            long startTime = System.currentTimeMillis();

            try {
                LogUtil.info("[" + index + "/" + total + "] Iniciando processamento | loja="
                        + item.getStoreName() + " | telefone=" + item.getPhone());

                BufferedImage image = imageGeneratorService.generateImage(
                        TEMPLATE_PATH,
                        OUTPUT_PATH,
                        TITLE,
                        item.getCoordinatorName(),
                        item.getMessage());

                String imageBase64 = ImageBase64Util.toBase64Png(image);

                ZApiConfig config = new ZApiConfig(
                        PRODUCT_ID,
                        PHONE_ID,
                        TOKEN,
                        item.getPhone());

                String response = whatsAppSenderService.sendImage(
                        config,
                        imageBase64,
                        "Meta da loja " + item.getStoreName());

                long endTime = System.currentTimeMillis();

                results.add(new ProcessingResult(
                        item.getStoreName(),
                        item.getPhone(),
                        true,
                        response,
                        (endTime - startTime)));

                LogUtil.info("[" + index + "/" + total + "] Sucesso | tempo="
                        + (endTime - startTime) + "ms");

            } catch (Exception e) {

                long endTime = System.currentTimeMillis();

                results.add(new ProcessingResult(
                        item.getStoreName(),
                        item.getPhone(),
                        false,
                        e.getMessage(),
                        (endTime - startTime)));

                LogUtil.error("[" + index + "/" + total + "] Erro no processamento", e);
            }

            Thread.sleep(2000);
            index++;
        }

        long successCount = results.stream().filter(ProcessingResult::isSuccess).count();
        long errorCount = results.size() - successCount;

        LogUtil.info("===== RESUMO DO LOTE =====");
        LogUtil.info("Total: " + results.size());
        LogUtil.info("Sucesso: " + successCount);
        LogUtil.info("Erro: " + errorCount);
    }
}