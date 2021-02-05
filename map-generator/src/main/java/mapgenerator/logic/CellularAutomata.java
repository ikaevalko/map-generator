package mapgenerator.logic;

/**
 * Soluautomaatiolla toimiva karttageneraattori
 */
public class CellularAutomata {
    private boolean[][] taulukko;
    private final RandomNumberGenerator rng;
    private final int kartanLeveys;
    private final int kartanKorkeus;
    private final double tayttoaste;
    private final int tasoituskertoja;
    
    /**
     * Luo uuden CellularAutomata-tyyppisen karttageneraattorin.
     * 
     * @param kartanLeveys solujen m‰‰r‰ x-akselilla
     * @param kartanKorkeus solujen m‰‰r‰ y-akselilla
     * @param seed satunnaislukugeneraattorin alkuarvo
     * @param tayttoaste m‰‰r‰‰ lattiasolujen esiintyvyyden alustusvaiheessa (0.0 - 1.0)
     * @param tasoituskertoja montako kertaa karttaa tasoitetaan soluautomaatiolla (v‰hint‰‰n 1)
     */
    public CellularAutomata(int kartanLeveys, int kartanKorkeus, int seed, double tayttoaste, int tasoituskertoja) {
        this.taulukko = new boolean[kartanKorkeus][kartanLeveys];
        this.kartanLeveys = kartanLeveys;
        this.kartanKorkeus = kartanKorkeus;
        this.rng = new RandomNumberGenerator(seed);
        this.tayttoaste = tayttoaste;
        this.tasoituskertoja = tasoituskertoja;
    }
    
    /**
     * Kutsuu metodeja, jotka luovat kartan ja muokkaavat sit‰.
     * 
     * @return valmis kartta boolean-tyyppisen‰ taulukkona
     */
    public boolean[][] generoi() {
        alusta();
        for(int i = 0; i < tasoituskertoja; i++) {
            tasoita();
        }
        return taulukko;
    }
    
    private void alusta() {
        for(int x = 0; x < taulukko.length; x++) {
            for(int y = 0; y < taulukko.length; y++) {
                double satunnainen = rng.seuraava();
                taulukko[x][y] = satunnainen < tayttoaste;
            }
        }
    }
    
    private void tasoita() {
        for(int x = 0; x < taulukko.length; x++) {
            for(int y = 0; y < taulukko.length; y++) {
                if((x == 0 || x == kartanLeveys - 1) || (y == 0 || y == kartanKorkeus - 1)) {
                    taulukko[x][y] = false;
                    continue;
                }
                byte naapuriLattioita = getNaapuriLattioita(x, y);
                if(naapuriLattioita > 4) {
                    taulukko[x][y] = true;
                } else if(naapuriLattioita < 4) {
                    taulukko[x][y] = false;
                }
            }
        }
    }
    
    private byte getNaapuriLattioita(int soluX, int soluY) {
        
        byte maara = 0;
        
        for(byte x = -1; x <= 1; x++) {
            for(byte y = -1; y <= 1; y++) {
                int muutosX = soluX + x;
                int muutosY = soluY + y;
                if(x == 0 && y == 0) {
                    continue;
                }
                if((muutosX < 0 || muutosX > kartanLeveys - 1) || (muutosY < 0 || muutosY > kartanKorkeus - 1)) {
                    continue;
                }
                if(taulukko[muutosX][muutosY]) {
                    maara++;
                }
            }
        }
        
        return maara;
    }
}
