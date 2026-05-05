package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.util.LogUtil;

public class NotificationProcessorService {

    public void process() {
        try {
            LogUtil.info("Gerando dashboard da diretoria...");

            new DashboardProcessorService().process();

            LogUtil.info("Dashboard gerado com sucesso.");

        } catch (Exception e) {
            LogUtil.error("Erro ao gerar dashboard da diretoria", e);
        }
    }
}