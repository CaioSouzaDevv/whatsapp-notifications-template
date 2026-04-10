package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.model.StoreNotificationData;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageGeneratorService {

    public BufferedImage generateImage(
            String templatePath,
            String outputPath,
            String title,
            StoreNotificationData item) throws IOException {

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

        drawHeaderStoreName(graphics, "Meta" + " " + item.getStoreName());
        drawMetrics(graphics, item);

        graphics.dispose();

        ImageIO.write(bufferedImage, "png", new File(outputPath));

        return bufferedImage;
    }

    private void drawHeaderStoreName(Graphics2D graphics, String storeName) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("SansSerif", Font.BOLD, 36));
        graphics.drawString(storeName, 400, 150);
    }

    private void drawMetrics(Graphics2D graphics, StoreNotificationData item) {
        int startY = 430;
        int lineHeight = 48;

        int labelX = 300;
        int valueX = 690;

        Color primaryColor = new Color(255, 81, 1);
        Font labelFont = new Font("SansSerif", Font.BOLD, 28);
        Font valueFont = new Font("SansSerif", Font.BOLD, 28);

        graphics.setColor(primaryColor);

        drawLine(graphics, "Meta Ativados:", item.getMetaAtivados(), labelFont, valueFont, labelX, valueX, startY);

        drawLine(graphics, "Aprovações:", item.getQtdAprovacoes(), labelFont, valueFont, labelX, valueX,
                startY += lineHeight);

        drawLine(graphics, "Aprovações LY:", item.getQtdAprovacoesLy(), labelFont, valueFont, labelX, valueX,
                startY += lineHeight);

        drawLine(graphics, "Propostas:", item.getQtdPropostas(), labelFont, valueFont, labelX, valueX,
                startY += lineHeight);

        startY += 34;

        drawLine(graphics, "Meta PCJ:", item.getMetaPadraoPcj(), labelFont, valueFont, labelX, valueX,
                startY += lineHeight);

        drawLine(graphics, "Meta Participação:", item.getMetaPadraoParticipacao(), labelFont, valueFont, labelX, valueX,
                startY += lineHeight);
    }

    private void drawLine(
            Graphics2D graphics,
            String label,
            String value,
            Font labelFont,
            Font valueFont,
            int xLabel,
            int xValue,
            int y) {
        graphics.setFont(labelFont);
        graphics.drawString(label, xLabel, y);

        graphics.setFont(valueFont);
        graphics.drawString(safeValue(value), xValue, y);
    }

    private String safeValue(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}