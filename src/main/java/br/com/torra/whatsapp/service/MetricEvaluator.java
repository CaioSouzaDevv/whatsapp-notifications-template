package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.model.MetricsStatus;

public class MetricEvaluator {

    public MetricsStatus evoluate(double value, double target) {
        if (value >= target) {
            return MetricsStatus.GOOD;
        } else if (value >= target * 0.8) {
            return MetricsStatus.WARNING;
        } else {
            return MetricsStatus.BAD;
        }
    }

}
