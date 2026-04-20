package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.model.MetricStatus;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

public class IconService {

    private final Map<MetricStatus, BufferedImage> icons = new EnumMap<>(MetricStatus.class);

    public IconService() {
        icons.put(MetricStatus.GOOD, loadIcon("icons/up.png"));
        icons.put(MetricStatus.WARNING, loadIcon("icons/warning.png"));
        icons.put(MetricStatus.BAD, loadIcon("icons/down.png"));
    }

    public BufferedImage get(MetricStatus status) {
        return icons.get(status);
    }

    private BufferedImage loadIcon(String path) {
        try {
            InputStream stream = getClass()
                    .getClassLoader()
                    .getResourceAsStream(path);

            if (stream == null) {
                throw new RuntimeException("Ícone não encontrado: " + path);
            }

            return ImageIO.read(stream);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar ícone: " + path, e);
        }
    }
}