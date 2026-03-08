package com.gesco.controllers.costos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JOptionPane;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.views.costos.VistaRecargarSaldo;

public class ControladorRecargarSaldo {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final VistaRecargarSaldo vista;
    private final String cedulaSesion;
    private final Runnable onBack;
    private final Runnable onRecargaSuccess;

    public ControladorRecargarSaldo(
        VistaRecargarSaldo vista,
        String cedulaSesion,
        Runnable onBack,
        Runnable onRecargaSuccess
    ) {
        this.vista = vista;
        this.cedulaSesion = DataBase.normalizarCedula(cedulaSesion);
        this.onBack = onBack;
        this.onRecargaSuccess = onRecargaSuccess;
    }

    public void conectar() {
        vista.setCedula(cedulaSesion);

        vista.getSwitchPana().addActionListener(e -> {
        if (vista.getSwitchPana().isSelected()) {
            vista.getSwitchPana().setText("¡Modo Pana Activado!");
            vista.getSwitchPana().setBackground(new java.awt.Color(45, 120, 180));
            vista.getCedula().setEditable(true);
            vista.getCedula().setText("");
            vista.getCedula().requestFocus();
        } 
        else {
            vista.getSwitchPana().setText("¿Recargar a un Pana?");
            vista.getSwitchPana().setBackground(new java.awt.Color(30, 30, 35));
            vista.getCedula().setEditable(false);
            vista.getCedula().setText(cedulaSesion);
        }
    });
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });

        vista.getBotonRecargar().addActionListener(e -> procesarRecarga());
    }

    private void procesarRecarga() {
        String fecha = valor(vista.getFecha());
        String banco = valor(vista.getBanco());
        String referencia = valor(vista.getReferencia());
        String montoStr = valor(vista.getMonto());
        boolean modoPana = vista.getSwitchPana().isSelected();
        String cedulaDestino = modoPana
            ? DataBase.normalizarCedula(valor(vista.getCedula().getText()))
            : cedulaSesion;

        if (cedulaSesion.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "No hay una sesión activa para recargar saldo.",
                "Sesión requerida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (cedulaDestino.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "Debe indicar una cédula destino válida.",
                "Cédula requerida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!cedulaDestino.matches("\\d+")) {
            JOptionPane.showMessageDialog(
                vista,
                "La cédula debe contener solo números.",
                "Cédula inválida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!DataBase.cedulaYaRegistrada(cedulaDestino)) {
            JOptionPane.showMessageDialog(
                vista,
                "La cédula destino no se encuentra registrada.",
                "Usuario no encontrado",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (fecha.isBlank() || banco.isBlank() || referencia.isBlank() || montoStr.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "Debe completar fecha, banco, referencia y monto.",
                "Datos incompletos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if ("Seleccione un banco".equals(banco)) {
            JOptionPane.showMessageDialog(
                vista,
                "Debe seleccionar un banco de la lista.",
                "Banco requerido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!fechaTransaccionValida(fecha)) {
            JOptionPane.showMessageDialog(
                vista,
                "La fecha de la transacción debe estar dentro de los últimos 2 días.",
                "Fecha inválida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (referencia.length() != 20) {
            JOptionPane.showMessageDialog(
                vista,
                "El número de referencia debe ser de 20 dígitos.\nVerifique su transacción.",
                "Referencia inválida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!DataBase.referenciaBancariaValida(referencia)) {
            JOptionPane.showMessageDialog(
                vista,
                "Verifique su transacción.",
                "Referencia bancaria invalida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (DataBase.referenciaRecargaExiste(referencia)) {
            JOptionPane.showMessageDialog(
                vista,
                "La referencia ya fue registrada. Verifique e intente con una nueva transacción.",
                "Referencia duplicada",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr.replace(',', '.'));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                vista,
                "El monto debe ser numérico.",
                "Monto inválido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (monto <= 0) {
            JOptionPane.showMessageDialog(
                vista,
                "El monto debe ser mayor a 0.",
                "Monto inválido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (monto > 10351.0) {
        JOptionPane.showMessageDialog(
            vista,
            "El monto máximo permitido por recarga es 10.351 Bs (25 bs BCV).",
            "Límite de recarga excedido",
            JOptionPane.WARNING_MESSAGE
        );
            return;
        }

        double saldoActual = DataBase.obtenerSaldo(cedulaDestino);
        double nuevoSaldo = saldoActual + monto;
        boolean actualizado = DataBase.actualizarSaldo(cedulaDestino, nuevoSaldo);

        if (!actualizado) {
            JOptionPane.showMessageDialog(
                vista,
                "No se pudo actualizar el saldo. Intente nuevamente.",
                "Recarga fallida",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        boolean registrado = DataBase.registrarRecarga(referencia, monto, banco, fecha, cedulaSesion);
        if (!registrado) {
            JOptionPane.showMessageDialog(
                vista,
                "Saldo actualizado, pero no se pudo guardar el registro de la transacción.",
                "Consultar con su banco",
                JOptionPane.WARNING_MESSAGE
            );
        }

        JOptionPane.showMessageDialog(
            vista,
            "Recarga exitosa para la cédula " + cedulaDestino + ". Nuevo saldo: " + String.format("%.2f", nuevoSaldo) + " Bs.",
            "Recarga exitosa",
            JOptionPane.INFORMATION_MESSAGE
        );
        onRecargaSuccess.run();
    }

    private String valor(String texto) {
        return texto == null ? "" : texto.trim();
    }

    private boolean fechaTransaccionValida(String fechaTexto) {
        try {
            LocalDate fechaTransaccion = LocalDate.parse(fechaTexto, FORMATO_FECHA);
            LocalDate hoy = LocalDate.now();
            LocalDate limiteInferior = hoy.minusDays(2);
            return !fechaTransaccion.isAfter(hoy) && !fechaTransaccion.isBefore(limiteInferior);
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
