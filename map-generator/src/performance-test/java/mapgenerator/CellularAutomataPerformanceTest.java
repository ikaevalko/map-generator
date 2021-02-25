package mapgenerator;

import java.util.ArrayList;
import mapgenerator.logic.CellularAutomata;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class CellularAutomataPerformanceTest {
    
    private static ArrayList<String> nimet;
    private static ArrayList<String> ajat;
    private static CellularAutomata generaattori;
    
    @BeforeAll
    static void alusta() {
        nimet = new ArrayList<>();
        ajat = new ArrayList<>();
    }
    
    @AfterAll
    static void lopeta() {
        System.out.println("");
        System.out.println("Suorituskykytestien tulokset (CellularAutomata):");
        for(int i = 0; i < nimet.size(); i++) {
            System.out.printf("%30s %10s%n", nimet.get(i), ajat.get(i));
        }
    }
    
    static void lisaaTulos(String nimi, String aika) {
        nimet.add(nimi + ": ");
        ajat.add(aika + " ms");
    }
    
    @Test
    @Order(1)
    public void pieniKarttaOletusparametreilla() {
        generaattori = new CellularAutomata(64, 64, 10, 0.5, 5, 5, 1);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("64x64, 10, 0.5, 5, 5, 1", String.valueOf(loppu-alku));
    }
    
    @Test
    @Order(2)
    public void pieniKarttaIlmanKaytavia() {
        generaattori = new CellularAutomata(64, 64, 10, 0.5, 5, 5, 0);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("64x64, 10, 0.5, 5, 5, 0", String.valueOf(loppu-alku));
    }
    
    @Test
    @Order(3)
    public void keskikokoinenKarttaOletusparametreilla() {
        generaattori = new CellularAutomata(128, 128, 10, 0.5, 5, 5, 1);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("128x128, 10, 0.5, 5, 5, 1", String.valueOf(loppu-alku));
    }
    
    @Test
    @Order(4)
    public void keskikokoinenKarttaIlmanKaytavia() {
        generaattori = new CellularAutomata(128, 128, 10, 0.5, 5, 5, 0);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("128x128, 10, 0.5, 5, 5, 0", String.valueOf(loppu-alku));
    }
    
    @Test
    @Order(5)
    public void suuriKarttaOletusparametreilla() {
        generaattori = new CellularAutomata(512, 512, 10, 0.5, 5, 5, 1);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("512x512, 10, 0.5, 5, 5, 1", String.valueOf(loppu-alku));
    }
    
    @Test
    @Order(6)
    public void suuriKarttaIlmanKaytavia() {
        generaattori = new CellularAutomata(512, 512, 10, 0.5, 5, 5, 0);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("512x512, 10, 0.5, 5, 5, 0", String.valueOf(loppu-alku));
    }
    
    @Test
    @Order(7)
    public void suurempiKarttaOletusparametreilla() {
        generaattori = new CellularAutomata(1024, 1024, 10, 0.5, 5, 5, 1);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("1024x1024, 10, 0.5, 5, 5, 1", String.valueOf(loppu-alku));
    }
    
    @Test
    @Order(8)
    public void suurempiKarttaIlmanKaytavia() {
        generaattori = new CellularAutomata(1024, 1024, 10, 0.5, 5, 5, 0);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("1024x1024, 10, 0.5, 5, 5, 0", String.valueOf(loppu-alku));
    }
    
    @Test
    @Order(9)
    public void massiivinenKarttaOletusparametreilla() {
        generaattori = new CellularAutomata(2048, 2048, 10, 0.5, 5, 5, 1);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("2048x2048, 10, 0.5, 5, 5, 1", String.valueOf(loppu-alku));
    }
    
    @Test
    @Order(10)
    public void massiivinenKarttaIlmanKaytavia() {
        generaattori = new CellularAutomata(2048, 2048, 10, 0.5, 5, 5, 0);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("2048x2048, 10, 0.5, 5, 5, 0", String.valueOf(loppu-alku));
    }
}
