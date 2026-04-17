package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.model.StoreNotificationData;
import br.com.torra.whatsapp.util.LogUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderService {

    private static final String FILE_PATH = "data/hora-mock.csv";

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
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] columns = line.split(";", -1);

                if (columns.length < 20) {
                    LogUtil.info("Linha ignorada por quantidade insuficiente de colunas | linha="
                            + lineNumber + " | colunas=" + columns.length + " | conteúdo=" + line);
                    continue;
                }

                StoreNotificationData data = new StoreNotificationData(
                        columns[0].trim(),
                        columns[1].trim(),
                        columns[2].trim(),
                        columns[3].trim(),
                        columns[4].trim(),
                        columns[5].trim(),
                        columns[11].trim(),
                        columns[15].trim(),
                        columns[19].trim()
                );

                notifications.add(data);
            }

            return notifications;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo CSV.", e);
        }
    }
}