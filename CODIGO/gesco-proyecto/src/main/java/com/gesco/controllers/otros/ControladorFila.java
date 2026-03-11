package com.gesco.controllers.otros;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

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
        sincronizarEstadoFilaDesdeArchivo();

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
        sincronizarEstadoFilaDesdeArchivo();

        if (usuarioEnFila) {
            JOptionPane.showMessageDialog(
                vista,
                "Ya te encuentras en la fila. No puedes entrar más de una vez.",
                "Ingreso no permitido",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!hayMenuActivoHoy()) {
            JOptionPane.showMessageDialog(
                vista,
                "No hay menu disponible para hoy.",
                "Menu no disponible",
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

        TipoUsuario tipoUsuarioSesion = obtenerTipoUsuarioSesion();
        if (tipoUsuarioSesion != TipoUsuario.EXONERADO && obtenerCcbBase() <= 0) {
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
        sincronizarEstadoFilaDesdeArchivo();

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

        boolean eliminadoAcudieron = DataBase.eliminarRegistroAcudieron(cedulaSesion);
        if (!eliminadoAcudieron) {
            JOptionPane.showMessageDialog(
                vista,
                "No se pudo actualizar el archivo acudieron.txt al salir de la fila.",
                "Actualización incompleta",
                JOptionPane.WARNING_MESSAGE
            );
        }

        sincronizarEstadoFilaDesdeArchivo();
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
        TipoUsuario tipoUsuario = obtenerTipoUsuarioSesion();
        double costoMenu = obtenerCostoMenuHoy();

        // Exonerado no requiere CCB disponible: aplica 0% de recargo.
        if (tipoUsuario == TipoUsuario.EXONERADO) {
            vista.setCobroInfo(costoMenu, 0.0, costoMenu);
            return;
        }

        CCB ccbConfigurado = DataBase.obtenerCcbPorFechaYTipo(LocalDate.now(), tipoUsuario);
        if (ccbConfigurado == null) {
            vista.setCobroInfo(0.0, 0.0, 0.0);
            return;
        }

        double ccbBase = ccbConfigurado.getCcb();
        double porcentaje = CCB.normalizarPorcentajeParaTipo(tipoUsuario, ccbConfigurado.getPorcentajeAplicado());
        double costoCcb = CCB.calcularMontoPorTipo(ccbBase, tipoUsuario, porcentaje);
        double costoFinal = costoMenu + costoCcb;
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

        TipoUsuario tipoUsuario = obtenerTipoUsuarioSesion();
        Menu.TipoMenu tipoServicio = obtenerTipoServicioActual();
        boolean registroAcudieron = DataBase.registrarAcudieron(
            cedulaSesion,
            tipoUsuario,
            tipoServicio,
            costoFinal
        );
        if (!registroAcudieron) {
            JOptionPane.showMessageDialog(
                vista,
                "El cobro se realizó, pero no se pudo registrar la asistencia en acudieron.txt.",
                "Registro de asistencia incompleto",
                JOptionPane.WARNING_MESSAGE
            );
        }

        sincronizarEstadoFilaDesdeArchivo();

        JOptionPane.showMessageDialog(
            vista,
            "Cobro realizado. Saldo restante: " + String.format("%.2f", saldoActual - costoFinal) + " Bs.",
            "Cobro exitoso",
            JOptionPane.INFORMATION_MESSAGE
        );
        vista.cerrarEmergente();
    }

    private double obtenerCostoMenuHoy() {
        Menu menu = obtenerMenuServicioActual();
        if (menu == null) {
            return 0.0;
        }
        return menu.getCostoMenu();
    }

    private Menu.TipoMenu obtenerTipoServicioActual() {
        Menu menuServicio = obtenerMenuServicioActual();
        return menuServicio == null ? Menu.TipoMenu.DESAYUNO : menuServicio.getTipoMenu();
    }

    private Menu obtenerMenuServicioActual() {
        String fechaHoy = LocalDate.now().toString();
        Menu menuDesayuno = DataBase.obtenerMenuPorFechaYTipo(fechaHoy, Menu.TipoMenu.DESAYUNO);
        Menu menuAlmuerzo = DataBase.obtenerMenuPorFechaYTipo(fechaHoy, Menu.TipoMenu.ALMUERZO);

        boolean desayunoActivo = esMenuActivo(menuDesayuno);
        boolean almuerzoActivo = esMenuActivo(menuAlmuerzo);

        if (desayunoActivo && !almuerzoActivo) {
            return menuDesayuno;
        }

        if (!desayunoActivo && almuerzoActivo) {
            return menuAlmuerzo;
        }

        if (desayunoActivo && almuerzoActivo) {
            return LocalTime.now().isBefore(LocalTime.NOON) ? menuDesayuno : menuAlmuerzo;
        }

        return null;
    }

    private double obtenerCcbBase() {
        CCB ultimoCcb = DataBase.obtenerCcbPorFechaYTipo(LocalDate.now(), obtenerTipoUsuarioSesion());
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

    private boolean hayMenuActivoHoy() {
        String fechaHoy = LocalDate.now().toString();
        Menu menuDesayuno = DataBase.obtenerMenuPorFechaYTipo(fechaHoy, Menu.TipoMenu.DESAYUNO);
        Menu menuAlmuerzo = DataBase.obtenerMenuPorFechaYTipo(fechaHoy, Menu.TipoMenu.ALMUERZO);

        return esMenuActivo(menuDesayuno) || esMenuActivo(menuAlmuerzo);
    }

    private boolean esMenuActivo(Menu menu) {
        return menu != null
            && menu.getEstado() != Menu.EstadoMenu.NO_DISPONIBLE
            && menu.tienePlatillos();
    }

    private void sincronizarEstadoFilaDesdeArchivo() {
        LocalDate hoy = LocalDate.now();
        int enFilaHoy = DataBase.contarAcudieronPorFecha(hoy);
        vista.setEnFila(enFilaHoy);
        usuarioEnFila = DataBase.existeRegistroAcudieronPorCedulaYFecha(cedulaSesion, hoy);
    }
}
