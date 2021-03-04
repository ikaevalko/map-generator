package mapgenerator.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class CustomMathTest {
    
    private CustomMath apuMath;
    
    @BeforeEach
    void init() {
        apuMath = new CustomMath();
    }
    
    @Test
    public void neliojuuriPalauttaaOikeanTuloksenKokonaisluvulla() {
        assertEquals(3, apuMath.neliojuuri(9));
    }
    
    @Test
    public void neliojuuriPalauttaaOikeanTuloksenLiukuluvulla() {
        assertEquals(3, apuMath.neliojuuri(9));
    }
    
    @Test
    public void neliojuuriEiHyvaksyNegatiivistaLukua() {
        assertThrows(IllegalArgumentException.class, () -> {
            apuMath.neliojuuri(-9);
        });
    }
    
    @Test
    public void itseisarvoPalauttaaSamanLuvunJosPositiivinen() {
        assertEquals(10, apuMath.itseisarvo(10));
    }
    
    @Test
    public void itseisarvoPalauttaaKaanteisluvunJosNegatiivinen() {
        assertEquals(10, apuMath.itseisarvo(-10));
    }
    
    @Test
    public void signumPalauttaaNollanJosLukuNolla() {
        assertEquals(0, apuMath.signum(0));
    }
    
    @Test
    public void signumPalauttaaYksiJosLukuSuurempiKuinNolla() {
        assertEquals(1, apuMath.signum(10));
    }
    
    @Test
    public void signumPalauttaaMiinusYksiJosLukuOnPienempiKuinNolla() {
        assertEquals(-1, apuMath.signum(-10));
    }
}