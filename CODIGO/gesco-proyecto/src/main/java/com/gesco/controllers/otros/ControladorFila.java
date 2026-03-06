package com.gesco.controllers.otros;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.controllers.gestion_principal.ValidadorIdentidad;
import com.gesco.models.costos.CCB;
import com.gesco.models.menu.Menu;
import com.gesco.models.usuarios.Usuario.TipoUsuario;
import com.gesco.views.otros.VistaFila;

public class ControladorFila {

    private final VistaFila vista;
    private final String cedulaSesion;
    private final Runnable onBack;
    private final Runnable onVerMenu;
    private boolean usuarioEnFila;

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
        this.usuarioEnFila = false;
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
        if (usuarioEnFila) {
            JOptionPane.showMessageDialog(
                vista,
                "Ya te encuentras en la fila. No puedes entrar más de una vez.",
                "Ingreso no permitido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (vista.getEnFila() >= vista.getDisponible()) {
            JOptionPane.showMessageDialog(
                vista,
                "La fila está llena. Intente más tarde.",
                "Fila completa",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (obtenerCcbBase() <= 0) {
            JOptionPane.showMessageDialog(
                vista,
                "No hay un CCB cargado para calcular el cobro.",
                "CCB no disponible",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        actualizarCobro();
        vista.crearVentanaEmergente();
    }

    private void procesarSalida() {
        if (!usuarioEnFila) {
            JOptionPane.showMessageDialog(
                vista,
                "No está en la fila actualmente.",
                "Sin entrada",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        double costoFinal = vista.getCostoTotal();
        double saldoActual = DataBase.obtenerSaldo(cedulaSesion);
        boolean actualizado = DataBase.actualizarSaldo(cedulaSesion, saldoActual + costoFinal);
        if (actualizado) {
            JOptionPane.showMessageDialog(
                vista,
                "Saldo reembolsado correctamente. Saldo disponible: " + String.format("%.2f", saldoActual + costoFinal) + " Bs.",
                "Reembolso exitoso",
                JOptionPane.INFORMATION_MESSAGE
            );
        }

        vista.setEnFila(vista.getEnFila() - 1);
        usuarioEnFila = false;
    }

    private void seleccionarArchivo() {
        JFileChooser chooser = new JFileChooser();
        File carpetaSecretaria = DataBase.obtenerCarpetaImagenesSecretaria();
        if (carpetaSecretaria.isDirectory()) {
            chooser.setCurrentDirectory(carpetaSecretaria);
        }
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileNameExtensionFilter(
            "Imagenes (JPG, JPEG, PNG)", "jpg", "jpeg", "png"
        ));
        int result = chooser.showOpenDialog(vista); 
        if (result == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            vista.setArchivoSeleccionado(archivo);
            vista.setNombreArchivoSeleccionado(archivo.getName());
        }
    }

    private void actualizarCobro() {
        double costoMenu = obtenerCostoMenuHoy();
        double ccbBase = obtenerCcbBase();
        TipoUsuario tipoUsuario = obtenerTipoUsuarioSesion();
        double porcentaje = DataBase.calcularMontoCcbPorTipo(1.0, tipoUsuario);
        double costoFinal = costoMenu + (ccbBase * porcentaje);
        double ajuste = porcentaje * 100.0;

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

        if (vista.getCostoTotal() <= 0) {
            JOptionPane.showMessageDialog(
                vista,
                "No hay un CCB válido para cobrar.",
                "CCB no disponible",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        if(!vista.getArchivoSeleccionado().getName().toLowerCase().matches(".*\\.(jpg|jpeg|png)$")) {
            //Archivo invalido, no es una imagen
                JOptionPane.showMessageDialog(
                vista,
                "Seleccione un archivo de imagen válido (.jpg, .jpeg, .png).",
                "Archivo no válido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        File usuarioImg = vista.getArchivoSeleccionado();
        File usuarioSecretaria = DataBase.obtenerImagenSecretaria(cedulaSesion);
        if (!usuarioSecretaria.exists() || !usuarioSecretaria.isFile()) {
            JOptionPane.showMessageDialog(
                vista,
                "No se encontró la imagen de secretaria para la cédula " + cedulaSesion + ".",
                "Imagen de secretaria no disponible",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        try {
            ValidadorIdentidad validador = new ValidadorIdentidad();
            boolean esValida = validador.compararImagenes(usuarioImg, usuarioSecretaria);
            if (!esValida) {
                JOptionPane.showMessageDialog(
                    vista,
                    "La imagen no coincide con la registrada para esta sesión.",
                    "Validación fallida",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                vista,
                "Error al validar la imagen: " + ex.getMessage(),
                "Error de validación",
                JOptionPane.ERROR_MESSAGE
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

        vista.setEnFila(vista.getEnFila() + 1);
        usuarioEnFila = true;

        JOptionPane.showMessageDialog(
            vista,
            "Cobro realizado. Saldo restante: " + String.format("%.2f", saldoActual - costoFinal) + " Bs.",
            "Cobro exitoso",
            JOptionPane.INFORMATION_MESSAGE
        );
        vista.cerrarEmergente();
    }

    private double obtenerCostoMenuHoy() {
        String fechaHoy = LocalDate.now().toString();
        Menu menu = DataBase.obtenerMenuPorFechaYTipo(fechaHoy, Menu.TipoMenu.ALMUERZO);
        if (menu == null || menu.getEstado() == Menu.EstadoMenu.NO_DISPONIBLE || !menu.tienePlatillos()) {
            menu = DataBase.obtenerMenuPorFechaYTipo(fechaHoy, Menu.TipoMenu.DESAYUNO);
        }
        if (menu == null || menu.getEstado() == Menu.EstadoMenu.NO_DISPONIBLE || !menu.tienePlatillos()) {
            return 0.0;
        }
        return menu.getCostoMenu();
    }

    private double obtenerCcbBase() {
        CCB ultimoCcb = DataBase.obtenerUltimoCcb();
        if (ultimoCcb == null) {
            return 0.0;
        }
        return ultimoCcb.getCcb();
    }

    private TipoUsuario obtenerTipoUsuarioSesion() {
        if (cedulaSesion.isBlank()) {
            return TipoUsuario.COMENSAL;
        }
        return DataBase.obtenerTipoUsuario(cedulaSesion);
    }
}
