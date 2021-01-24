package mapgenerator.logic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomNumberGeneratorTest {
    
    private RandomNumberGenerator rand;
    
    @Before
    public void setUp() {
        this.rand = new RandomNumberGenerator(10);
    }
    
    @Test
    public void seuraavaPalauttaaLuvunNollastaYhteen() {
        double luku = rand.seuraava();
        assertTrue(luku >= 0.0);
        assertTrue(luku <= 1.0);
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
}
