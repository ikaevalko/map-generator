package mapgenerator.util;

/**
 * Yksinkertainen listarakenne lis‰ys-, poisto- ja hakutoiminnoilla.
 */
public class CustomList<T> {
    private Object[] lista;
    private int osoitin;
    
    /**
     * Luo uuden listan kapasiteetilla 8.
     */
    public CustomList() {
        this.lista = new Object[8];
        this.osoitin = 0;
    }
    
    /**
     * Lis‰‰ annetun olion listan loppuun.
     * 
     * @param olio listalle lis‰tt‰v‰ olio
     */
    public void lisaa(T olio) {
        if(osoitin >= lista.length) {
            kasvata();
        }
        lista[osoitin] = olio;
        osoitin++;
    }
    
    /**
     * Poistaa annetussa indeksiss‰ olevan olion listalta.
     * 
     * @param indeksi indeksi, josta olio halutaan poistaa
     */
    public void poista(int indeksi) {
        tarkistaIndeksi(indeksi);
        if(indeksi == osoitin-1) {
            lista[osoitin-1] = null;
        } else {
            Object[] kopio = new Object[lista.length];
            int j = 0;
            for(int i = 0; i < osoitin; i++) {
                if(i == indeksi) {
                    continue;
                }
                kopio[j] = lista[i];
                j++;
            }
            lista = kopio;
        }
        osoitin--;
        if(osoitin <= (int)(lista.length*0.25) && lista.length > 8) {
            pienenna();
        }
    }
    
    /**
     * Poistaa annetussa listassa m‰‰ritellyist‰ indekseist‰ oliot listalta. 
     * Indeksien on oltava listalla j‰rjestyksess‰ pienimm‰st‰ suurimpaan.
     * 
     * @param indeksit 
     */
    public void poistaMonta(CustomList<Integer> indeksit) {
        if(indeksit.koko() <= 0) {
            return;
        }
        for(int i = indeksit.koko() - 1; i >= 0; i--) {
            poista(indeksit.hae(i));
        }
    }
    
    /**
     * Hakee annetussa indeksiss‰ olevan olion listalta.
     * 
     * @param indeksi indeksi, josta olio halutaan hakea
     * @return listalta haettu olio
     */
    public T hae(int indeksi) {
        tarkistaIndeksi(indeksi);
        return lista(indeksi);
    }
    
    /**
     * Kertoo listan alkioiden m‰‰r‰n.
     * 
     * @return listan alkioiden m‰‰r‰
     */
    public int koko() {
        return osoitin;
    }
    
    /**
     * Kertoo, montako alkiota listalle mahtuu.
     * 
     * @return listan nykyinen kapasiteetti
     */
    public int kapasiteetti() {
        return lista.length;
    }
    
    private void kasvata() {
        Object[] kopio = new Object[lista.length*2];
        for(int i = 0; i < osoitin; i++) {
            kopio[i] = lista[i];
        }
        lista = kopio;
    }
    
    private void pienenna() {
        Object[] kopio = new Object[(int)(lista.length*0.5)];
        for(int i = 0; i < osoitin; i++) {
            kopio[i] = lista[i];
        }
        lista = kopio;
    }
    
    private void tarkistaIndeksi(int indeksi) {
        if(indeksi < 0 || indeksi >= osoitin) {
            throw new IndexOutOfBoundsException("Virheellinen indeksi: " + indeksi);
        }
    }
    
    @SuppressWarnings("unchecked")
    private T lista(int indeksi) {
        return (T)lista[indeksi];
    }
}
