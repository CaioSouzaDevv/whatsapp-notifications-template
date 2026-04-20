package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.model.MetricStatus;
import br.com.torra.whatsapp.model.StoreNotificationData;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

public class ImageGeneratorService {

    private final MetricEvaluator metricEvaluator;

    public ImageGeneratorService() {
        this.metricEvaluator = new MetricEvaluator();
    }

    public BufferedImage generateImage(
            String templatePath,
            String outputPath,
            String title,
            StoreNotificationData item) throws Exception {

        InputStream templateStream = getClass()
                .getClassLoader()
                .getResourceAsStream(templatePath);

        if (templateStream == null) {
            throw new RuntimeException("Template não encontrado em: " + templatePath);
        }

        BufferedImage bufferedImage = ImageIO.read(templateStream);

        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawHeaderStoreName(graphics, "Meta " + safeValue(item.getStoreName()));
        drawMetrics(graphics, item);

        graphics.dispose();

        ImageIO.write(bufferedImage, "png", new File(outputPath));

        return bufferedImage;
    }

    private void drawHeaderStoreName(Graphics2D graphics, String storeName) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Roboto", Font.BOLD, 36));
        graphics.drawString(storeName, 400, 150);
    }

    private void drawMetrics(Graphics2D graphics, StoreNotificationData item) {
        int startY = 430;
        int lineHeight = 48;

        int labelX = 300;
        int valueX = 690;
        int arrowX = valueX - 40; // seta à esquerda do valor

        Color primaryColor = new Color(255, 81, 1);
        Font labelFont = new Font("Roboto", Font.BOLD, 28);
        Font valueFont = new Font("Roboto", Font.BOLD, 28);

        graphics.setColor(primaryColor);

        drawMetricLine(
                graphics,
                "Meta Ativados:",
                item.getMetaAtivados(),
                labelFont,
                valueFont,
                labelX,
                valueX,
                arrowX,
                startY,
                null,
                null);

        drawMetricLine(
                graphics,
                "Aprovações:",
                item.getQtdAprovacoes(),
                labelFont,
                valueFont,
                labelX,
                valueX,
                arrowX,
                startY += lineHeight,
                safeParse(item.getQtdAprovacoes()),
                safeParse(item.getMetaAtivados()));

        drawMetricLine(
                graphics,
                "Aprovações LY:",
                item.getQtdAprovacoesLy(),
                labelFont,
                valueFont,
                labelX,
                valueX,
                arrowX,
                startY += lineHeight,
                safeParse(item.getQtdAprovacoesLy()),
                safeParse(item.getQtdAprovacoes()));

        drawMetricLine(
                graphics,
                "Propostas:",
                item.getQtdPropostas(),
                labelFont,
                valueFont,
                labelX,
                valueX,
                arrowX,
                startY += lineHeight,
                null,
                null);

        startY += 34;

        drawMetricLine(
                graphics,
                "Meta PCJ:",
                item.getMetaPadraoPcj(),
                labelFont,
                valueFont,
                labelX,
                valueX,
                arrowX,
                startY += lineHeight,
                null,
                null);

        drawMetricLine(
                graphics,
                "Meta Participação:",
                item.getMetaPadraoParticipacao(),
                labelFont,
                valueFont,
                labelX,
                valueX,
                arrowX,
                startY += lineHeight,
                null,
                null);
    }

    private void drawMetricLine(
            Graphics2D graphics,
            String label,
            String value,
            Font labelFont,
            Font valueFont,
            int xLabel,
            int xValue,
            int xArrow,
            int y,
            Double actualValue,
            Double targetValue) {

        graphics.setColor(new Color(255, 81, 1));

        graphics.setFont(labelFont);
        graphics.drawString(label, xLabel, y);

        graphics.setFont(valueFont);
        graphics.drawString(safeValue(value), xValue, y);

        if (actualValue != null && targetValue != null) {
            MetricStatus status = metricEvaluator.evaluate(actualValue, targetValue);
            drawStatusArrow(graphics, status, xArrow, y - 25);
        }
    }

    private void drawStatusArrow(Graphics2D graphics, MetricStatus status, int x, int y) {
        Color green = new Color(34, 177, 76);
        Color red = new Color(220, 53, 69);

        if (status == MetricStatus.GOOD) {
            graphics.setColor(green);
            drawUpArrow(graphics, x, y, 22, 22);
            return;
        }

        graphics.setColor(red);
        drawDownArrow(graphics, x, y, 22, 22);
    }

    private void drawUpArrow(Graphics2D graphics, int x, int y, int width, int height) {
        graphics.setStroke(new BasicStroke(5));

        int centerX = x + (width / 1);
        int topY = y;
        int bottomY = y + height;

        graphics.drawLine(centerX, bottomY, centerX, y + 10);

        int[] arrowHeadX = { centerX, centerX - 8, centerX + 8 };
        int[] arrowHeadY = { topY, topY + 10, topY + 10 };
        graphics.fillPolygon(arrowHeadX, arrowHeadY, 3);
    }

    private void drawDownArrow(Graphics2D graphics, int x, int y, int width, int height) {
        graphics.setStroke(new BasicStroke(5));

        int centerX = x + (width / 2);
        int topY = y;
        int bottomY = y + height;

        graphics.drawLine(centerX, topY, centerX, bottomY - 10);

        int[] arrowHeadX = { centerX, centerX - 8, centerX + 8 };
        int[] arrowHeadY = { bottomY, bottomY - 10, bottomY - 10 };
        graphics.fillPolygon(arrowHeadX, arrowHeadY, 3);
    }

    private Double safeParse(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            String normalized = value.trim().replace(",", ".");
            return Double.parseDouble(normalized);
        } catch (Exception e) {
            return null;
        }
    }

    private String safeValue(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}