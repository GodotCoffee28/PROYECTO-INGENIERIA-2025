package com.gesco.models;

public class CFCV {
    private double cf;
    private double cv;

    public CFCV() {
        this(0.0, 0.0);
    }

    public CFCV(double cf, double cv) {
        this.cf = cf;
        this.cv = cv;
    }

    public double getCf() { return cf; }
    public double getCv() { return cv; }

    public void setCf(double cf) { this.cf = cf; }
    public void setCv(double cv) { this.cv = cv; }

    public String toLine() {
        return String.format("%.2f:%.2f", cf, cv);
    }

    public static CFCV fromLine(String line) {
        if (line == null || line.isBlank()) return new CFCV();
        String[] parts = line.trim().split(":");
        try {
            double cf = parts.length > 0 ? Double.parseDouble(parts[0].replace(',', '.')) : 0.0;
            double cv = parts.length > 1 ? Double.parseDouble(parts[1].replace(',', '.')) : 0.0;
            return new CFCV(cf, cv);
        } catch (NumberFormatException e) {
            return new CFCV();
        }
    }
}
