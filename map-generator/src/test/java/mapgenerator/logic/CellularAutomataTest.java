package mapgenerator.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellularAutomataTest {
    
    @Test
    public void alustaTayttaaTaulukonSatunnaisillaTotuusarvoilla() {
        CellularAutomata generaattori = new CellularAutomata(32, 32, 10, 0.5, 1, 0, 0);
        generaattori.alusta();
        boolean[][] kartta = generaattori.getKartta();
        int perakkaiset = 0;
        boolean edellinen = false;
        boolean satunnainen = true;
        for(int x = 0; x < kartta.length; x++) {
            for(int y = 0; y < kartta[0].length; y++) {
                boolean b = kartta[x][y];
                if(b == edellinen) {
                    perakkaiset++;
                    if(perakkaiset >= 16) {
                        satunnainen = false;
                        break;
                    }
                } else {
                    perakkaiset = 0;
                }
                edellinen = b;
            }
            if(!satunnainen) {
                break;
            }
        }
        assertTrue(satunnainen);
    }
    
    @Test
    public void generoituKarttaVastaaMallikarttaa1() {
        // - Ei poista huoneita
        // - Ei lisää käytäviä
        CellularAutomata generaattori = new CellularAutomata(16, 16, 10, 0.5, 1, 0, 0);
        generaattori.generoi();
        String malli = "################" +
                       "################" +
                       "################" +
                       "#......#########" +
                       "#.......########" +
                       "##........######" +
                       "##.........#####" +
                       "##..........####" +
                       "##.............#" +
                       "###............#" +
                       "####.........###" +
                       "####.........###" +
                       "####...........#" +
                       "####...........#" +
                       "####...........#" +
                       "################";
        assertEquals(malli, generaattori.getKarttaMerkkijonona());
    }
    
    @Test
    public void generoituKarttaVastaaMallikarttaa2() {
        // - Ei poista huoneita
        // - Ei lisää käytäviä
        CellularAutomata generaattori = new CellularAutomata(16, 16, 20, 0.55, 1, 0, 0);
        generaattori.generoi();
        String malli = "################" +
                       "################" +
                       "###########..###" +
                       "###########..###" +
                       "##.########..###" +
                       "#...######..####" +
                       "##..############" +
                       "##...###########" +
                       "###..###########" +
                       "###..###########" +
                       "##....##########" +
                       "###....#########" +
                       "####...#########" +
                       "#####.##########" +
                       "################" +
                       "################";
        assertEquals(malli, generaattori.getKarttaMerkkijonona());
    }
    
    @Test
    public void generoituKarttaVastaaMallikarttaa3() {
        // - Ei poista huoneita
        // - Lisää käytäviä
        CellularAutomata generaattori = new CellularAutomata(16, 16, 700, 0.55, 1, 0, 1);
        generaattori.generoi();
        String malli = "################" +
                       "#..##......#####" +
                       "#...........####" +
                       "##..........####" +
                       "##.....##....###" +
                       "#####...#....###" +
                       "####.....#...###" +
                       "####.....#######" +
                       "######...#######" +
                       "######....######" +
                       "######...#######" +
                       "####......######" +
                       "####......######" +
                       "####......######" +
                       "###.......######" +
                       "################";
        assertEquals(malli, generaattori.getKarttaMerkkijonona());
    }
    
    @Test
    public void generoituKarttaVastaaMallikarttaa4() {
        // - Poistaa huoneita
        // - Lisää käytäviä
        CellularAutomata generaattori = new CellularAutomata(16, 16, 700, 0.55, 1, 8, 1);
        generaattori.generoi();
        String malli = "################" +
                       "#..#############" +
                       "#....###########" +
                       "##....##########" +
                       "##....##########" +
                       "###.....########" +
                       "###......#######" +
                       "####.....#######" +
                       "#####....#######" +
                       "#####.....######" +
                       "######...#######" +
                       "####......######" +
                       "####......######" +
                       "####......######" +
                       "###.......######" +
                       "################";
        assertEquals(malli, generaattori.getKarttaMerkkijonona());
    }
}