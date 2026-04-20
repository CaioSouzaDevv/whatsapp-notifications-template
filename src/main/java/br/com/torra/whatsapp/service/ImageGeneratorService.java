package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.model.MetricStatus;
import br.com.torra.whatsapp.model.StoreNotificationData;

import javax.imageio.ImageIO;
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
        int valueX = 630;
        int arrowX = valueX + 38;

        

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
            drawStatusArrow(graphics, status, xArrow, y);
        }
    }

    private void drawStatusArrow(Graphics2D graphics, MetricStatus status, int x, int y) {
        Color green = new Color(34, 177, 76);
        Color red = new Color(220, 53, 69);

        Font arrowFont = new Font("Arial", Font.BOLD, 24);
        graphics.setFont(arrowFont);

        if (status == MetricStatus.GOOD) {
   
            graphics.setColor(green);
            graphics.drawString("▲", x, y - 2);
            return;
        }

        graphics.setColor(red);
        graphics.drawString("▼", x, y);
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