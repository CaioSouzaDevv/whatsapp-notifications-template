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

    private static final String FILE_PATH = "data/hora-mock-completo.csv";

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
                            get(columns, headerIndex, "FilialID_TACC", "filial_id"),
                            get(columns, headerIndex, "nm_filial", "filial", "nome"),
                            get(columns, headerIndex, "V_Meta_Ativados"),
                            get(columns, headerIndex, "Qtd_Aprovacoes", "Aprovados"),
                            get(columns, headerIndex, "Qtd_Aprovacoes_ly"),
                            get(columns, headerIndex, "Qtd_Propostas", "Indicados"),
                            get(columns, headerIndex, "V_Meta_Padrao_PCJ", "Meta_PCJ"),
                            get(columns, headerIndex, "V_Meta_Padrao_Participacao", "Meta_Participacao_%"),
                            get(columns, headerIndex, "telefone")
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

            if (webhookPhone.equals(csvPhone)) {
                return item;
            }
        }

        return null;
    }

    private Map<String, Integer> buildHeaderIndex(String[] headers) {
        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            map.put(headers[i].trim(), i);
        }

        return map;
    }

    private String get(String[] columns, Map<String, Integer> map, String... keys) {
        for (String key : keys) {
            Integer index = map.get(key);

            if (index != null && index < columns.length) {
                return columns[index].trim();
            }
        }

        return "";
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