package mapgenerator.logic;

import mapgenerator.util.Vector;

/**
 * Perlin Noise -algoritmiin perustuva karttageneraattori.
 */
public class PerlinNoise {
    private final double[][] arvot;
    private final Vector[][] vektorit;
    private final int kartanKoko;
    private final int ristikonTiheys;
    private final RandomNumberGenerator rng;
    private final double askel;
    
    /**
     * Luo uuden PerlinNoise-tyyppisen karttageneraattorin.
     * 
     * @param kartanKoko Kartan solujen määrä x- ja y-akseleilla
     * @param ristikonTiheys Laskennassa käytettävien vektorien määrä (-1) ristikon x- ja y-akseleilla
     * @param seed Satunnaisgeneraattorin alkuarvo
     */
    public PerlinNoise(int kartanKoko, int ristikonTiheys, int seed) {
        this.arvot = new double[kartanKoko][kartanKoko];
        this.vektorit = new Vector[ristikonTiheys+1][ristikonTiheys+1];
        this.kartanKoko = kartanKoko;
        this.ristikonTiheys = ristikonTiheys;
        this.rng = new RandomNumberGenerator(seed);
        this.askel = ristikonTiheys/(double)kartanKoko;
    }
    
    /**
     * Muodostaa kartan kutsumalla kaikille soluille perlin-metodia.
     * 
     * @return valmis kartta double-tyyppisenä taulukkona
     */
    public double[][] generoi() {
        try {
            tarkistaArgumentit(kartanKoko, ristikonTiheys);
        } catch(IllegalArgumentException e) {
            throw e;
        }
        alustaRistikko();
        int indeksiX = 0;
        int indeksiY = 0;
        for(double x = 0.0; x < ristikonTiheys; x+=askel) {
            for(double y = 0.0; y < ristikonTiheys; y+=askel) {
                if(indeksiX >= kartanKoko || indeksiY >= kartanKoko) continue;
                arvot[indeksiX][indeksiY] = perlin(x, y);
                indeksiY++;
            }
            indeksiX++;
            indeksiY = 0;
        }
        
        return arvot;
    }
    
    /**
     * Alustaa ristikon satunnaisiin suuntiin osoittavilla (normalisoiduilla) vektoreilla.
     */
    public void alustaRistikko() {
        for(int x = 0; x <= ristikonTiheys; x++) {
            for(int y = 0; y <= ristikonTiheys; y++) {
                Vector v = new Vector(new double[]{rng.seuraava()-0.5, rng.seuraava()-0.5});
                v.normalisoi();
                vektorit[x][y] = v;
            }
        }
    }
    
    /**
     * Laskee Perlin Noise -funktion arvon pisteessä (x, y).
     * 
     * @param x X-koordinaatti
     * @param y Y-koordinaatti
     * @return laskettu arvo
     */
    private double perlin(double x, double y) {
        int x0 = (int)x;
        int x1 = x0 + 1;
        int y0 = (int)y;
        int y1 = y0 + 1;
        
        Vector vektori00 = vektorit[x0][y0];
        Vector vektori01 = vektorit[x0][y1];
        Vector vektori10 = vektorit[x1][y0];
        Vector vektori11 = vektorit[x1][y1];
        
        double pistetulo00 = vektori00.pistetulo(new Vector(new double[]{x-x0, y-y0}));
        double pistetulo01 = vektori01.pistetulo(new Vector(new double[]{x-x0, y-y1}));
        double pistetulo10 = vektori10.pistetulo(new Vector(new double[]{x-x1, y-y0}));
        double pistetulo11 = vektori11.pistetulo(new Vector(new double[]{x-x1, y-y1}));
        
        double u = fade(x-(double)x0);
        double v = fade(y-(double)y0);
        
        return lerp(u, 
                    lerp(v, pistetulo00, pistetulo01), 
                    lerp(v, pistetulo10, pistetulo11));
    }
    
    // "Smootherstep"-interpolaatiofunktio
    private double fade(double t) {
        return t*t*t*(t*(t*6-15)+10);
    }
    
    // Lineaarinen interpolaatiofunktio
    private double lerp(double t, double a, double b) {
        return a+t*(b-a);
    }
    
    private void tarkistaArgumentit(int kartanKoko, int ristikonTiheys) throws IllegalArgumentException {
        if(kartanKoko < 1) {
            throw new IllegalArgumentException("kartanKoko on oltava v\u00e4hint\u00e4\u00e4n 1");
        }
        if(ristikonTiheys < 1) {
            throw new IllegalArgumentException("ristikonTiheys on oltava v\u00e4hint\u00e4\u00e4n 1");
        }
    }
    
    public double[][] getKartta() {
        return this.arvot;
    }
    
    public Vector[][] getVektorit() {
        return this.vektorit;
    }
    
    public String getKarttaMerkkijonona() {
        char[] merkit = new char[kartanKoko*kartanKoko];
        for(int x = 0; x < kartanKoko; x++) {
            for(int y = 0; y < kartanKoko; y++) {
                merkit[x*kartanKoko+y] = muutaMerkiksi(arvot[x][y]);
            }
        }
        return new String(merkit);
    }
    
    private char muutaMerkiksi(double arvo) {
        return Character.forDigit((int)((arvo + 1) * 4.5), 10);
    }
}
