package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.model.DashboardRow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DashboardImageGeneratorService {

    private static final int WIDTH = 1900;
    private static final int HEIGHT = 1750;

    private static final Color BACKGROUND = Color.WHITE;
    private static final Color CARD_BACKGROUND = new Color(214, 235, 229);
    private static final Color TEXT_DARK = new Color(48, 62, 68);
    private static final Color TEXT_MUTED = new Color(95, 100, 100);
    private static final Color ORANGE = new Color(232, 103, 59);
    private static final Color GRID = new Color(225, 225, 225);
    private static final Color HEADER_BG = new Color(245, 247, 247);
    private static final Color RED = new Color(220, 0, 0);
    private static final Color GREEN = new Color(0, 150, 60);

    public void generate(List<DashboardRow> rows) throws Exception {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        configure(g);

        g.setColor(BACKGROUND);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        drawCards(g, rows);

        int tableY = 430;

        tableY = drawTable(g, "FILIAIS_TOPO", filter(rows, "FILIAIS_TOPO"), 110, tableY, true);
        tableY = drawTable(g, "COORDENADORES", filter(rows, "COORDENADORES"), 110, tableY + 36, false);
        tableY = drawTable(g, "REGIONAIS", filter(rows, "REGIONAIS"), 110, tableY + 36, false);
        drawTable(g, "FILIAIS", filter(rows, "FILIAIS_BASE"), 60, tableY + 36, false);

        g.dispose();

        ImageIO.write(image, "png", new File("resultado.png"));
    }

    private void configure(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    private void drawCards(Graphics2D g, List<DashboardRow> rows) {
        List<DashboardRow> cards = filter(rows, "CARDS");

        int cardY = 35;
        int cardW = 390;
        int cardH = 360;
        int gap = 35;

        int totalCardsW = (cardW * 3) + (gap * 2);
        int x1 = (WIDTH - totalCardsW) / 2;
        int x2 = x1 + cardW + gap;
        int x3 = x2 + cardW + gap;

        drawLeftCard(g, x1, cardY, cardW, cardH, find(cards, "PCJ"), find(cards, "PARTICIPACAO"));

        drawMiddleCard(
                g,
                x2,
                cardY,
                cardW,
                cardH,
                find(cards, "INDICADOS"),
                find(cards, "APROVADOS"),
                find(cards, "TICKETS_NAO_CARTAO")
        );

        drawRightCard(
                g,
                x3,
                cardY,
                cardW,
                cardH,
                find(cards, "EMPRESTIMO_PESSOAL"),
                find(cards, "APROVEITAMENTO")
        );
    }

    private void drawLeftCard(Graphics2D g, int x, int y, int w, int h, DashboardRow pcj, DashboardRow participacao) {
        drawCardBackground(g, x, y, w, h);

        drawMainMetricBlock(g, x + 24, y + 44, pcj);
        drawMetaLy(g, x + 24, y + 132, pcj);

        drawMainMetricBlock(g, x + 24, y + 220, participacao);
        drawMetaLy(g, x + 24, y + 308, participacao);
    }

    private void drawMiddleCard(
            Graphics2D g,
            int x,
            int y,
            int w,
            int h,
            DashboardRow indicados,
            DashboardRow aprovados,
            DashboardRow tickets
    ) {
        drawCardBackground(g, x, y, w, h);

        drawCompactMetric(g, x + 24, y + 44, indicados, false);
        drawCompactMetric(g, x + 24, y + 156, aprovados, true);
        drawCompactMetric(g, x + 24, y + 268, tickets, false);
    }

    private void drawRightCard(
            Graphics2D g,
            int x,
            int y,
            int w,
            int h,
            DashboardRow emprestimo,
            DashboardRow aproveitamento
    ) {
        drawCardBackground(g, x, y, w, h);

        drawCompactMetric(g, x + 24, y + 44, emprestimo, true);

        g.setFont(new Font("Arial", Font.ITALIC, 16));
        g.setColor(new Color(110, 120, 115));

        if (emprestimo != null) {
            drawMultiline(g, value(emprestimo.observacao), x + 24, y + 155, 295, 20);
        }

        drawCompactMetric(g, x + 24, y + 260, aproveitamento, false);
    }

    private void drawCardBackground(Graphics2D g, int x, int y, int w, int h) {
        g.setColor(CARD_BACKGROUND);
        g.fillRoundRect(x, y, w, h, 20, 20);
    }

    private void drawMainMetricBlock(Graphics2D g, int x, int y, DashboardRow row) {
        if (row == null) return;

        g.setColor(TEXT_DARK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(value(row.cardTitle), x, y);

        g.setColor(TEXT_MUTED);
        g.setFont(new Font("Arial", Font.PLAIN, 19));
        g.drawString(value(row.cardSubtitle), x, y + 24);

        g.setColor(ORANGE);
        g.setFont(new Font("Arial", Font.BOLD, 44));
        g.drawString(value(row.valor), x, y + 72);
    }

    private void drawCompactMetric(Graphics2D g, int x, int y, DashboardRow row, boolean showMeta) {
        if (row == null) return;

        g.setColor(TEXT_DARK);
        g.setFont(new Font("Arial", Font.BOLD, 19));
        g.drawString(value(row.cardTitle), x, y);

        g.setColor(TEXT_MUTED);
        g.setFont(new Font("Arial", Font.PLAIN, 17));
        g.drawString(value(row.cardSubtitle), x, y + 22);

        g.setColor(ORANGE);
        g.setFont(new Font("Arial", Font.BOLD, 34));
        g.drawString(value(row.valor), x, y + 64);

        int infoX = x + 165;
        int deltaX = x + 285;

        g.setColor(TEXT_DARK);
        g.setFont(new Font("Arial", Font.PLAIN, 15));

        if (showMeta) {
            g.drawString("Meta", infoX, y + 44);

            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString(value(row.meta), infoX + 54, y + 44);

            g.setFont(new Font("Arial", Font.PLAIN, 15));
            g.drawString("LY", infoX, y + 68);

            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString(value(row.ly), infoX + 54, y + 68);
        } else {
            g.drawString("LY", infoX, y + 64);

            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString(value(row.ly), infoX + 44, y + 64);
        }

        drawCardDelta(g, deltaX, y + 42);
    }

    private void drawMetaLy(Graphics2D g, int x, int y, DashboardRow row) {
        if (row == null) return;

        int labelX = x;
        int valueX = x + 62;
        int deltaX = x + 155;

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.setColor(TEXT_DARK);
        g.drawString("Meta", labelX, y);

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(value(row.meta), valueX, y);

        g.setColor(RED);
        g.drawString("-1,2%", deltaX, y);

        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.setColor(TEXT_DARK);
        g.drawString("LY", labelX, y + 28);

        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString(value(row.ly), valueX, y + 28);

        g.setColor(GREEN);
        g.drawString("-1,1%", deltaX, y + 28);
    }

    private void drawCardDelta(Graphics2D g, int x, int y) {
        g.setColor(RED);
        g.setFont(new Font("Arial", Font.BOLD, 15));
        g.drawString("-1,2%", x, y);
        g.drawString("-20,4%", x, y + 24);
    }

    private int drawTable(Graphics2D g, String title, List<DashboardRow> rows, int x, int y, boolean showTopHeaders) {
        if (rows.isEmpty()) return y;

        boolean isCoordenadores = "COORDENADORES".equalsIgnoreCase(title);
        boolean isRegionais = "REGIONAIS".equalsIgnoreCase(title);
        boolean isFiliaisBase = "FILIAIS".equalsIgnoreCase(title);

        int rowH = 22;
        int headerH = 34;

        int[] widths = buildWidths(isCoordenadores, isRegionais, isFiliaisBase);
        String[] headers = buildHeaders(isCoordenadores, isRegionais, isFiliaisBase);
        int totalW = sum(widths);

        if (!showTopHeaders) {
            g.setColor(TEXT_DARK);
            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString(title, x + (totalW / 2) - 45, y + 18);
            y += 28;
        }

        g.setFont(new Font("Arial", Font.BOLD, 11));
        g.setColor(HEADER_BG);
        g.fillRect(x, y, totalW, headerH);

        int currentX = x;

        for (int i = 0; i < headers.length; i++) {
            g.setColor(TEXT_DARK);
            drawCentered(g, headers[i], currentX, y + 20, widths[i]);

            g.setColor(GRID);
            g.drawRect(currentX, y, widths[i], headerH);

            currentX += widths[i];
        }

        y += headerH;

        g.setFont(new Font("Arial", Font.PLAIN, 11));

        for (DashboardRow row : rows) {
            currentX = x;

            String[] values = buildValues(row, isCoordenadores, isRegionais, isFiliaisBase);

            for (int i = 0; i < values.length; i++) {
                g.setColor(Color.WHITE);
                g.fillRect(currentX, y, widths[i], rowH);

                g.setColor(GRID);
                g.drawRect(currentX, y, widths[i], rowH);

                if (isDeltaColumn(i, isCoordenadores, isRegionais, isFiliaisBase)) {
                    drawDeltaValue(g, value(values[i]), currentX, y + 15, widths[i]);
                } else {
                    g.setColor(TEXT_DARK);
                    drawCentered(g, value(values[i]), currentX, y + 15, widths[i]);
                }

                currentX += widths[i];
            }

            y += rowH;
        }

        return y;
    }

private int[] buildWidths(boolean isCoordenadores, boolean isRegionais, boolean isFiliaisBase) {
    if (isRegionais) {
        return new int[]{
                250,
                85, 95, 115,
                115, 125, 125,
                105, 105, 150,
                130, 140, 150
        };
    }

    if (isCoordenadores) {
        return new int[]{
                250,
                85, 95, 115,
                115, 125, 125,
                105, 105, 150,
                130, 140, 150
        };
    }

    if (isFiliaisBase) {
        return new int[]{
                160, 270,
                75, 90, 110,
                110, 120, 120,
                95, 95, 140,
                120, 130, 140
        };
    }

    return new int[]{
            100, 120, 210,
            75, 90, 110,
            110, 120, 120,
            95, 95, 140,
            120, 130, 140
    };
}

    private String[] buildHeaders(boolean isCoordenadores, boolean isRegionais, boolean isFiliaisBase) {
        if (isRegionais) {
            return new String[]{
                    "Regional",
                    "% PCJ",
                    "Meta PCJ",
                    "App % PCJ x Meta",
                    "Participação %",
                    "Meta Participação",
                    "App Participação",
                    "Indicados",
                    "Aprovados",
                    "Qtd Vendas Não Cartão",
                    "% Aproveitamento",
                    "Empréstimo",
                    "Meta Empréstimo"
            };
        }

        if (isCoordenadores) {
            return new String[]{
                    "Coord.",
                    "% PCJ",
                    "Meta PCJ",
                    "App % PCJ x Meta",
                    "Participação %",
                    "Meta Participação",
                    "App Participação",
                    "Indicados",
                    "Aprovados",
                    "Qtd Vendas Não Cartão",
                    "% Aproveitamento",
                    "Empréstimo",
                    "Meta Empréstimo"
            };
        }

        if (isFiliaisBase) {
            return new String[]{
                    "Coord.",
                    "Filial",
                    "% PCJ",
                    "Meta PCJ",
                    "App % PCJ x Meta",
                    "Participação %",
                    "Meta Participação",
                    "App Participação",
                    "Indicados",
                    "Aprovados",
                    "Qtd Vendas Não Cartão",
                    "% Aproveitamento",
                    "Empréstimo",
                    "Meta Empréstimo"
            };
        }

        return new String[]{
                "Regional",
                "Coord.",
                "Filial",
                "% PCJ",
                "Meta PCJ",
                "App % PCJ x Meta",
                "Participação %",
                "Meta Participação",
                "App Participação",
                "Indicados",
                "Aprovados",
                "Qtd Vendas Não Cartão",
                "% Aproveitamento",
                "Empréstimo",
                "Meta Empréstimo"
        };
    }

    private String[] buildValues(DashboardRow row, boolean isCoordenadores, boolean isRegionais, boolean isFiliaisBase) {
        if (isRegionais) {
            return new String[]{
                    normalizeTableValue(row.regional),
                    row.pcj,
                    row.metaPcj,
                    delta(row.pcj, row.metaPcj),
                    row.participacao,
                    row.metaParticipacao,
                    delta(row.participacao, row.metaParticipacao),
                    row.indicados,
                    row.aprovados,
                    row.naoCartao,
                    row.aproveitamento,
                    row.emprestimo,
                    row.metaEmprestimo
            };
        }

        if (isCoordenadores) {
            return new String[]{
                    normalizeTableValue(row.coordenador),
                    row.pcj,
                    row.metaPcj,
                    delta(row.pcj, row.metaPcj),
                    row.participacao,
                    row.metaParticipacao,
                    delta(row.participacao, row.metaParticipacao),
                    row.indicados,
                    row.aprovados,
                    row.naoCartao,
                    row.aproveitamento,
                    row.emprestimo,
                    row.metaEmprestimo
            };
        }

        if (isFiliaisBase) {
            return new String[]{
                    normalizeTableValue(row.coordenador),
                    normalizeTableValue(row.filial),
                    row.pcj,
                    row.metaPcj,
                    delta(row.pcj, row.metaPcj),
                    row.participacao,
                    row.metaParticipacao,
                    delta(row.participacao, row.metaParticipacao),
                    row.indicados,
                    row.aprovados,
                    row.naoCartao,
                    row.aproveitamento,
                    row.emprestimo,
                    row.metaEmprestimo
            };
        }

        return new String[]{
                normalizeTableValue(row.regional),
                normalizeTableValue(row.coordenador),
                normalizeTableValue(row.filial),
                row.pcj,
                row.metaPcj,
                delta(row.pcj, row.metaPcj),
                row.participacao,
                row.metaParticipacao,
                delta(row.participacao, row.metaParticipacao),
                row.indicados,
                row.aprovados,
                row.naoCartao,
                row.aproveitamento,
                row.emprestimo,
                row.metaEmprestimo
        };
    }

    private boolean isDeltaColumn(int index, boolean isCoordenadores, boolean isRegionais, boolean isFiliaisBase) {
        if (isCoordenadores || isRegionais) {
            return index == 3 || index == 6;
        }

        if (isFiliaisBase) {
            return index == 4 || index == 7;
        }

        return index == 5 || index == 8;
    }

    private String normalizeTableValue(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }

        return value;
    }

    private void drawDeltaValue(Graphics2D g, String value, int x, int y, int width) {
        if (value.startsWith("-")) {
            g.setColor(RED);
            drawCentered(g, "▼ " + value, x, y, width);
            return;
        }

        g.setColor(TEXT_DARK);
        drawCentered(g, "▲ " + value, x, y, width);
    }

    private String delta(String v, String m) {
        try {
            double a = parse(v);
            double b = parse(m);
            double r = a - b;

            return String.format(java.util.Locale.forLanguageTag("pt-BR"), "%.1fpp", r);
        } catch (Exception e) {
            return "-";
        }
    }

    private double parse(String v) {
        return Double.parseDouble(value(v).replace("%", "").replace(".", "").replace(",", "."));
    }

    private void drawCentered(Graphics2D g, String text, int x, int y, int width) {
        String safeText = cutText(g, value(text), width - 8);
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(safeText);
        g.drawString(safeText, x + (width - w) / 2, y);
    }

    private String cutText(Graphics2D g, String text, int maxWidth) {
        if (text == null) return "";

        String result = text;

        while (g.getFontMetrics().stringWidth(result) > maxWidth && result.length() > 2) {
            result = result.substring(0, result.length() - 1);
        }

        if (result.length() < text.length()) {
            return result + ".";
        }

        return result;
    }

    private List<DashboardRow> filter(List<DashboardRow> rows, String section) {
        List<DashboardRow> result = new ArrayList<>();

        for (DashboardRow row : rows) {
            if (section.equalsIgnoreCase(value(row.section))) {
                result.add(row);
            }
        }

        result.sort((a, b) -> Integer.compare(a.sortOrder, b.sortOrder));

        return result;
    }

    private DashboardRow find(List<DashboardRow> rows, String key) {
        for (DashboardRow row : rows) {
            if (key.equalsIgnoreCase(value(row.metricKey))) {
                return row;
            }
        }

        return null;
    }

    private int sum(int[] values) {
        int total = 0;

        for (int value : values) {
            total += value;
        }

        return total;
    }

    private String value(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private void drawMultiline(Graphics2D g, String text, int x, int y, int maxWidth, int lineHeight) {
        if (text == null || text.isBlank()) return;

        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            String test = line.length() == 0 ? word : line + " " + word;

            if (g.getFontMetrics().stringWidth(test) > maxWidth) {
                g.drawString(line.toString(), x, y);
                y += lineHeight;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(test);
            }
        }

        if (line.length() > 0) {
            g.drawString(line.toString(), x, y);
        }
    }
}