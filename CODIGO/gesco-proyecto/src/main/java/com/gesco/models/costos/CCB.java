package com.gesco.models.costos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import com.gesco.models.usuarios.Usuario.TipoUsuario;

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
    private static final double NB_MAXIMO_EXCLUSIVO = 2500.0;
    private static final double CCB_ESTUDIANTE_MIN = 0.20;
    private static final double CCB_ESTUDIANTE_MAX = 0.30;
    private static final double CCB_BECARIO_MIN = 0.00;
    private static final double CCB_BECARIO_MAX = 0.30;
    private static final double CCB_BECARIO_POR_DEFECTO = 0.05;
    private static final double CCB_EXONERADO_PORCENTAJE = 0.00;
    private static final double CCB_PROFESOR_MIN = 0.70;
    private static final double CCB_PROFESOR_MAX = 0.90;
    private static final double CCB_EMPLEADO_MIN = 0.90;
    private static final double CCB_EMPLEADO_MAX = 1.10;

    private final LocalDate fecha;
    private final String tipoUsuario;
    private final double porcentajeAplicado;
    private final double ccb, cf, cv, nb, merma;

    public CCB(LocalDate fecha, String tipoUsuario, double cf, double cv, double nb, double merma) {
        this(fecha, tipoUsuario, cf, cv, nb, merma, obtenerPorcentajeDeterministicoPorTipo(parseTipoUsuario(tipoUsuario)));
    }

    public CCB(LocalDate fecha, String tipoUsuario, double cf, double cv, double nb, double merma, double porcentajeAplicado) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser null.");
        }
        if (Double.isNaN(nb) || Double.isInfinite(nb) || nb <= 0 || nb >= NB_MAXIMO_EXCLUSIVO) {
            throw new IllegalArgumentException("NB debe ser mayor que cero y menor a 2500.");
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
        TipoUsuario tipoNormalizado = parseTipoUsuario(this.tipoUsuario);
        validarPorcentajeEnRango(tipoNormalizado, porcentajeAplicado);
        this.porcentajeAplicado = redondearMoneda(porcentajeAplicado);
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
        return ((cf + cv) / nb) * (1 + merma / 100.0);
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

    public double getPorcentajeAplicado() {
        return porcentajeAplicado;
    }

    public static double calcularMontoPorTipo(double ccbBase, TipoUsuario tipoUsuario) {
        if (ccbBase < 0) {
            throw new IllegalArgumentException("El CCB base no puede ser negativo.");
        }

        double porcentaje = obtenerPorcentajeDeterministicoPorTipo(tipoUsuario);
        return redondearMoneda(ccbBase * porcentaje);
    }

    public static double generarPorcentajePorTipo(TipoUsuario tipoUsuario) {
        double[] rango = obtenerRangoPorTipoUsuario(tipoUsuario);
        if (rango[0] == rango[1]) {
            return redondearMoneda(rango[0]);
        }

        double porcentaje = rango[0] + (Math.random() * (rango[1] - rango[0]));
        return redondearMoneda(porcentaje);
    }

    public static double calcularMontoPorTipo(double ccbBase, TipoUsuario tipoUsuario, double porcentajeCcb) {
        if (ccbBase < 0) {
            throw new IllegalArgumentException("El CCB base no puede ser negativo.");
        }

        if (tipoUsuario == TipoUsuario.EXONERADO) {
            return 0.0;
        }

        if (tipoUsuario == TipoUsuario.BECARIO) {
            validarPorcentajeBecario(porcentajeCcb);
            return redondearMoneda(ccbBase * porcentajeCcb);
        }

        double[] rango = obtenerRangoPorTipoUsuario(tipoUsuario);
        if (porcentajeCcb < rango[0] || porcentajeCcb > rango[1]) {
            throw new IllegalArgumentException("El porcentaje no esta en el rango permitido para el tipo de usuario.");
        }

        return redondearMoneda(ccbBase * porcentajeCcb);
    }

    public static double normalizarPorcentajeParaTipo(TipoUsuario tipoUsuario, double porcentajeSugerido) {
        if (tipoUsuario == TipoUsuario.BECARIO) {
            if (porcentajeSugerido >= CCB_BECARIO_MIN && porcentajeSugerido <= CCB_BECARIO_MAX) {
                return redondearMoneda(porcentajeSugerido);
            }
            return CCB_BECARIO_POR_DEFECTO;
        }
        if (tipoUsuario == TipoUsuario.EXONERADO) {
            return CCB_EXONERADO_PORCENTAJE;
        }

        double[] rango = obtenerRangoPorTipoUsuario(tipoUsuario);
        if (porcentajeSugerido >= rango[0] && porcentajeSugerido <= rango[1]) {
            return redondearMoneda(porcentajeSugerido);
        }
        return redondearMoneda(obtenerPorcentajeDeterministicoPorTipo(tipoUsuario));
    }

    public static TipoUsuario parseTipoUsuario(String tipoUsuarioTexto) {
        if (tipoUsuarioTexto == null || tipoUsuarioTexto.isBlank()) {
            return null;
        }

        String normalizado = tipoUsuarioTexto.trim().toUpperCase();
        return switch (normalizado) {
            case "ESTUDIANTE" -> TipoUsuario.ESTUDIANTE;
            case "BECARIO" -> TipoUsuario.BECARIO;
            case "EXONERADO" -> TipoUsuario.EXONERADO;
            case "PROFESOR" -> TipoUsuario.PROFESOR;
            case "EMPLEADO" -> TipoUsuario.EMPLEADO;
            case "ADMIN", "SUPER_ADMIN", "SUPER ADMIN" -> TipoUsuario.ADMIN;
            default -> null;
        };
    }

    public static boolean coincideTipoCcb(String tipoGuardado, TipoUsuario tipoUsuario) {
        TipoUsuario tipoRegistro = parseTipoUsuario(tipoGuardado);
        if (tipoRegistro == null || tipoUsuario == null) {
            return false;
        }

        if (esBeneficioEstudiantil(tipoRegistro) && esBeneficioEstudiantil(tipoUsuario)) {
            return true;
        }

        if (tipoRegistro == tipoUsuario) {
            return true;
        }

        // Compatibilidad: tipos laborales comparten CCB (empleado/admin)
        return esTipoLaboral(tipoRegistro) && esTipoLaboral(tipoUsuario);
    }

    private static boolean esTipoLaboral(TipoUsuario tipoUsuario) {
        return tipoUsuario == TipoUsuario.EMPLEADO
            || tipoUsuario == TipoUsuario.ADMIN;
    }

    public static double[] obtenerRangoPorTipoUsuario(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            return new double[] { 1.0, 1.0 };
        }

        return switch (tipoUsuario) {
            case ESTUDIANTE -> new double[] { CCB_ESTUDIANTE_MIN, CCB_ESTUDIANTE_MAX };
            case BECARIO -> new double[] { CCB_BECARIO_MIN, CCB_BECARIO_MAX };
            case EXONERADO -> new double[] { CCB_EXONERADO_PORCENTAJE, CCB_EXONERADO_PORCENTAJE };
            case PROFESOR -> new double[] { CCB_PROFESOR_MIN, CCB_PROFESOR_MAX };
            case EMPLEADO, ADMIN -> new double[] { CCB_EMPLEADO_MIN, CCB_EMPLEADO_MAX };
            default -> new double[] { 1.0, 1.0 };
        };
    }

    public static String normalizarTipoConfiguracion(String tipoUsuarioTexto) {
        TipoUsuario tipo = parseTipoUsuario(tipoUsuarioTexto);
        if (tipo == null) {
            return tipoUsuarioTexto == null ? "" : tipoUsuarioTexto.trim();
        }

        TipoUsuario tipoNormalizado = esBeneficioEstudiantil(tipo) ? TipoUsuario.ESTUDIANTE : tipo;
        return tipoNormalizado.toString().charAt(0) + tipoNormalizado.toString().substring(1).toLowerCase();
    }

    private static double obtenerPorcentajeDeterministicoPorTipo(TipoUsuario tipoUsuario) {
        double[] rango = obtenerRangoPorTipoUsuario(tipoUsuario);
        if (rango[0] == rango[1]) {
            return rango[0];
        }
        return (rango[0] + rango[1]) / 2.0;
    }

    private static void validarPorcentajeEnRango(TipoUsuario tipoUsuario, double porcentaje) {
        if (tipoUsuario == TipoUsuario.BECARIO) {
            validarPorcentajeBecario(porcentaje);
            return;
        }

        if (tipoUsuario == TipoUsuario.EXONERADO) {
            if (redondearMoneda(porcentaje) != CCB_EXONERADO_PORCENTAJE) {
                throw new IllegalArgumentException("El usuario exonerado no debe pagar porcentaje de CCB.");
            }
            return;
        }

        double[] rango = obtenerRangoPorTipoUsuario(tipoUsuario);
        if (porcentaje < rango[0] || porcentaje > rango[1]) {
            throw new IllegalArgumentException(
                String.format(
                    "El porcentaje debe estar entre %.2f%% y %.2f%% para el tipo seleccionado.",
                    rango[0] * 100.0,
                    rango[1] * 100.0
                )
            );
        }
    }

    private static boolean esBeneficioEstudiantil(TipoUsuario tipoUsuario) {
        return tipoUsuario == TipoUsuario.ESTUDIANTE
            || tipoUsuario == TipoUsuario.BECARIO
            || tipoUsuario == TipoUsuario.EXONERADO;
    }

    private static void validarPorcentajeBecario(double porcentajeAplicado) {
        if (porcentajeAplicado < CCB_BECARIO_MIN || porcentajeAplicado > CCB_BECARIO_MAX) {
            throw new IllegalArgumentException("El porcentaje del becario debe estar entre 0% y 30%.");
        }
    }

    private static double redondearMoneda(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP).doubleValue();
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

