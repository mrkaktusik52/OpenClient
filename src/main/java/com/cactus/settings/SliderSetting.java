package com.cactus.settings;

public class SliderSetting extends Setting<Double> {

    private final double min;
    private final double max;
    private final double step;

    public SliderSetting(
            String name,
            String id,
            double defaultValue,
            double min,
            double max,
            double step
    ) {
        super(name, id, defaultValue);

        this.min = min;
        this.max = max;
        this.step = step;
    }

    @Override
    public void setValue(Double value) {
        double stepped = Math.round(value / step) * step;
        double clamped = Math.max(min, Math.min(max, stepped));

        super.setValue(clamped);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getStep() {
        return step;
    }
}