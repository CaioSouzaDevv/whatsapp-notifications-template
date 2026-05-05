package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.model.DashboardRow;

import java.util.List;

public class DashboardProcessorService {

    public void process() throws Exception {

        DashboardCsvReaderService csv = new DashboardCsvReaderService();
        DashboardImageGeneratorService image = new DashboardImageGeneratorService();

        List<DashboardRow> rows = csv.read();

        image.generate(rows);
    }
}