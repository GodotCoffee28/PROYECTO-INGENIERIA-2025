package com.gesco.controllers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.menu.Insumo;
import com.gesco.models.menu.Menu;
import com.gesco.models.menu.Platillo;

public class MenuConfiguracionCajaNegraTest {

    private Path dataDirTemporal;
    private String dataDirAnterior;

    @Before
    public void setUp() throws IOException {
        dataDirAnterior = DataBase.getDataDir();
        dataDirTemporal = Files.createTempDirectory("gesco-menu-caja-negra-");
        DataBase.setDataDir(dataDirTemporal.toString());
    }

    @After
    public void tearDown() throws IOException {
        if (dataDirAnterior != null && !dataDirAnterior.isBlank()) {
            DataBase.setDataDir(dataDirAnterior);
        }

        if (dataDirTemporal != null && Files.exists(dataDirTemporal)) {
            Files.walk(dataDirTemporal)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                    }
                });
        }
    }

    @Test
    public void guardarMenu_persisteConfiguracionDelDia() {
        LocalDate fechaHabil = proximoDiaHabil(LocalDate.now());

        Menu menu = new Menu(fechaHabil);
        menu.setTipoMenu(Menu.TipoMenu.DESAYUNO);

        Platillo platillo = new Platillo("Arepa con queso");
        platillo.agregarInsumo(new Insumo("Harina", 2, "Carbohidrato", 5.0f));
        menu.agregarPlatillo(platillo);

        assertTrue(DataBase.guardarMenu(menu));

        Menu recuperado = DataBase.obtenerMenuPorFechaYTipo(fechaHabil.toString(), Menu.TipoMenu.DESAYUNO);
        assertNotNull(recuperado);
        assertTrue(recuperado.tienePlatillos());
    }

    private LocalDate proximoDiaHabil(LocalDate fecha) {
        LocalDate cursor = fecha;
        while (!DataBase.esDiaHabil(cursor)) {
            cursor = cursor.plusDays(1);
        }
        return cursor;
    }
}
