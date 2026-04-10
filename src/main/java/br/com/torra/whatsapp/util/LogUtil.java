package br.com.torra.whatsapp.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void info(String message) {
        System.out.println("[INFO] " + now() + " - " + message);
    }

    public static void error(String message, Exception e) {
        System.err.println("[ERROR] " + now() + " - " + message);
        e.printStackTrace();
    }

    private static String now() {
        return sdf.format(new Date());
    }
}