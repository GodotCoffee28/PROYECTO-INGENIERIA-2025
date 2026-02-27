package com.gesco.models.costos;

import java.time.*;

/*
CCB: Valor del Costo Cubierto por bandeja
CF: Costos Fijos totales del servicio (ej. mano de obra, mantenimiento, alquiler).
CV: Costos Variables totales por servicio (ej. insumos, materiales). Grupo Docente INGENIERÍA DE SOFTWARE
2025 1
NB: Número de bandejas proyectadas o servidas en un periodo
%_Merma: Porcentaje de desecho o merma de alimentos.

𝐶𝐶𝐵 = [(𝐶𝐹+𝐶𝑉)/𝑁𝐵]*(1+%𝑀𝑒𝑟𝑚𝑎)
*/

public class CCB {
    private static final double LIMITE_COSTO = 10_000.0;

    private final LocalDate fecha;
    private final String tipoUsuario;
    private final double ccb, cf, cv, nb, merma;

    public CCB(LocalDate fecha, String tipoUsuario, double cf, double cv, double nb, double merma) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser null.");
        }
        if (Double.isNaN(nb) || Double.isInfinite(nb) || nb <= 0) {
            throw new IllegalArgumentException("NB debe ser mayor que cero.");
        }
        if (Double.isNaN(merma) || Double.isInfinite(merma) || merma < 0 || merma > 100) {
            throw new IllegalArgumentException("MERMA debe estar entre 0 y 100.");
        }
        if (Double.isNaN(cf) || Double.isInfinite(cf) || cf < 0 || cf > LIMITE_COSTO) {
            throw new IllegalArgumentException("CF debe estar entre 0 y 10000.");
        }
        if (Double.isNaN(cv) || Double.isInfinite(cv) || cv < 0 || cv > LIMITE_COSTO) {
            throw new IllegalArgumentException("CV debe estar entre 0 y 10000.");
        }
        this.fecha = fecha;
        this.tipoUsuario = tipoUsuario == null ? "" : tipoUsuario.trim();
        this.cf = cf;
        this.cv = cv;
        this.nb = nb;
        this.merma = merma;
        double valorCalculado = calcularCCB();
        this.ccb = Math.min(valorCalculado, 20702.0);
}

    private double calcularCCB() {
        if (nb <= 0) {
            throw new IllegalArgumentException("El número de bandejas debe ser mayor que cero.");
        }
        return ((cf + cv) / nb) * (1 + merma);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public double getCcb() {
        return ccb;
    }

    public double getCf() {
        return cf;
    }

    public double getCv() {
        return cv;
    }

    public double getNb() {
        return nb;
    }

    public double getMerma() {
        return merma;
    }

    @Override
    public String toString() {
        String usuario = tipoUsuario.isEmpty() ? "N/A" : tipoUsuario;
        return "Tipo: " + usuario
                + ", NB: " + nb
                + ", Merma: " + merma
                + ", CF: " + cf
                + ", CV: " + cv
                + ", CCB: " + ccb;
    }
}

