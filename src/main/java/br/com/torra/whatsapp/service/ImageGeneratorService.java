package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.model.StoreNotificationData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ImageGeneratorService {

    // =========================
    // 🔴 MÉTODO ANTIGO (WEBHOOK)
    // =========================
    public BufferedImage generateImage(
            String templatePath,
            String outputPath,
            String title,
            StoreNotificationData item) throws Exception {

        InputStream templateStream = getClass()
                .getClassLoader()
                .getResourceAsStream(templatePath);

        if (templateStream == null) {
            throw new RuntimeException("Template não encontrado: " + templatePath);
        }

        BufferedImage image = ImageIO.read(templateStream);

        Graphics2D g = image.createGraphics();

        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.setColor(Color.BLACK);
        g.drawString(item.getStoreName(), 50, 80);

        g.dispose();

        ImageIO.write(image, "png", new File(outputPath));

        return image;
    }

    // =========================
    // 🟢 NOVO (WHATSAPP)
    // =========================
    public BufferedImage generateWhatsAppImage(
            List<StoreNotificationData> dados,
            String coordenador
    ) throws Exception {

        List<StoreNotificationData> filtrados = filtrarPorCoordenador(dados, coordenador);

        int width = 1080;
        int height = 1350;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        drawHeader(g, coordenador);
        drawCards(g, filtrados);
        drawTopFiliais(g, filtrados);

        g.dispose();

        ImageIO.write(image, "png", new File("resultado.png"));

        return image;
    }

    private void drawHeader(Graphics2D g, String coordenador) {
        g.setColor(new Color(40, 40, 40));
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Meta HORA", 40, 60);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.setColor(new Color(80, 80, 80));
        g.drawString("Coordenador: " + coordenador, 40, 100);
    }

    private void drawCards(Graphics2D g, List<StoreNotificationData> dados) {

        StoreNotificationData exemplo = dados.isEmpty()
                ? new StoreNotificationData("", "", "", "", "", "", "", "", "")
                : dados.get(0);

        drawCard(g, 40, 140, "PCJ", exemplo.getPcjPercentual());
        drawCard(g, 380, 140, "Participação", exemplo.getParticipacaoPercentual());
        drawCard(g, 720, 140, "Aproveitamento", exemplo.getAproveitamentoPercentual());
    }

    private void drawCard(Graphics2D g, int x, int y, String titulo, String valor) {

        g.setColor(new Color(214, 234, 228));
        g.fillRoundRect(x, y, 300, 160, 20, 20);

        g.setColor(new Color(50, 65, 70));
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(titulo, x + 20, y + 35);

        g.setColor(new Color(230, 85, 45));
        g.setFont(new Font("Arial", Font.BOLD, 42));
        g.drawString(valor(valor), x + 20, y + 95);
    }

    private void drawTopFiliais(Graphics2D g, List<StoreNotificationData> dados) {

        List<StoreNotificationData> top = topFiliais(dados, 5);

        int y = 360;

        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.BLACK);
        g.drawString("Top Filiais", 40, y);

        y += 40;

        g.setFont(new Font("Arial", Font.PLAIN, 20));

        for (StoreNotificationData item : top) {

            g.drawString(valor(item.getStoreName()), 40, y);
            g.drawString("PCJ: " + valor(item.getPcjPercentual()), 400, y);
            g.drawString("Aprov: " + valor(item.getAprovados()), 650, y);

            y += 35;
        }
    }

    private List<StoreNotificationData> filtrarPorCoordenador(List<StoreNotificationData> dados, String coordenador) {
        List<StoreNotificationData> result = new ArrayList<>();

        for (StoreNotificationData item : dados) {
            if (coordenador.equalsIgnoreCase(valor(item.getCoordenadorCartao()))) {
                result.add(item);
            }
        }

        return result;
    }

    private List<StoreNotificationData> topFiliais(List<StoreNotificationData> dados, int limit) {

        return dados.stream()
                .filter(d -> d.getStoreName() != null && !d.getStoreName().isBlank())
                .sorted(Comparator.comparingDouble(this::safePcj).reversed())
                .limit(limit)
                .toList();
    }

    private double safePcj(StoreNotificationData d) {
        try {
            return Double.parseDouble(valor(d.getPcjPercentual()).replace("%", "").replace(",", "."));
        } catch (Exception e) {
            return 0;
        }
    }

    private String valor(String v) {
        return (v == null || v.isBlank()) ? "-" : v;
    }
}