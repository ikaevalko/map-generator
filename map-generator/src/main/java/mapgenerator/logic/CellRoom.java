package mapgenerator.logic;

import mapgenerator.util.CustomList;

/**
 * Soluista koostuvaa huonetta kuvaava luokka.
 */
public class CellRoom {
    private CustomList<int[]> solut;
    private int[] yhdistajasolu;
    private boolean[][] kartta;
    
    /**
     * Luo uuden huoneen.
     * 
     * @param solut kaikki huoneeseen kuuluvat solut listana
     * @param kartta generoitava kartta, johon huone kuuluu
     */
    public CellRoom(CustomList<int[]> solut, boolean[][] kartta) {
        this.solut = solut;
        this.yhdistajasolu = this.solut.hae((int)(this.solut.koko()/2));
        this.kartta = kartta;
    }
    
    public CustomList<int[]> getSolut() {
        return this.solut;
    }
    
    public int[] getYhdistajasolu() {
        return this.yhdistajasolu;
    }
    
    public int koko() {
        return this.solut.koko();
    }
}
