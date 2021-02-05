package mapgenerator.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class CustomListTest {
    @Test
    public void listalleVoiLisataKokonaislukuja() {
        CustomList<Integer> lista = new CustomList<>();
        for(int i = 0; i < 100; i++) {
            lista.lisaa(i);
        }
        for(int i = 0; i < 100; i++) {
            assertTrue(lista.hae(i) == i);
        }
    }
    
    @Test
    public void listaltaVoiPoistaaKokonaislukuja() {
        CustomList<Integer> lista = new CustomList<>();
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
        CustomList<Integer> lista = new CustomList<>();
        for(int i = 9; i >= 0; i--) {
            lista.lisaa(i);
        }
        assertTrue(lista.hae(0) == 9);
        assertTrue(lista.hae(4) == 5);
        assertTrue(lista.hae(9) == 0);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void listaEiHyvaksyNegatiivistaIndeksia() {
        CustomList<Integer> lista = new CustomList<>();
        for(int i = 0; i < 10; i++) {
            lista.lisaa(i);
        }
        lista.hae(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void listaEiHyvaksyLiianSuurtaIndeksia() {
        CustomList<Integer> lista = new CustomList<>();
        for(int i = 0; i < 10; i++) {
            lista.lisaa(i);
        }
        lista.hae(10);
    }
    
    @Test
    public void listaKasvattaaKapasiteettiaTarvittaessa() {
        CustomList<Integer> lista = new CustomList<>();
        for(int i = 0; i < 8; i++) {
            lista.lisaa(i);
        }
        assertTrue(lista.kapasiteetti() == 8);
        lista.lisaa(8);
        assertTrue(lista.kapasiteetti() == 16);
    }
    
    @Test
    public void listaPienentaaKapasiteettiaTarvittaessa() {
        CustomList<Integer> lista = new CustomList<>();
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
        CustomList<Integer> lista = new CustomList<>();
        for(int i = 0; i < 8; i++) {
            lista.lisaa(i);
        }
        lista.poista(7);
        assertTrue(lista.koko() == 7);
    }
}