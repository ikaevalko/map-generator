package mapgenerator.logic;

import mapgenerator.util.CustomList;
import mapgenerator.util.CustomMath;

/**
 * Soluautomaatiolla toimiva karttageneraattori.
 */
public class CellularAutomata {
    private final boolean[][] kartta;
    private final int kartanLeveys;
    private final int kartanKorkeus;
    private final double tayttoaste;
    private final int tasoituskertoja;
    private final int huoneidenMinimikoko;
    private final int kaytavienKoko;
    private final CustomList<CellRoom> huoneet;
    private final RandomNumberGenerator rng;
    private final CustomMath apuMath;
    
    /**
     * Luo uuden CellularAutomata-tyyppisen karttageneraattorin.
     * 
     * @param kartanLeveys Solujen määrä x-akselilla
     * @param kartanKorkeus Solujen määrä y-akselilla
     * @param seed Satunnaislukugeneraattorin alkuarvo
     * @param tayttoaste Määrää lattiasolujen esiintyvyyden alustusvaiheessa (0.0 - 1.0)
     * @param tasoituskertoja Montako kertaa karttaa tasoitetaan soluautomaatiolla (vähintään 1)
     * @param huoneidenMinimikoko Tätä pienemmät huoneet poistetaan kartasta
     * @param kaytavienKoko Huoneiden yhdistelyssä käytettävien käytävien koko
     */
    public CellularAutomata(int kartanLeveys, int kartanKorkeus, int seed, 
                            double tayttoaste, int tasoituskertoja, int huoneidenMinimikoko, 
                            int kaytavienKoko) {
        this.kartta = new boolean[kartanLeveys][kartanKorkeus];
        this.kartanLeveys = kartanLeveys;
        this.kartanKorkeus = kartanKorkeus;
        this.tayttoaste = tayttoaste;
        this.tasoituskertoja = tasoituskertoja;
        this.huoneidenMinimikoko = huoneidenMinimikoko;
        this.kaytavienKoko = kaytavienKoko;
        this.huoneet = new CustomList<>();
        this.rng = new RandomNumberGenerator(seed);
        this.apuMath = new CustomMath();
    }
    
    /**
     * Kutsuu metodeja, jotka luovat kartan ja muokkaavat sitä.
     * 
     * @return valmis kartta boolean-tyyppisenä taulukkona
     */
    public boolean[][] generoi() {
        alusta();
        for(int i = 0; i < tasoituskertoja; i++) {
            tasoita();
        }
        etsiHuoneet();
        if(huoneidenMinimikoko > 0) {
            poistaLiianPienetHuoneet();
        }
        if(kaytavienKoko > 0) {
            yhdistaHuoneet();
        }
        return kartta;
    }
    
    /**
     * Alustaa karttataulukon satunnaisilla totuusarvoilla, joiden 
     * jakauma määräytyy täyttöaste-parametrin mukaan.
     */
    public void alusta() {
        for(int x = 0; x < kartanLeveys; x++) {
            for(int y = 0; y < kartanKorkeus; y++) {
                double satunnainen = rng.seuraava();
                kartta[x][y] = satunnainen < tayttoaste;
            }
        }
    }
    
    /**
     * Muokkaa karttataulukkoa määrittämällä jokaisen solun totuusarvon
     * sen naapurisolujen totuusarvojen jakauman perusteella. 
     * Mikäli solulla on naapureina enemmän lattioita kuin seiniä, niin 
     * solu muuttuu lattiaksi. Jos naapureina on enemmän seiniä kuin 
     * lattioita, niin solu muuttuu seinäksi.
     */
    private void tasoita() {
        for(int x = 0; x < kartanLeveys; x++) {
            for(int y = 0; y < kartanKorkeus; y++) {
                if((x == 0 || x == kartanLeveys - 1) || (y == 0 || y == kartanKorkeus - 1)) {
                    kartta[x][y] = false;
                    continue;
                }
                byte naapuriLattioita = getNaapuriLattioita(x, y);
                if(naapuriLattioita > 4) {
                    kartta[x][y] = true;
                } else if(naapuriLattioita < 4) {
                    kartta[x][y] = false;
                }
            }
        }
    }
    
    /**
     * Laskee, montako lattiaa solulla on naapurina.
     * 
     * @param soluX Solun sijanti x-akselilla
     * @param soluY Solun sijainti y-akselilla
     * @return solun naapurilattioiden määrä
     */
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
                if(kartta[muutosX][muutosY]) {
                    maara++;
                }
            }
        }
        return maara;
    }
    
    /**
     * Etsii karttataulukosta kaikki huoneet ja lisää ne huoneet-listalle.
     */
    private void etsiHuoneet() {
        boolean[][] kartanKopio = kopioiKartta();
        for(int x = 0; x < kartanLeveys; x++) {
            for(int y = 0; y < kartanKorkeus; y++) {
                if(kartanKopio[x][y]) {
                    CustomList<int[]> huoneenSolut = new CustomList<>();
                    etsiHuone(huoneenSolut, kartanKopio, x, y);
                    huoneet.lisaa(new CellRoom(huoneenSolut, kartta));
                }
            }
        }
    }
    
    /**
     * Etsii rekursiolla karttataulukosta kaikki 
     * yksittäiseen huoneeseen kuuluvat solut.
     * 
     * @param solut Huoneeseen kuuluvat solut
     * @param kartanKopio Huoneiden etsinnässä käytettävä karttataulukon kopio
     * @param x Käsiteltävän solun sijainti x-akselilla
     * @param y Käsiteltävän solun sijainti y-akselilla
     * @return huoneeseen kuuluvat solut
     */
    private void etsiHuone(CustomList<int[]> solut, boolean[][] kartanKopio, int x, int y) {
        if((x == 0 || x == kartanLeveys - 1) || (y == 0 || y == kartanKorkeus - 1)) return;
        if(!kartanKopio[x][y]) return;
        
        solut.lisaa(new int[]{x, y});
        kartanKopio[x][y] = false;
        
        etsiHuone(solut, kartanKopio, x, y+1);
        etsiHuone(solut, kartanKopio, x+1, y);
        etsiHuone(solut, kartanKopio, x, y-1);
        etsiHuone(solut, kartanKopio, x-1, y);
    }
    
    /**
     * Poistaa kartasta kaikki huoneet, joiden solujen määrä on 
     * pienempi kuin huoneidenMinimikoko.
     */
    private void poistaLiianPienetHuoneet() {
        CustomList<Integer> poistettavat = new CustomList<>();
        for(int i = 0; i < huoneet.koko(); i++) {
            CellRoom huone = huoneet.hae(i);
            if(huone.koko() < huoneidenMinimikoko) {
                poistaHuone(huone);
                poistettavat.lisaa(i);
            }
        }
        huoneet.poistaMonta(poistettavat);
    }
    
    /**
     * Poistaa yksittäisen huoneen karttataulukosta muuttamalla 
     * kaikki sen solut seiniksi.
     * 
     * @param huone Poistettava huone
     */
    private void poistaHuone(CellRoom huone) {
        CustomList<int[]> huoneenSolut = huone.getSolut();
        for(int i = 0; i < huoneenSolut.koko(); i++) {
            int[] solu = huoneenSolut.hae(i);
            kartta[solu[0]][solu[1]] = false;
        }
    }
    
    /**
     * Yhdistelee lähimpänä toisiaan olevia huoneita. Metodi ei varmista, 
     * että kaikki huoneet olisivat transitiivisesti yhteydessä.
     */
    private void yhdistaHuoneet() {
        if(huoneet.koko() <= 1) return;
        int[] yhdistetyt = new int[huoneet.koko()];
        for(int i = 0; i < yhdistetyt.length; i++) {
            yhdistetyt[i] = -1;
        }
        for(int i = 0; i < huoneet.koko(); i++) {
            if(yhdistetyt[i] != -1) continue;
            int lahin = getLahinHuone(i);
            lisaaKaytava(huoneet.hae(i).getYhdistajasolu(), huoneet.hae(lahin).getYhdistajasolu());
            if(yhdistetyt[lahin] == -1) {
                yhdistetyt[i] = i;
                yhdistetyt[lahin] = i;
            } else {
                yhdistetyt[i] = yhdistetyt[lahin];
            }
        }
    }
    
    /**
     * Hakee huoneet-listalta sen huoneen (indeksin), joka on lähimpänä annettua 
     * huonetta. Etäisyydet lasketaan huoneiden yhdistäjäsolujen perusteella.
     * 
     * @param huoneenIndeksi Huone, jolle etsitään naapuria
     * @return lähimmän huoneen indeksi
     */
    private int getLahinHuone(int huoneenIndeksi) {
        int[] alku = huoneet.hae(huoneenIndeksi).getYhdistajasolu();
        int lahin = 0;
        double pieninEtaisyys = Double.MAX_VALUE;
        for(int i = 0; i < huoneet.koko(); i++) {
            if(i == huoneenIndeksi) continue;
            int[] loppu = huoneet.hae(i).getYhdistajasolu();
            int muutosX = loppu[0]-alku[0];
            int muutosY = loppu[1]-alku[1];
            double etaisyys = apuMath.neliojuuri((muutosX*muutosX)+(muutosY*muutosY));
            if(etaisyys < pieninEtaisyys) {
                lahin = i;
                pieninEtaisyys = etaisyys;
            }
        }
        return lahin;
    }
    
    /**
     * Muodostaa lattiasoluista suoran käytävän pisteiden alku ja loppu 
     * välille. Käytävän paksuus määräytyy parametrin "kaytavienKoko" mukaan.
     * 
     * @param alku Käytävän alkupiste
     * @param loppu Käytävän loppupiste
     */
    private void lisaaKaytava(int[] alku, int[] loppu) {
        int deltaX = loppu[0]-alku[0];
        int deltaY = loppu[1]-alku[1];
        int x = alku[0];
        int y = alku[1];
        int askel = apuMath.signum(deltaX);
        int kaltevuusaskel = apuMath.signum(deltaY);
        int pisin = apuMath.itseisarvo(deltaX);
        int lyhin = apuMath.itseisarvo(deltaY);
        boolean kaanteinen = false;
        if(pisin < lyhin) {
            kaanteinen = true;
            pisin = apuMath.itseisarvo(deltaY);
            lyhin = apuMath.itseisarvo(deltaX);
            askel = apuMath.signum(deltaY);
            kaltevuusaskel = apuMath.signum(deltaX);
        }
        int kaltevuuskertyma = pisin/2;
        for(int i = 0; i < pisin; i++) {
            for(int kx = -kaytavienKoko; kx <= kaytavienKoko; kx++) {
                for(int ky = -kaytavienKoko; ky <= kaytavienKoko; ky++) {
                    if((x+kx <= 0 || x+kx >= kartanLeveys - 1) || (y+ky <= 0 || y+ky >= kartanKorkeus - 1)) continue;
                    if(kartta[x+kx][y+ky]) continue;
                    kartta[x+kx][y+ky] = true;
                }
            }
            if(kaanteinen) {
                y += askel;
            } else {
                x += askel;
            }
            kaltevuuskertyma += lyhin;
            if(kaltevuuskertyma >= pisin) {
                if(kaanteinen) {
                    x += kaltevuusaskel;
                }
                else {
                    y += kaltevuusaskel;
                }
                kaltevuuskertyma -= pisin;
            }
        }
    }
    
    private boolean[][] kopioiKartta() {
        boolean[][] kopio = new boolean[kartanLeveys][kartanKorkeus];
        for(int x = 0; x < kartanLeveys; x++) {
            for(int y = 0; y < kartanKorkeus; y++) {
                kopio[x][y] = kartta[x][y];
            }
        }
        return kopio;
    }
    
    public int getLeveys() {
        return this.kartanLeveys;
    }
    
    public int getKorkeus() {
        return this.kartanKorkeus;
    }
    
    public boolean[][] getKartta() {
        return this.kartta;
    }
    
    public CustomList<CellRoom> getHuoneet() {
        return this.huoneet;
    }
    
    public String getKarttaMerkkijonona() {
        char[] merkit = new char[kartanLeveys*kartanKorkeus];
        for(int x = 0; x < kartanLeveys; x++) {
            for(int y = 0; y < kartanKorkeus; y++) {
                if(kartta[x][y]) {
                    merkit[x*kartanKorkeus+y] = '.';
                } else {
                    merkit[x*kartanKorkeus+y] = '#';
                }
            }
        }
        return new String(merkit);
    }
}
