/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.thop.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.foi.nwtis.thop.ejb.eb.Adrese;
import org.foi.nwtis.thop.ejb.eb.Dnevnik;
import org.foi.nwtis.thop.ejb.sb.AdreseFacade;
import org.foi.nwtis.thop.ejb.sb.DnevnikFacade;
import org.foi.nwtis.thop.ejb.sb.MeteoAdresniKlijent;
import org.foi.nwtis.thop.web.podaci.Lokacija;
import org.foi.nwtis.thop.web.podaci.MeteoPrognoza;

/**
 *
 * @author NWTiS_1
 */
@ManagedBean
@SessionScoped
public class OdabirAdresaPrognoza implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;
    
    @EJB
    private MeteoAdresniKlijent meteoAdresniKlijent;
    @EJB
    private AdreseFacade adreseFacade;
    
    private String novaAdresa;
    private String odabranaAdresaZaDodati;
    private String odabranaAdresaZaMaknuti;
    private String azuriranaAdresa;
    private String originalnaAdresa;
    private List<String> aktivneAdrese;
    private List<String> prognozaAdrese = new ArrayList<>();
    
    private MeteoPrognoza[] meteoPrognoza;
    private List<MeteoPrognoza> mpp = new ArrayList<>();
    
    private int brojDana;
    private String poruka;
    
    private boolean prikaziAzuriranje = false;
    private boolean prikaziPrognoze = false;
    private boolean prikaziError = false;
    
    public void spremiAkciju(String korisnik, int status, String url, String ip, int trajanje) {
        Dnevnik dnevnik = new Dnevnik();
        dnevnik.setIpadresa(ip);
        dnevnik.setKorisnik(korisnik);
        dnevnik.setUrl(url);
        dnevnik.setStatus(status);
        dnevnik.setTrajanje(trajanje);
        Date date = new Date();     
        dnevnik.setVrijeme(date);
        dnevnikFacade.create(dnevnik);
    }

    /**
     * Metoda dodaje string u listu prognozaAdrese
     */
    public void prebaciAdresu() {
        prognozaAdrese.add(odabranaAdresaZaDodati);
        spremiAkciju("thop", 0, "url", "localhost", 0);
    }

    /**
     * Metoda brise string iz liste prognozaAdrese dodaje ga u aktivneAdrese te
     * poziva funkciju UkloniPrognoze
     */
    public void prebaciAdresuNazad() {
        prognozaAdrese.remove(odabranaAdresaZaMaknuti);
        aktivneAdrese.add(odabranaAdresaZaMaknuti);
        UkloniPrognoze();
        spremiAkciju("thop", 1, "url", "localhost", 0);
    }

    /**
     * Metoda dohvaća prognoze za sve adrese u listi prognozaAdrese i sprema ih
     * u listu prognoza mpp
     */
    public void DohvatiPrognoze() {
        spremiAkciju("thop", 2, "url", "localhost", 0);
        mpp = new ArrayList<>();
        for (String adresa : prognozaAdrese) {
            MeteoPrognoza[] mp = meteoAdresniKlijent.dajMeteoPrognoze(adresa, brojDana);
            for (int i = 0; i < brojDana; i++) {
                mpp.add(mp[i]);
            }
        }
        
    }

    /**
     * Metoda brise prognoze iz liste prognoza na osnovi odabrane adrese
     */
    public void UkloniPrognoze() {
        List<MeteoPrognoza> pomocna = new ArrayList<>();
        for (MeteoPrognoza mp : mpp) {
            if (!mp.getAdresa().equals(odabranaAdresaZaMaknuti)) {
                pomocna.add(mp);
            }
        }
        mpp = pomocna;
    }

    /**
     * Creates a new instance of OdabirAdresaPrognoza
     */
    public OdabirAdresaPrognoza() {
    }
    
    public String getNovaAdresa() {
        return novaAdresa;
    }
    
    public void setNovaAdresa(String novaAdresa) {
        this.novaAdresa = novaAdresa;
    }
    
    public String getOdabranaAdresaZaDodati() {
        return odabranaAdresaZaDodati;
    }
    
    public void setOdabranaAdresaZaDodati(String odabranaAdresaZaDodati) {
        this.odabranaAdresaZaDodati = odabranaAdresaZaDodati;
    }
    
    public String getOdabranaAdresaZaMaknuti() {
        return odabranaAdresaZaMaknuti;
    }
    
    public void setOdabranaAdresaZaMaknuti(String odabranaAdresaZaMaknuti) {
        this.odabranaAdresaZaMaknuti = odabranaAdresaZaMaknuti;
    }
    
    public String getAzuriranaAdresa() {
        return azuriranaAdresa;
    }
    
    public void setAzuriranaAdresa(String azuriranaAdresa) {
        this.azuriranaAdresa = azuriranaAdresa;
    }

    /**
     * Metoda dohvaća sve adrese iz baze i dodaje ih u aktivneAdrese ako se ne
     * nalaze u prognozaAdrese
     *
     * @return
     */
    public List<String> getAktivneAdrese() {
        aktivneAdrese = new ArrayList<>();
        List<Adrese> adrese = adreseFacade.findAll();
        for (Adrese a : adrese) {
            boolean uPrognozaAdrese = false;
            for (String adresa : prognozaAdrese) {
                if (adresa.equals(a.getAdresa())) {
                    uPrognozaAdrese = true;
                }
            }
            if (uPrognozaAdrese == false) {
                aktivneAdrese.add(a.getAdresa());
            }
        }
        return aktivneAdrese;
    }
    
    public void setAktivneAdrese(List<String> aktivneAdrese) {
        this.aktivneAdrese = aktivneAdrese;
    }
    
    public List<String> getPrognozaAdrese() {
        return prognozaAdrese;
    }
    
    public void setPrognozaAdrese(List<String> prognozaAdrese) {
        this.prognozaAdrese = prognozaAdrese;
    }

    /**
     * Metoda ubacije adresu u bazu podataka
     *
     * @return
     */
    public Object upisiAdresu() {
        spremiAkciju("thop", 3, "url", "localhost", 0);
        Adrese adresa = new Adrese();
        adresa.setAdresa(novaAdresa);
        Lokacija l = meteoAdresniKlijent.dajLokaciju(novaAdresa);
        adresa.setLatitude(l.getLatitude());
        adresa.setLongitude(l.getLongitude());
        adreseFacade.create(adresa);
        return null;
    }

    /**
     * Metoda prolazi kroz sve adrese u bazi i u slucaju da pronađe adresu ona
     * ju brise i ubacuje novu adresu u bazu
     *
     * @return
     */
    public Object azurirajAdresu() {
        spremiAkciju("thop", 4, "url", "localhost", 0);
        List<Adrese> adrese = adreseFacade.findAll();
        for (Adrese a : adrese) {
            if (a.getAdresa().equals(originalnaAdresa)) {
                adreseFacade.remove(a);
            }
        }
        Adrese adresa = new Adrese();
        adresa.setAdresa(azuriranaAdresa);
        Lokacija l = meteoAdresniKlijent.dajLokaciju(azuriranaAdresa);
        adresa.setLatitude(l.getLatitude());
        adresa.setLongitude(l.getLongitude());
        adreseFacade.create(adresa);
        prikaziAzuriranje = false;
        return null;
    }

    /**
     * prikazujem obrazac za azuriranje te spremam odabranu adresu u orginalnu
     * adresu te istu spremam u azuriranaAdresa kako bi odmah pisala adresa u
     * inputtextu
     *
     * @return
     */
    public Object otvoriAzuriranjeAdrese() {
        prikaziAzuriranje = true;
        originalnaAdresa = odabranaAdresaZaDodati;
        azuriranaAdresa = odabranaAdresaZaDodati;
        return null;
    }

    /**
     * zatvaranje obrasca za ažuriranje
     *
     * @return
     */
    public Object zatvoriPrognozu() {
        prikaziPrognoze = false;
        return null;
    }

    /**
     * otvaranje obrasca za prikazivanje prognoze ako je broj unesenih dana
     * između 1 i 16 ukoliko broj nije valjan ispisujem poruku greške
     *
     * @return
     */
    public Object otvoriPrikazivanjePrognoze() {
        if (brojDana > 0 && brojDana < 17) {
            prikaziError = false;
            DohvatiPrognoze();
            prikaziPrognoze = true;
        } else {
            prikaziError = true;
            poruka = "Broj dana mora biti između 1 i 16";
        }
        
        return null;
    }
    
    public boolean isPrikaziAzuriranje() {
        return prikaziAzuriranje;
    }
    
    public void setPrikaziAzuriranje(boolean prikaziAzuriranje) {
        this.prikaziAzuriranje = prikaziAzuriranje;
    }
    
    public boolean isPrikaziPrognoze() {
        return prikaziPrognoze;
    }
    
    public void setPrikaziPrognoze(boolean prikaziPrognoze) {
        this.prikaziPrognoze = prikaziPrognoze;
    }
    
    public int getBrojDana() {
        return brojDana;
    }
    
    public void setBrojDana(int brojDana) {
        this.brojDana = brojDana;
    }
    
    public MeteoPrognoza[] getMeteoPrognoza() {
        return meteoPrognoza;
    }
    
    public void setMeteoPrognoza(MeteoPrognoza[] meteoPrognoza) {
        this.meteoPrognoza = meteoPrognoza;
    }
    
    public List<MeteoPrognoza> getMpp() {
        return mpp;
    }
    
    public void setMpp(List<MeteoPrognoza> mpp) {
        this.mpp = mpp;
    }
    
    public String getPoruka() {
        return poruka;
    }
    
    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }
    
    public boolean isPrikaziError() {
        return prikaziError;
    }
    
    public void setPrikaziError(boolean prikaziError) {
        this.prikaziError = prikaziError;
    }
    
}
