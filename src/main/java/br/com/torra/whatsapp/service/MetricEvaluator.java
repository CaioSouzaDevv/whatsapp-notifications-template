package br.com.torra.whatsapp.service;

import br.com.torra.whatsapp.model.MetricStatus;;

public class MetricEvaluator {

    public MetricStatus evaluate(double value, double target) {
        if (value >= target) {
            return MetricStatus.GOOD;
        }

        if (value >= target * 0.8) {
            return MetricStatus.WARNING;
        }

        return MetricStatus.BAD;
    }
}