package mapgenerator.util;

/**
 * Luokka laskennassa käytettäville apumetodeille.
 */
public class CustomMath {
    public double neliojuuri(int luku) {
	double apu;
	double nj = luku/2;
	do {
            apu = nj;
            nj = (apu + (luku / apu)) / 2;
	} while((apu - nj) != 0);
	return nj;
    }
    
    public int itseisarvo(int luku) {
        return luku >= 0 ? luku : -luku;
    }
    
    public int signum(int luku) {
        if(luku == 0) return 0;
        else if(luku > 0) return 1;
        else return -1;
    }
}
