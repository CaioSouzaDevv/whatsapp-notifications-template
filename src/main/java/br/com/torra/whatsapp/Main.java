package br.com.torra.whatsapp;

import br.com.torra.whatsapp.config.AppConfig;
import br.com.torra.whatsapp.service.WhatsAppSenderService;

public class Main {
    public static void main(String[] args) {

        WhatsAppSenderService sender = new WhatsAppSenderService();

        sender.sendTemplate(AppConfig.META_TEST_TO_PHONE);
    }
}