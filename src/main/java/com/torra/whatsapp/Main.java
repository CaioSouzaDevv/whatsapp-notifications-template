package com.torra.whatsapp;

import com.torra.whatsapp.service.NotificationProcessorService;

public class Main {

    public static void main(String[] args) {
        try {
            NotificationProcessorService processor = new NotificationProcessorService();
            processor.process();
        } catch (Exception e) {
            System.err.println("Erro ao executar aplicação");
            e.printStackTrace();
        }
    }
}