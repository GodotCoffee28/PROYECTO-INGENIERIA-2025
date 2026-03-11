package com.gesco.controllers.menu;

import javax.swing.JOptionPane;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.views.menu.VistaAgregarInsumo;

public class ControladorAgregarInsumo {

    private final VistaAgregarInsumo vista;
    private final Runnable onBack;

    public ControladorAgregarInsumo(VistaAgregarInsumo vista, Runnable onBack) {
        this.vista = vista;
        this.onBack = onBack;
    }

    public void conectar() {
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });

        vista.getBtnCrear().addActionListener(e -> guardarInsumo());
    }

    private void guardarInsumo() {
        String nombre    = vista.getNombreInsumo().trim();
        String cantStr   = vista.getCantidadInsumo().trim();
        String tipo      = vista.getTipoNutricional().trim();
        String precioStr = vista.getPrecioUnitario().trim();

        if (nombre.isBlank() || cantStr.isBlank() || tipo.isBlank() || precioStr.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "Debe completar todos los campos.",
                "Datos incompletos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$")) {
            JOptionPane.showMessageDialog(
                vista,
                "El nombre del insumo solo debe contener letras.",
                "Nombre inválido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!tipo.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$")) {
            JOptionPane.showMessageDialog(
                vista,
                "El tipo nutricional solo debe contener letras.",
                "Tipo inválido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantStr);
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                vista,
                "La cantidad debe ser un número entero positivo.",
                "Cantidad inválida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (cantidad > DataBase.CANTIDAD_MAXIMA_INSUMO) {
            JOptionPane.showMessageDialog(
                vista,
                "La cantidad no puede ser mayor a " + DataBase.CANTIDAD_MAXIMA_INSUMO + ".",
                "Cantidad inválida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        float precio;
        try {
            precio = Float.parseFloat(precioStr.replace(',', '.'));
            if (precio < 1.0f) {
                JOptionPane.showMessageDialog(
                    vista,
                    "No se puede ingresar un valor menor a 1.",
                    "Precio inválido",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                vista,
                "El precio unitario debe ser un número válido.",
                "Precio inválido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!Float.isFinite(precio) || precio > DataBase.PRECIO_MAXIMO_INSUMO) {
            JOptionPane.showMessageDialog(
                vista,
                "El precio unitario no puede ser mayor a " + DataBase.PRECIO_MAXIMO_INSUMO + ".",
                "Precio inválido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        boolean ok = DataBase.guardarOActualizarInsumo(nombre, cantidad, tipo, precio);

        if (ok) {
            JOptionPane.showMessageDialog(
                vista,
                "Insumo guardado correctamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );
            onBack.run();
        } else {
            JOptionPane.showMessageDialog(
                vista,
                "No se pudo guardar el insumo. Intente nuevamente.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
