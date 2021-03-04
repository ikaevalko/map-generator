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
    
    @Test
    public void generoituKarttaVastaaMallikarttaa1() {
        PerlinNoise generaattori = new PerlinNoise(16, 6, 10);
        generaattori.generoi();
        String malli = "4554443245446532" +
                       "4554332234567532" +
                       "5554234334555542" +
                       "3443245545433433" +
                       "2222344444344433" +
                       "3334543343356543" +
                       "5445654454344334" +
                       "5556544444443345" +
                       "4544445544532354" +
                       "4423345555532444" +
                       "5423335446544545" +
                       "3345445446533435" +
                       "4355443334422334" +
                       "4454443334432354" +
                       "4454454456553454" +
                       "5444654466454454";
        assertEquals(malli, generaattori.getKarttaMerkkijonona());
    }
    
    @Test
    public void generoituKarttaVastaaMallikarttaa2() {
        PerlinNoise generaattori = new PerlinNoise(16, 3, 20);
        generaattori.generoi();
        String malli = "4443345554444333" +
                       "3333344555555434" +
                       "2233345566665444" +
                       "2333445666665444" +
                       "3344445666554334" +
                       "4444445565543223" +
                       "4544334555433223" +
                       "5543334444322123" +
                       "4432233443221112" +
                       "4322234443322223" +
                       "4322334554433223" +
                       "4322345555444333" +
                       "4322345555554444" +
                       "4322344444555555" +
                       "3333333333445555" +
                       "4333443222345565";
        assertEquals(malli, generaattori.getKarttaMerkkijonona());
    }
    
    @Test
    public void generoituKarttaVastaaMallikarttaa3() {
        PerlinNoise generaattori = new PerlinNoise(16, 12, 700);
        generaattori.generoi();
        String malli = "4444436345344534" +
                       "4264553455354365" +
                       "4342242533123432" +
                       "4545452555444645" +
                       "4425453543634534" +
                       "4645354343454424" +
                       "2432644445324534" +
                       "5355334543555554" +
                       "4543434544354345" +
                       "4334323545435364" +
                       "5456535544432334" +
                       "3443455343555535" +
                       "4345443545344525" +
                       "3445544454454465" +
                       "5453234534464343" +
                       "4354564343254543";
        assertEquals(malli, generaattori.getKarttaMerkkijonona());
    }
}
