package mapgenerator.util;

/**
 * Vektoria kuvaava luokka.
 */
public class Vector {
    private final double[] alkiot;
    private final CustomMath apuMath;
    
    public Vector(double[] alkiot) {
        this.alkiot = alkiot;
        this.apuMath = new CustomMath();
    }
    
    public double getAlkio(int indeksi) {
        return this.alkiot[indeksi];
    }
    
    public double[] getAlkiot() {
        return this.alkiot;
    }
    
    /**
     * Laskee vektorin pituuden.
     * 
     * @return vektorin pituus
     */
    public double pituus() {
        double summa = 0.0;
        for(int i = 0; i < alkiot.length; i++) {
            double a = alkiot[i];
            summa += a*a;
        }
        return apuMath.neliojuuri(summa);
    }
    
    /**
     * Normalisoi vektorin.
     */
    public void normalisoi() {
        double vektorinPituus = pituus();
        for(int i = 0; i < alkiot.length; i++) {
            if(alkiot[i] != 0) {
                alkiot[i] /= vektorinPituus;
            }
        }
    }
    
    /**
     * Laskee tämän ja argumenttina annetun vektorin pistetulon. 
     * Vektoreilla on oltava sama määrä alkioita.
     * 
     * @param toinen vektori, jonka kanssa pistetulo lasketaan
     * @return vektoreiden pistetulo
     */
    public double pistetulo(Vector toinen) {
        if(alkiot.length != toinen.getAlkiot().length) {
            throw new IllegalArgumentException("Vektoreilla on eri määrä alkioita");
        }
        double pt = 0.0;
        for(int i = 0; i < alkiot.length; i++) {
            pt += alkiot[i]*toinen.getAlkio(i);
        }
        return pt;
    }
}
