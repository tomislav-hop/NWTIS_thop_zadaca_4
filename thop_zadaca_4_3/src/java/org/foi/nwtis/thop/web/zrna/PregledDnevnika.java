/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.thop.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.thop.ejb.eb.Dnevnik;
import org.foi.nwtis.thop.ejb.sb.DnevnikFacade;

/**
 *
 * @author Tomislav Hop
 */
@ManagedBean
@SessionScoped
public class PregledDnevnika implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;

    public List<Dnevnik> dp;
    public List<Dnevnik> pdp = null;
    private String korisnik;
    private String ipadresa;
    private String trajanjeOd;
    private String trajanjeDo;
    private String trajanje;
    private String status;
    private boolean prikaziFiltriranje = false;

    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {

    }

    /**
     * Getter za podatke iz dnevnika Ako je lista podataka pdp prazna samo
     * dohvaća sve vrijednosti iz baze, a ako lista pdp nije prazna onda vraća
     * listu pdp
     *
     * @return
     */
    public List<Dnevnik> getDp() {
        List<Dnevnik> dnevnik = dnevnikFacade.findAll();

        if (pdp == null) {
            dp = dnevnik;
            return dp;
        } else {
            return pdp;
        }

    }

    public void setDp(List<Dnevnik> dp) {
        this.dp = dp;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getIpadresa() {
        return ipadresa;
    }

    public void setIpadresa(String ipadresa) {
        this.ipadresa = ipadresa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * metoda za prikazivanje obrasca za filtriranje
     *
     * @return
     */
    public Object otvoriFiltriranje() {
        prikaziFiltriranje = true;
        return null;
    }

    /**
     * metoda za zatvaranja obrasca za filtriranje koja također resetira listu
     * pdp kako bi se opet vidjeli svi podaci iz baze
     *
     * @return
     */
    public Object zatvoriFiltriranje() {
        prikaziFiltriranje = false;
        pdp = null;
        return null;
    }

    public boolean isPrikaziFiltriranje() {
        return prikaziFiltriranje;
    }

    public void setPrikaziFiltriranje(boolean prikaziFiltriranje) {
        this.prikaziFiltriranje = prikaziFiltriranje;
    }

    public String getTrajanjeDo() {
        return trajanjeDo;
    }

    public void setTrajanjeDo(String trajanjeDo) {
        this.trajanjeDo = trajanjeDo;
    }

    public String getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(String trajanje) {
        this.trajanje = trajanje;
    }

    public String getTrajanjeOd() {
        return trajanjeOd;
    }

    public void setTrajanjeOd(String trajanjeOd) {
        this.trajanjeOd = trajanjeOd;
    }

    /**
     * Metoda za filtriranje podataka na osnovi svih mogucih inputTextova. Za
     * svaki input privjerava ako se ta vrijednost trenutno nalazi u pomocnoj
     * listi te ako se nalazi sprema ju u drugu pomocnu listu i salje dalje
     */
    public void filtriranje() {
        pdp = new ArrayList<>();
        //Morao sam koristiti puno pomoćnih listi kako bih izbjegao ConcurrentModificationException
        List<Dnevnik> pomocnaLista = new ArrayList<>();
        List<Dnevnik> pomocnaLista2 = new ArrayList<>();
        List<Dnevnik> pomocnaLista3 = new ArrayList<>();
        List<Dnevnik> pomocnaLista4 = new ArrayList<>();
        List<Dnevnik> pomocnaLista5 = new ArrayList<>();
        if (korisnik.equals("")) {
            pdp = dp;
        } else {
            for (Dnevnik d : dp) {
                if (d.getKorisnik().equals(korisnik)) {
                    pdp.add(d);
                }
            }
        }
        if (ipadresa.equals("")) {
            pomocnaLista2 = pdp;
        } else {

            for (Dnevnik d : pdp) {
                if (d.getIpadresa().equals(ipadresa)) {
                    pomocnaLista.add(d);
                }
                pomocnaLista2 = pomocnaLista;
                pdp = pomocnaLista2;
            }
        }
        if (trajanje.equals("")) {
            pomocnaLista3 = pomocnaLista2;
        } else {
            for (Dnevnik d : pomocnaLista2) {
                if (d.getTrajanje() > Integer.parseInt(trajanje)) {
                    pomocnaLista5.add(d);
                }
                pomocnaLista3 = pomocnaLista5;
                pdp = pomocnaLista5;
            }
        }
        if (!status.equals("")) {
            for (Dnevnik d : pomocnaLista3) {
                if (d.getStatus() == Integer.parseInt(status)) {
                    pomocnaLista4.add(d);
                }
                pdp = pomocnaLista4;
            }
        }
    }
}
