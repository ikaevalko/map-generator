package mapgenerator.logic;

/**
 * Yksinkertainen näennäissatunnaislukugeneraattori
 * https://en.wikipedia.org/wiki/Linear_congruential_generator
*/
public class RandomNumberGenerator {
    
    private long edellinen;
    private final long modulus = 281474976710656L;
    private final long kerroin = 25214903917L;
    private final long inkrementti = 11L;
    
    /**
     * Luo uuden satunnaislukugeneraattorin.
     * 
     * @param seed alkuarvo
     */
    public RandomNumberGenerator(long seed) {
        this.edellinen = seed;
    }
    
    /**
     * Generoi seuraavan satunnaisluvun edellisen luvun perusteella.
     * 
     * @return seuraava satunnaisluku väliltä 0.0 - 1.0
     */
    public double seuraava() {
        long luku = ((edellinen * kerroin) + inkrementti) % modulus;
        edellinen = luku;
        luku = luku > 0 ? luku : -luku;
        return (double)luku/modulus;
    }
}