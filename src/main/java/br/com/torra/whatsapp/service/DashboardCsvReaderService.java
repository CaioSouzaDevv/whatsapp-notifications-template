package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.model.DashboardRow;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DashboardCsvReaderService {

    private static final String FILE_PATH = "data/hora-mock-completo.csv";

    public List<DashboardRow> read() {
        List<DashboardRow> rows = new ArrayList<>();

        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(FILE_PATH);

        if (inputStream == null) {
            throw new RuntimeException("CSV do dashboard não encontrado em: " + FILE_PATH);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();

            if (headerLine == null || headerLine.isBlank()) {
                throw new RuntimeException("CSV do dashboard está sem cabeçalho.");
            }

            String[] headers = removeBom(headerLine).split(";", -1);
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
                    DashboardRow row = new DashboardRow();

                    row.rowType = get(columns, headerIndex, "row_type");
                    row.section = get(columns, headerIndex, "section");
                    row.sortOrder = parseInt(get(columns, headerIndex, "sort_order"));

                    row.scope = get(columns, headerIndex, "scope");
                    row.regional = get(columns, headerIndex, "regional");
                    row.coordenador = get(columns, headerIndex, "coordenador");
                    row.filial = get(columns, headerIndex, "filial");

                    row.cardTitle = get(columns, headerIndex, "card_title");
                    row.cardSubtitle = get(columns, headerIndex, "card_subtitle");
                    row.metricKey = get(columns, headerIndex, "metric_key");
                    row.valor = get(columns, headerIndex, "valor");
                    row.meta = get(columns, headerIndex, "meta");
                    row.ly = get(columns, headerIndex, "ly");
                    row.observacao = get(columns, headerIndex, "observacao");

                    row.pcj = get(columns, headerIndex, "pcj");
                    row.metaPcj = get(columns, headerIndex, "meta_pcj");
                    row.participacao = get(columns, headerIndex, "participacao");
                    row.metaParticipacao = get(columns, headerIndex, "meta_participacao");

                    row.indicados = get(columns, headerIndex, "indicados");
                    row.aprovados = get(columns, headerIndex, "aprovados");
                    row.naoCartao = get(columns, headerIndex, "vendas_nao_cartao");
                    row.aproveitamento = get(columns, headerIndex, "aproveitamento");

                    row.emprestimo = get(columns, headerIndex, "emprestimo_pessoal");
                    row.metaEmprestimo = get(columns, headerIndex, "meta_emprestimo_pessoal");

                    rows.add(row);

                } catch (Exception e) {
                    throw new RuntimeException("Erro ao processar linha " + lineNumber + ": " + line, e);
                }
            }

            rows.sort(Comparator.comparing((DashboardRow r) -> safe(r.section))
                    .thenComparingInt(r -> r.sortOrder));

            return rows;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler CSV do dashboard.", e);
        }
    }

    private Map<String, Integer> buildHeaderIndex(String[] headers) {
        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            map.put(headers[i].trim(), i);
        }

        return map;
    }

    private String get(String[] columns, Map<String, Integer> headerIndex, String key) {
        Integer index = headerIndex.get(key);

        if (index == null || index >= columns.length) {
            return "";
        }

        return columns[index].trim();
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private String removeBom(String value) {
        return value == null ? "" : value.replace("\uFEFF", "");
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}