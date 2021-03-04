package mapgenerator.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class CustomListTest {
    
    private CustomList<Integer> lista;
    
    @BeforeEach
    void init() {
        lista = new CustomList<>();
    }
    
    @Test
    public void listalleVoiLisataKokonaislukuja() {
        for(int i = 0; i < 100; i++) {
            lista.lisaa(i);
        }
        for(int i = 0; i < 100; i++) {
            assertTrue(lista.hae(i) == i);
        }
    }
    
    @Test
    public void listaltaVoiPoistaaKokonaislukuja() {
        for(int i = 0; i < 100; i++) {
            lista.lisaa(i);
        }
        for(int i = 0; i < 50; i++) {
            lista.poista(i);
        }
        assertTrue(lista.koko() == 50);
    }
    
    @Test
    public void listaHakeeOikeanKokonaisluvun() {
        for(int i = 9; i >= 0; i--) {
            lista.lisaa(i);
        }
        assertTrue(lista.hae(0) == 9);
        assertTrue(lista.hae(4) == 5);
        assertTrue(lista.hae(9) == 0);
    }
    
    @Test
    public void listaEiHyvaksyNegatiivistaIndeksia() {
        for(int i = 0; i < 10; i++) {
            lista.lisaa(i);
        }
        assertThrows(IndexOutOfBoundsException.class, () -> {
            lista.hae(-1);
        });
    }
    
    @Test
    public void listaEiHyvaksyLiianSuurtaIndeksia() {
        for(int i = 0; i < 10; i++) {
            lista.lisaa(i);
        }
        assertThrows(IndexOutOfBoundsException.class, () -> {
            lista.hae(10);
        });
    }
    
    @Test
    public void listaKasvattaaKapasiteettiaTarvittaessa() {
        for(int i = 0; i < 8; i++) {
            lista.lisaa(i);
        }
        assertTrue(lista.kapasiteetti() == 8);
        lista.lisaa(8);
        assertTrue(lista.kapasiteetti() == 16);
    }
    
    @Test
    public void listaPienentaaKapasiteettiaTarvittaessa() {
        for(int i = 0; i < 12; i++) {
            lista.lisaa(i);
        }
        assertTrue(lista.kapasiteetti() == 16);
        for(int i = 0; i < 10; i++) {
            lista.poista(0);
        }
        assertTrue(lista.kapasiteetti() == 8);
    }
    
    @Test
    public void viimeisestaIndeksistaPoistoToimii() {
        for(int i = 0; i < 8; i++) {
            lista.lisaa(i);
        }
        lista.poista(7);
        assertTrue(lista.koko() == 7);
    }
}