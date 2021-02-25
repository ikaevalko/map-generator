package mapgenerator.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class RandomNumberGeneratorTest {
    
    private RandomNumberGenerator rand;
    private int seed = 10;
    
    @BeforeEach
    void setUp() {
        this.rand = new RandomNumberGenerator(seed);
    }
    
    @Test
    public void seuraavaPalauttaaLuvunNollastaYhteen() {
        for(int i = 0; i < 100; i++) {
            double luku = rand.seuraava();
            assertTrue(luku >= 0.0);
            assertTrue(luku <= 1.0);
        }
    }
    
    @Test
    public void generaattoriPalauttaaTasaisestiEriLukuja() {
        int vahemmanKuinPuoli = 0;
        int enemmanKuinPuoli = 0;
        for(int i = 0; i < 100; i += 7) {
            rand = new RandomNumberGenerator(i);
            vahemmanKuinPuoli = 0;
            enemmanKuinPuoli = 0;
            for(int j = 0; j < 100; j++) {
                double luku = rand.seuraava();
                if(luku < 0.5) {
                    vahemmanKuinPuoli++;
                } else {
                    enemmanKuinPuoli++;
                }
            }
            assertTrue(vahemmanKuinPuoli >= 20);
            assertTrue(enemmanKuinPuoli >= 20);
        }
    }
    
    @Test
    public void generaattoriPalauttaaLuvunDeterministisesti() {
        double[] luvut = new double[10];
        for(int i = 0; i < 10; i++) {
            luvut[i] = rand.seuraava();
        }
        rand = new RandomNumberGenerator(seed);
        for(int i = 0; i < 10; i++) {
            assertEquals(rand.seuraava(), luvut[i], 0.01);
        }
    }
}
