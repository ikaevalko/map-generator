package mapgenerator.logic;

/**
 * Yksinkertainen näennäissatunnaislukugeneraattori
 * https://en.wikipedia.org/wiki/Linear_congruential_generator
*/
public class RandomNumberGenerator {
    
    private long edellinen;
    private final long modulus = 32768L;
    private final long kerroin = 25173L;
    private final long inkrementti = 13849L;
    
    public RandomNumberGenerator(long seed) {
        this.edellinen = seed;
    }
    
    /**
     * Generoi seuraavan satunnaisluvun edellisen luvun perusteella.
     * 
     * @return seuraava satunnaisluku väliltä 0-1 (double)
     */
    public double seuraava() {
        long luku = ((edellinen * kerroin) + inkrementti) % modulus;
        edellinen = luku;
        luku = luku > 0 ? luku : -luku;
        return (double)luku/modulus;
    }
}