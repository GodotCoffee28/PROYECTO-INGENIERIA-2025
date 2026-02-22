package com.gesco.views.PlantillasViews;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.YearMonth;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
public class CampoFecha extends JPanel {
    private final JLabel label;
    private final JComboBox<Integer> diaCombo;
    private final JMonthChooser mesChooser;
    private final JYearChooser anioChooser;



    
    public CampoFecha(String labelText, String fuente, int estilo, int tamano, Color color, String alineacion) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        label = new JLabel(labelText);
        label.setFont(new Font(fuente, estilo, tamano));
        label.setForeground(color);

        diaCombo = new JComboBox<>();
        mesChooser = new JMonthChooser();
        anioChooser = new JYearChooser();

        mesChooser.addPropertyChangeListener("month", evt -> updateDays());
        anioChooser.addPropertyChangeListener("year", evt -> updateDays());
        updateDays();

        JPanel fechaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        fechaPanel.setOpaque(false);
        fechaPanel.add(diaCombo);
        fechaPanel.add(mesChooser);
        fechaPanel.add(anioChooser);

        add(label);
        add(Box.createVerticalStrut(8));
        add(fechaPanel);
        setMaximumSize(new Dimension(450, 80));
    }

    public String getDia() {
        Object selected = diaCombo.getSelectedItem();
        return selected == null ? "" : selected.toString();
    }

    public String getMes() {
        return String.valueOf(mesChooser.getMonth() + 1);
    }

    public String getAnio() {
        return String.valueOf(anioChooser.getYear());
    }

    public String getFechaTexto() {
        int dia = getSelectedDay();
        int mes = mesChooser.getMonth() + 1;
        int anio = anioChooser.getYear();
        if (dia <= 0) {
            return "";
        }
        String diaText = String.format("%02d", dia);
        String mesText = String.format("%02d", mes);
        String anioText = String.valueOf(anio);
        return diaText + "/" + mesText + "/" + anioText;
    }

    private void updateDays() {
        int selectedDay = getSelectedDay();
        int year = anioChooser.getYear();
        int month = mesChooser.getMonth() + 1;
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

        diaCombo.removeAllItems();
        for (int day = 1; day <= daysInMonth; day++) {
            diaCombo.addItem(day);
        }

        if (selectedDay <= 0) {
            selectedDay = 1;
        }
        diaCombo.setSelectedItem(Math.min(selectedDay, daysInMonth));
    }

    private int getSelectedDay() {
        Object selected = diaCombo.getSelectedItem();
        if (selected instanceof Integer) {
            return (Integer) selected;
        }
        if (selected instanceof String) {
            try {
                return Integer.parseInt((String) selected);
            } catch (NumberFormatException ex) {
                return 0;
            }
        }
        return 0;
    }

    public void setLabelText(String text) {
        label.setText(text);
    }

    public void setLabelColor(Color color) {
        label.setForeground(color);
    }
}
