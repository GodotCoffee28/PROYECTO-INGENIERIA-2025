package com.gesco.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gesco.controllers.gestion_principal.DataBase;
import com.gesco.models.menu.Platillo;
import com.gesco.models.menu.Menu;

public class MenuConfiguracionCajaNegraNegativaTest {

    private Path dataDirTemporal;
    private String dataDirAnterior;

    @Before
    public void setUp() throws IOException {
        dataDirAnterior = DataBase.getDataDir();
        dataDirTemporal = Files.createTempDirectory("gesco-menus-negativo-");
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
    public void guardarMenu_fechaPasada_rechazaGuardado() {
        Menu menu = new Menu(LocalDate.now().minusDays(5));
        menu.setTipoMenu(Menu.TipoMenu.DESAYUNO);

        assertFalse(DataBase.guardarMenu(menu));
    }

    @Test
    public void platillo_sinInsumos_mantieneListaVaciaYCostoCero() {
        Platillo platillo = new Platillo("Platillo Vacio");

        assertTrue(platillo.getInsumos().isEmpty());
        assertEquals(0.0f, platillo.getCostoPlatillo(), 0.0001f);
    }

    @Test
    public void platillo_agregarInsumoNulo_noModificaListaNiCosto() {
        Platillo platillo = new Platillo("Platillo Nulo");

        platillo.agregarInsumo(null);

        assertTrue(platillo.getInsumos().isEmpty());
        assertEquals(0.0f, platillo.getCostoPlatillo(), 0.0001f);
    }
}