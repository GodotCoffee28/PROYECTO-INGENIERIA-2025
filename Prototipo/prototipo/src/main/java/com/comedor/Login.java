package com.comedor; // Asegúrate que el package coincida con tu carpeta

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

// 1. AQUI ESTA LA MAGIA: "extends JFrame"
// Esto le dice a Java: "La clase Login ES UNA VENTANA"
public class Login extends JFrame {

    // 2. CONSTRUCTOR (En vez de "main")
    // Este código se ejecuta automáticamente cuando haces "new Login()"
    public Login() {
        // Configuración de la ventana (ya no usas "ventana.", usas "this." o directo)
        super("Acceso al Comedor UCV"); // Título de la ventana
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        // --- TUS COMPONENTES ---
        
        JLabel labelUsuario = new JLabel("Usuario / Cédula:");
        labelUsuario.setBounds(30, 30, 120, 25);
        add(labelUsuario); // Ya no es ventana.add, es solo add()

        JTextField textoUsuario = new JTextField();
        textoUsuario.setBounds(150, 30, 150, 25);
        add(textoUsuario);

        JLabel labelPass = new JLabel("Contraseña:");
        labelPass.setBounds(30, 70, 120, 25);
        add(labelPass);

        JPasswordField textoPass = new JPasswordField();
        textoPass.setBounds(150, 70, 150, 25);
        add(textoPass);

        JButton botonEntrar = new JButton("Ingresar");
        botonEntrar.setBounds(100, 130, 120, 30);
        add(botonEntrar);

        JLabel mensaje = new JLabel("");
        mensaje.setBounds(30, 170, 300, 25);
        add(mensaje);

        // --- ACCIÓN DEL BOTÓN ---
        botonEntrar.addActionListener(e -> {
            String usuario = textoUsuario.getText();
            if(usuario.length() > 0) {
                mensaje.setText("Bienvenido, " + usuario + "!");
                // AQUÍ LUEGO LLAMARÁS A LA SIGUIENTE VENTANA
            } else {
                mensaje.setText("Error: Ingrese su usuario.");
            }
        });
    }
}