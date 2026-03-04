package com.gesco.controllers.inicio;

import com.gesco.views.inicio.VistaInicioAdmin;

public class ControladorInicioAdmin {

    private final VistaInicioAdmin vista;
    private final Runnable onBack;
    private final Runnable onGestionMenu;
    private final Runnable onSubirDatos;
    private final Runnable onVerDatos;
    private final Runnable onCambio;
    private final Runnable onVerMenu;
    private final Runnable onVerAdmins;
    private final Runnable onVerUsuarios;

    public ControladorInicioAdmin(
        VistaInicioAdmin vista,
        Runnable onBack,
        Runnable onGestionMenu,
        Runnable onSubirDatos,
        Runnable onVerDatos,
        Runnable onCambio,
        Runnable onVerMenu,
        Runnable onVerAdmins,
        Runnable onVerUsuarios
    ) {
        this.vista = vista;
        this.onBack = onBack;
        this.onGestionMenu = onGestionMenu;
        this.onSubirDatos = onSubirDatos;
        this.onVerDatos = onVerDatos;
        this.onCambio = onCambio;
        this.onVerMenu = onVerMenu;
        this.onVerAdmins = onVerAdmins;
        this.onVerUsuarios = onVerUsuarios;
    }

    public void conectar() {
        vista.getBackIcon().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onBack.run();
            }
        });

        vista.getBtnGestion().addActionListener(e -> onGestionMenu.run());
        vista.getBtnSubirDatos().addActionListener(e -> onSubirDatos.run());
        vista.getBtnVerCCB().addActionListener(e -> onVerDatos.run());
        vista.getBtnCambio().addActionListener(e -> onCambio.run());
        vista.getBtnVerMenu().addActionListener(e -> onVerMenu.run());
        vista.getBtnVerAdmins().addActionListener(e -> onVerAdmins.run());
        vista.getBtnVerUsuarios().addActionListener(e -> onVerUsuarios.run());
    }
}



