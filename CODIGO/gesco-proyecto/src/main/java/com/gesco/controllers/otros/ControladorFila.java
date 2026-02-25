package com.gesco.controllers.otros;

import java.io.File;
import java.time.LocalDate;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.menu.Menu;
import com.gesco.models.usuarios.Usuario.TipoUsuario;
import com.gesco.views.otros.VistaFila;

public class ControladorFila {

    private final VistaFila vista;
    private final String cedulaSesion;
    private final Runnable onBack;
    private final Runnable onVerMenu;

    public ControladorFila(
        VistaFila vista,
        String cedulaSesion,
        Runnable onBack,
        Runnable onVerMenu
    ) {
        this.vista = vista;
        this.cedulaSesion = DataBase.normalizarCedula(cedulaSesion);
        this.onBack = onBack;
        this.onVerMenu = onVerMenu;
    }

    public void conectar() {
        vista.getBtnBack().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });

        vista.getBtnVerMenu().addActionListener(e -> onVerMenu.run());
        vista.getBtnEntrar().addActionListener(e -> procesarEntrada());
        vista.getBtnSalir().addActionListener(e -> procesarSalida());
        vista.getBtnSeleccionar().addActionListener(e -> seleccionarArchivo());
        vista.getBtnCancelar().addActionListener(e -> vista.cerrarEmergente());
        vista.getBtnCobrar().addActionListener(e -> procesarCobro());
    }

    private void procesarEntrada() {
        if (vista.getEnFila() >= vista.getDisponible()) {
            JOptionPane.showMessageDialog(
                vista,
                "La fila está llena. Intente más tarde.",
                "Fila completa",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        vista.setEnFila(vista.getEnFila() + 1);
        actualizarCobro();
        vista.crearVentanaEmergente();
    }

    private void procesarSalida() {
        if (vista.getEnFila() <= 0) {
            JOptionPane.showMessageDialog(
                vista,
                "No está en la fila actualmente.",
                "Sin entrada",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        vista.setEnFila(vista.getEnFila() - 1);
    }

    private void seleccionarArchivo() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(vista);
        if (result == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            vista.setArchivoSeleccionado(archivo);
            vista.setNombreArchivoSeleccionado(archivo.getName());
        }
    }

    private void actualizarCobro() {
        double costoMenu = obtenerCostoMenuHoy();
        TipoUsuario tipoUsuario = obtenerTipoUsuarioSesion();
        double costoFinal = DataBase.calcularMontoCcbPorTipo(costoMenu, tipoUsuario);
        double ajuste = costoMenu > 0 ? ((1 - (costoFinal / costoMenu)) * 100.0) : 0.0;

        vista.setCobroInfo(costoMenu, ajuste, costoFinal);
    }

    private void procesarCobro() {
        if (vista.getArchivoSeleccionado() == null) {
            JOptionPane.showMessageDialog(
                vista,
                "Seleccione una imagen antes de cobrar.",
                "Imagen requerida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (cedulaSesion.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "No hay una cédula de sesión activa para cobrar.",
                "Sesión requerida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double costoFinal = vista.getCostoTotal();
        double saldoActual = DataBase.obtenerSaldo(cedulaSesion);
        if (saldoActual < costoFinal) {
            JOptionPane.showMessageDialog(
                vista,
                "Saldo insuficiente para completar el cobro.",
                "Saldo insuficiente",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean actualizado = DataBase.actualizarSaldo(cedulaSesion, saldoActual - costoFinal);
        if (!actualizado) {
            JOptionPane.showMessageDialog(
                vista,
                "No se pudo actualizar el saldo. Intente nuevamente.",
                "Cobro fallido",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
            vista,
            "Cobro realizado. Saldo restante: " + String.format("%.2f", saldoActual - costoFinal) + " Bs.",
            "Cobro exitoso",
            JOptionPane.INFORMATION_MESSAGE
        );
        vista.cerrarEmergente();
    }

    private double obtenerCostoMenuHoy() {
        Menu menu = DataBase.obtenerMenuPorFecha(LocalDate.now().toString());
        if (menu == null || menu.getEstado() == Menu.EstadoMenu.NO_DISPONIBLE || !menu.tienePlatillos()) {
            return 0.0;
        }
        return menu.getCostoMenu();
    }

    private TipoUsuario obtenerTipoUsuarioSesion() {
        if (cedulaSesion.isBlank()) {
            return TipoUsuario.COMENSAL;
        }
        return DataBase.obtenerTipoUsuario(cedulaSesion);
    }
}
