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

    private static final String FILE_PATH = "data/hora.csv";

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

                if (columns.length < 19) {
                    LogUtil.info("Linha ignorada por quantidade insuficiente de colunas | linha="
                            + lineNumber + " | colunas=" + columns.length + " | conteúdo=" + line);
                    continue;
                }

                StoreNotificationData data = new StoreNotificationData(
                        columns[0].trim(),   // FilialID_TACC
                        columns[1].trim(),   // nm_filial
                        columns[2].trim(),   // V_Meta_Ativados
                        columns[3].trim(),   // Qtd_Aprovacoes
                        columns[4].trim(),   // Qtd_Aprovacoes_ly
                        columns[5].trim(),   // Qtd_Propostas
                        columns[11].trim(),  // V_Meta_Padrao_PCJ
                        columns[15].trim()   // V_Meta_Padrao_Participacao
                );

                notifications.add(data);
            }

            return notifications;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo CSV.", e);
        }
    }
}