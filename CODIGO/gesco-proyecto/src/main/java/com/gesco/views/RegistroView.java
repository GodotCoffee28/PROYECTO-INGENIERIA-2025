package com.gesco.views;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class RegistroView extends JFrame{

    //JFrame ventana = new JFrame("GESCO");
    private JTextField TxtNombreApellido, TxtCedula, TxtCorreo;
    private JPasswordField TxtContra;
    private JButton registroBoton;


    public RegistroView() {
            //El constructor de la clase
        setTitle("Registro Comedor UCV");
        setSize(1122, 654);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); 

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        agregarTitulo(panel);

        agregarCampos(panel);

        agregarInferior(panel);
        add(panel, BorderLayout.CENTER); 
        setVisible(true);
        }

    /*private void InicializarForm(){
        //Titulo superior
        JLabel Titulo = new JLabel("Crea una nueva cuenta", SwingConstants.CENTER);
        Titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(Titulo, BorderLayout.NORTH);

        //Este panel contendra el formulario, y se posicionara de forma central en la interfaz
        JPanel ContenedorForm = new JPanel(new GridBagLayout());
        ContenedorForm.setBackground(Color.GRAY);
        //Patron decorator de hecho
        JPanel PanelForm = new JPanel();
        PanelForm.setLayout(new BoxLayout(PanelForm, BoxLayout.Y_AXIS)); //Box para que los campos sean uno abajo de otro
        PanelForm.setPreferredSize(new Dimension(461,554)); //dimension del panel de campos
        PanelForm.setBackground(Color.WHITE);

        //Dimension para los FieldText y alineasion
        Dimension CampoDimension = new Dimension(800,20);
        //Agregar el apartado de poner nombre
        PanelForm.add(new JLabel(" Nombre y Apellido:"), Component.CENTER_ALIGNMENT); //Etiqueta o poner nombre de campo
        TxtNombreApellido = new JTextField(20);
        TxtNombreApellido.setMaximumSize(CampoDimension); //agregamos el tamaño maximo que puede agarrar
        TxtNombreApellido.setPreferredSize(CampoDimension); //El preferido
        TxtNombreApellido.setAlignmentX(Component.CENTER_ALIGNMENT); //Y su alineacion
        PanelForm.add(TxtNombreApellido); //Se agrega al panel

        PanelForm.add(Box.createVerticalStrut(15)); //Espacio vacío

        //Agregar el apartado de poner cedula
        PanelForm.add(new JLabel(" Cédula de identidad:"),Component.CENTER_ALIGNMENT);
        TxtCedula = new JTextField(20);
        TxtCedula.setMaximumSize(CampoDimension);
        TxtCedula.setPreferredSize(CampoDimension);
        TxtCedula.setAlignmentX(Component.CENTER_ALIGNMENT);
        PanelForm.add(TxtCedula);

        PanelForm.add(Box.createVerticalStrut(15));

        //Agregar el apartado de poner correo
        PanelForm.add(new JLabel(" Correo electrónico:"),Component.CENTER_ALIGNMENT);
        TxtCorreo = new JTextField(20);
        TxtCorreo.setMaximumSize(CampoDimension);
        TxtCorreo.setPreferredSize(CampoDimension);
        TxtCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);
        PanelForm.add(TxtCorreo);

        PanelForm.add(Box.createVerticalStrut(15));

        //Agregar el apartado de poner contraseña
        PanelForm.add(new JLabel(" Contraseña:"), Component.CENTER_ALIGNMENT);
        TxtContra = new JPasswordField(20);
        TxtContra.setMaximumSize(CampoDimension);
        TxtContra.setPreferredSize(CampoDimension);
        TxtContra.setAlignmentX(Component.CENTER_ALIGNMENT);
        PanelForm.add(TxtContra);

        PanelForm.add(Box.createVerticalGlue());

        ContenedorForm.add(PanelForm,  new GridBagConstraints());

        add(ContenedorForm, BorderLayout.CENTER);
        
        PanelForm.setVisible(true);
    }*/
    private void agregarTitulo(JPanel panel) {
        JLabel TituloLabel = new JLabel(" Crea una cuenta nueva: ");
        TituloLabel.setFont(new Font("Arial", Font.BOLD, 16));
        TituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(TituloLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

    }
    private void agregarCampos(JPanel panel) {

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        TxtNombreApellido = new JTextField();
        TxtCedula = new JTextField();
        TxtCorreo = new JTextField();
        TxtContra = new JPasswordField();
        agregarCampo(formPanel, "Nombre y Apellido", TxtNombreApellido);
        agregarCampo(formPanel, "Cédula de identidad", TxtCedula);
        agregarCampo(formPanel, "Correo electrónico", TxtCorreo);
        agregarCampo(formPanel, "Contraseña", TxtContra);

        panel.add(formPanel);
    }

    private void agregarCampo(JPanel panel, String textoLabel, JTextField campo) {
        JLabel label = new JLabel(textoLabel);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT); 

        Dimension dim = new Dimension(400, 35);
        campo.setMaximumSize(dim);
        campo.setPreferredSize(dim);
        campo.setAlignmentX(Component.CENTER_ALIGNMENT); //Centra

        panel.add(label);
        panel.add(Box.createVerticalStrut(5)); // Espacio etiqueta-campo
        panel.add(campo);
        panel.add(Box.createVerticalStrut(15)); // Espacio entre bloques
    }
    private void agregarInferior(JPanel panel){
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelInferior.add(new JLabel(" ¿Ya tienes una cuenta? ") );
        registroBoton = new JButton("Registrarse");
        

        panelInferior.add(registroBoton);

        panel.add(panelInferior);
    }

}