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

        boolean actualizado = DataBase.cambiarTipoUsuarioPorCedula(cedula, nuevoTipo);
        if (!actualizado) {
            JOptionPane.showMessageDialog(
                vista,
                "No se pudo actualizar el tipo. Verifique si la cédula es válida o si intenta modificar un super administrador.",
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
        return tipoUsuario == TipoUsuario.ESTUDIANTE
            || tipoUsuario == TipoUsuario.BECARIO
            || tipoUsuario == TipoUsuario.EXONERADO;
    }
}
