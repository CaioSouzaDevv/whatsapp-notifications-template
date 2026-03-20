package com.torra.whatsapp.config.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageGeneratorService {
    public BufferedImage generateImage(String templatePath, String outputPath, String title, String name,
            String message)

            throws IOException {

        int maxWidth = 500;
        int y = 460;

        BufferedImage bufferedImage = ImageIO.read(new File(templatePath));

        Graphics graphics = bufferedImage.getGraphics();

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial Black", Font.PLAIN, 36));
        graphics.drawString(title, 420, 160);

        graphics.setColor(new Color(255, 81, 1));
        graphics.setFont(new Font("Arial Black", Font.PLAIN, 36));
        graphics.drawString(name, 310, 360);

        graphics.setColor(new Color(255, 81, 1));
        graphics.setFont(new Font("Arial Black", Font.PLAIN, 30));

        FontMetrics metrics = graphics.getFontMetrics();

        String[] words = message.split("\\s+");
        String currentLine = "";

        for (String word : words) {
            String testLine = currentLine + word + " ";

            if (metrics.stringWidth(testLine) >= maxWidth) {
                graphics.drawString(currentLine, 310, y);
                y += 40;
                currentLine = word + " ";
            } else {
                currentLine = testLine;
            }
        }

        if (!currentLine.isEmpty()) {
            graphics.drawString(currentLine, 310, y);
        }

        graphics.dispose();

        ImageIO.write(bufferedImage, "png", new File(outputPath));

        return bufferedImage;

    }

}