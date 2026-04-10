package br.com.torra.whatsapp;

import br.com.torra.whatsapp.service.NotificationProcessorService;
import br.com.torra.whatsapp.util.LogUtil;

public class Main {

    public static void main(String[] args) {
        try {
            NotificationProcessorService processor = new NotificationProcessorService();
            processor.process();
        } catch (Exception e) {
            LogUtil.error("Erro ao executar aplicação", e);
        }
    }
}