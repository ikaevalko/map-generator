package mapgenerator.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import mapgenerator.util.Vector;

public class PerlinNoiseTest {

    @Test
    public void alustaTayttaaRistikonSatunnaisillaVektoreilla() {
        PerlinNoise generaattori = new PerlinNoise(200, 100, 10);
        generaattori.alustaRistikko();
        Vector[][] vektorit = generaattori.getVektorit();
        int perakkaiset = 0;
        boolean satunnainen = true;
        for(int x = 0; x < vektorit.length; x++) {
            for(int y = 0; y < vektorit[0].length; y++) {
                Vector v = vektorit[x][y];
                if((v.getAlkio(0) + v.getAlkio(1)) > 0) {
                    perakkaiset++;
                    if(perakkaiset >= 20) {
                        satunnainen = false;
                        break;
                    }
                } else {
                    perakkaiset = 0;
                }
            }
            if(!satunnainen) {
                break;
            }
        }
        assertTrue(satunnainen);
    }
}
