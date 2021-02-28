package mapgenerator;

import java.util.ArrayList;
import mapgenerator.logic.PerlinNoise;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
public class PerlinNoisePerformanceTest {
    
    private static ArrayList<String> nimet;
    private static ArrayList<String> ajat;
    private static PerlinNoise generaattori;
    
    @BeforeAll
    static void alusta() {
        nimet = new ArrayList<>();
        ajat = new ArrayList<>();
    }
    
    @AfterAll
    static void lopeta() {
        System.out.println("------------------------------------------------");
        System.out.println("Suorituskykytestien tulokset (PerlinNoise):");
        for(int i = 0; i < nimet.size(); i++) {
            System.out.printf("%30s %10s%n", nimet.get(i), ajat.get(i));
        }
        System.out.println("------------------------------------------------");
    }
    
    static void lisaaTulos(String nimi, String aika) {
        nimet.add(nimi + ": ");
        ajat.add(aika + " ms");
    }
    
    @Test
    @Order(1)
    public void pieniKarttaOletusparametreilla() {
        generaattori = new PerlinNoise(64, 6, 10);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("64x64, 6, 10", String.valueOf(loppu-alku));
    }
    
    @Test
    @Order(2)
    public void keskikokoinenKarttaOletusparametreilla() {
        generaattori = new PerlinNoise(128, 6, 10);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("128x128, 6, 10", String.valueOf(loppu-alku));
    }
    
    @Test
    @Order(3)
    public void suuriKarttaOletusparametreilla() {
        generaattori = new PerlinNoise(512, 6, 10);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("512x512, 6, 10", String.valueOf(loppu-alku));
    }
    
    @Test
    @Order(4)
    public void suurempiKarttaOletusparametreilla() {
        generaattori = new PerlinNoise(1024, 6, 10);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("1024x1024, 6, 10", String.valueOf(loppu-alku));
    }
    
    @Test
    @Order(5)
    public void massiivinenKarttaOletusparametreilla() {
        generaattori = new PerlinNoise(2048, 6, 10);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("2048x2048, 6, 10", String.valueOf(loppu-alku));
    }
    
    @Test
    @Order(5)
    public void massiivisempiKarttaOletusparametreilla() {
        generaattori = new PerlinNoise(4096, 6, 10);
        long alku = System.currentTimeMillis();
        generaattori.generoi();
        long loppu = System.currentTimeMillis();
        lisaaTulos("4096x4096, 6, 10", String.valueOf(loppu-alku));
    }
}