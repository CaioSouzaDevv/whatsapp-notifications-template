package com.torra.whatsapp.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

public class ImageBase64Util {

    private ImageBase64Util() {
    }

    public static String toBase64Png(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);

        String base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        return "data:image/png;base64," + base64;
    }
}
