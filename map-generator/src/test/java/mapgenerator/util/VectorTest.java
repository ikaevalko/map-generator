package mapgenerator.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {
    
    @Test
    public void pituusPalauttaaOikeanPituuden() {
        Vector v1 = new Vector(new double[]{1, 0});
        Vector v2 = new Vector(new double[]{0, 1});
        Vector v3 = new Vector(new double[]{1, 1});
        Vector v4 = new Vector(new double[]{-1, -1});
        assertEquals(1, v1.pituus(), 0.1);
        assertEquals(1, v2.pituus(), 0.1);
        assertEquals(1.4142, v3.pituus(), 0.1);
        assertEquals(1.4142, v4.pituus(), 0.1);
    }
    
    @Test
    public void normalisoiMuuttaaPituudenYhdeksi() {
        Vector v1 = new Vector(new double[]{1, 1});
        Vector v2 = new Vector(new double[]{-1, -1});
        v1.normalisoi();
        v2.normalisoi();
        assertEquals(1, v1.pituus(), 0.01);
        assertEquals(1, v2.pituus(), 0.01);
    }
    
    @Test
    public void summaPalauttaaOikeanSumman() {
        Vector v1 = new Vector(new double[]{3, 8});
        Vector v2 = new Vector(new double[]{1, -5});
        double[] alkiot = v1.summa(v2).getAlkiot();
        assertEquals(4, alkiot[0], 0.1);
        assertEquals(3, alkiot[1], 0.1);
    }
    
    @Test
    public void pistetuloPalauttaaOikeanPistetulon() {
        Vector v1 = new Vector(new double[]{3, 8});
        Vector v2 = new Vector(new double[]{1, -5});
        double pt = v1.pistetulo(v2);
        assertEquals(-37, pt, 0.1);
    }
}
