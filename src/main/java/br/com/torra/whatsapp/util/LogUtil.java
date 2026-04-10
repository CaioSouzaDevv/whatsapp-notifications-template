package br.com.torra.whatsapp.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtil {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LogUtil() {
    }

    public static void info(String message) {
        System.out.println(format("INFO", message));
    }

    public static void warn(String message) {
        System.out.println(format("WARN", message));
    }

    public static void error(String message) {
        System.err.println(format("ERROR", message));
    }

    public static void error(String message, Exception exception) {
        System.err.println(format("ERROR", message));
        exception.printStackTrace();
    }

    private static String format(String level, String message) {
        return "[" + level + "] "
                + LocalDateTime.now().format(FORMATTER)
                + " - "
                + message;
    }
}