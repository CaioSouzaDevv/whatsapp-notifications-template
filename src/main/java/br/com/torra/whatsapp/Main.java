package br.com.torra.whatsapp;

import br.com.torra.whatsapp.service.NotificationProcessorService;

public class Main {
    public static void main(String[] args) throws Exception {
        new NotificationProcessorService().process();
    }
}