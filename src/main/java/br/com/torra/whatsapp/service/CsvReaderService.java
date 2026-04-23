package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.model.StoreNotificationData;
import br.com.torra.whatsapp.util.LogUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

            String headerLine = reader.readLine();

            if (headerLine == null || headerLine.isBlank()) {
                throw new RuntimeException("CSV sem cabeçalho.");
            }

            String[] headers = headerLine.split(";", -1);
            Map<String, Integer> headerIndex = buildHeaderIndex(headers);

            validateRequiredColumns(headerIndex);

            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] columns = line.split(";", -1);

                try {
                    StoreNotificationData data = new StoreNotificationData(
                            get(columns, headerIndex, "FilialID_TACC"),
                            get(columns, headerIndex, "nm_filial"),
                            get(columns, headerIndex, "V_Meta_Ativados"),
                            get(columns, headerIndex, "Qtd_Aprovacoes"),
                            get(columns, headerIndex, "Qtd_Aprovacoes_ly"),
                            get(columns, headerIndex, "Qtd_Propostas"),
                            get(columns, headerIndex, "V_Meta_Padrao_PCJ"),
                            get(columns, headerIndex, "V_Meta_Padrao_Participacao"),
                            normalizePhone(getLastColumn(columns)) // ← TELEFONE PEGO AUTOMÁTICO
                    );

                    notifications.add(data);

                } catch (Exception e) {
                    LogUtil.info("Erro ao processar linha " + lineNumber + " | conteúdo=" + line);
                }
            }

            return notifications;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo CSV.", e);
        }
    }

    public StoreNotificationData findByPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return null;
        }

        String webhookPhone = normalizePhone(phone);

        if (webhookPhone.startsWith("55")) {
            webhookPhone = webhookPhone.substring(2);
        }

        for (StoreNotificationData item : read()) {

            if (item.getPhone() == null || item.getPhone().isBlank()) {
                continue;
            }

            String csvPhone = normalizePhone(item.getPhone());

            if (csvPhone.startsWith("55")) {
                csvPhone = csvPhone.substring(2);
            }

            System.out.println("Comparando -> webhook: " + webhookPhone
                    + " | csv: " + csvPhone
                    + " | loja: " + item.getStoreName());

            if (webhookPhone.equals(csvPhone)) {
                return item;
            }
        }

        return null;
    }

    // ==========================
    // Helpers
    // ==========================

    private Map<String, Integer> buildHeaderIndex(String[] headers) {
        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            map.put(headers[i].trim(), i);
        }

        return map;
    }

    private void validateRequiredColumns(Map<String, Integer> map) {
        List<String> required = List.of(
                "FilialID_TACC",
                "nm_filial",
                "V_Meta_Ativados",
                "Qtd_Aprovacoes",
                "Qtd_Aprovacoes_ly",
                "Qtd_Propostas",
                "V_Meta_Padrao_PCJ",
                "V_Meta_Padrao_Participacao");

        for (String column : required) {
            if (!map.containsKey(column)) {
                throw new RuntimeException("Coluna obrigatória não encontrada no CSV: " + column);
            }
        }
    }

    private String get(String[] columns, Map<String, Integer> map, String key) {
        Integer index = map.get(key);

        if (index == null || index >= columns.length) {
            return "";
        }

        return columns[index].trim();
    }

    private String getLastColumn(String[] columns) {
        if (columns.length == 0) {
            return "";
        }
        return columns[columns.length - 1].trim();
    }

    private String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return "";
        }

        String value = phone.trim();

        try {
            if (value.contains("E") || value.contains("e")) {
                value = value.replace(",", ".");
                BigDecimal bd = new BigDecimal(value);
                value = bd.toPlainString();
            }

            if (value.endsWith(".0")) {
                value = value.substring(0, value.length() - 2);
            }

        } catch (Exception ignored) {
        }

        return value.replaceAll("\\D", "");
    }
}