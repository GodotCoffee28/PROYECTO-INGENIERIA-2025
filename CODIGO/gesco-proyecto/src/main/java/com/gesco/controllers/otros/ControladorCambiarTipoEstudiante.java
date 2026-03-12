package com.gesco.controllers.otros;

import javax.swing.JOptionPane;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.usuarios.Usuario.TipoUsuario;
import com.gesco.views.otros.VistaCambiarTipoEstudiante;

public class ControladorCambiarTipoEstudiante {

    private final VistaCambiarTipoEstudiante vista;
    private final Runnable onBack;

    public ControladorCambiarTipoEstudiante(VistaCambiarTipoEstudiante vista, Runnable onBack) {
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

        vista.getBtnCambiar().addActionListener(e -> procesarCambioTipo());
        vista.actualizarEstadoPorcentajeBecario();
    }

    private void procesarCambioTipo() {
        String cedula = DataBase.normalizarCedula(vista.getCedula());
        if (cedula.isBlank()) {
            JOptionPane.showMessageDialog(
                vista,
                "Debe ingresar una cédula.",
                "Cédula requerida",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        TipoUsuario tipoActual = DataBase.obtenerTipoUsuarioSecretaria(cedula);
        if (tipoActual == null) {
            JOptionPane.showMessageDialog(
                vista,
                "La cédula no existe en el padrón de secretaría.",
                "Cédula no encontrada",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!esTipoEstudiante(tipoActual)) {
            JOptionPane.showMessageDialog(
                vista,
                "El usuario solicitado no es un estudiante/exonerado/becario.",
                "Tipo no permitido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        TipoUsuario nuevoTipo = vista.getTipoUsuarioSeleccionado();
        if (nuevoTipo == null) {
            JOptionPane.showMessageDialog(
                vista,
                "Debe seleccionar un tipo de usuario válido.",
                "Tipo inválido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Double porcentajeBecario = null;
        if (nuevoTipo == TipoUsuario.BECARIO) {
            double porcentajeEstudianteHoy = DataBase.obtenerPorcentajeCcbEstudiantilHoy();
            if (porcentajeEstudianteHoy <= 0.0) {
                JOptionPane.showMessageDialog(
                    vista,
                    "Primero debe existir un CCB de estudiante regular configurado para hoy.",
                    "CCB estudiantil no disponible",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String valorPorcentaje = vista.getPorcentajeBecario();
            if (valorPorcentaje == null || valorPorcentaje.isBlank()) {
                JOptionPane.showMessageDialog(
                    vista,
                    "Debe indicar el % individual del becario.",
                    "% de becario requerido",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            try {
                porcentajeBecario = Double.parseDouble(valorPorcentaje.trim().replace(',', '.')) / 100.0;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                    vista,
                    "El % individual del becario debe ser numérico.",
                    "% inválido",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (!DataBase.esPorcentajeBecarioValidoParaHoy(porcentajeBecario)) {
                JOptionPane.showMessageDialog(
                    vista,
                    "El % del becario debe ser menor al % del estudiante regular de hoy (" + String.format("%.2f", porcentajeEstudianteHoy * 100.0) + "% ).",
                    "% de becario inválido",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        }

        boolean actualizado = DataBase.cambiarTipoUsuarioPorCedula(cedula, nuevoTipo, porcentajeBecario);
        if (!actualizado) {
            JOptionPane.showMessageDialog(
                vista,
                "No se pudo actualizar el tipo. Verifique si la cédula es válida y que el cambio cumpla las reglas del porcentaje becario.",
                "Error al actualizar",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        JOptionPane.showMessageDialog(
            vista,
            "Tipo actualizado correctamente para la cédula " + cedula + ".",
            "Actualización exitosa",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private boolean esTipoEstudiante(TipoUsuario tipoUsuario) {
        return tipoUsuario != null && tipoUsuario.esTipoEstudiantil();
    }
}
