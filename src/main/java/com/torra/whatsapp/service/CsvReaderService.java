package com.torra.whatsapp.service;

import com.torra.whatsapp.model.StoreNotificationData;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderService {

    private static final String FILE_PATH = "data/store-notifications.csv";

    public List<StoreNotificationData> read() {
        List<StoreNotificationData> notifications = new ArrayList<>();

        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(FILE_PATH);

        if (inputStream == null) {
            throw new RuntimeException("Arquivo CSV não encontrado em: " + FILE_PATH);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] columns = line.split(",");

                if (columns.length < 8) {
                    throw new RuntimeException("Linha inválida no CSV: " + line);
                }

                StoreNotificationData data = new StoreNotificationData(
                        columns[0].trim(),
                        columns[1].trim(),
                        columns[2].trim(),
                        columns[3].trim(),
                        columns[4].trim(),
                        columns[5].trim(),
                        columns[6].trim(),
                        columns[7].trim());

                notifications.add(data);
            }

            return notifications;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo CSV.", e);
        }
    }
}