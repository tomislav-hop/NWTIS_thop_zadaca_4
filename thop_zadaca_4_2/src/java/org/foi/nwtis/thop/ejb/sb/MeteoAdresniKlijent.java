/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.thop.ejb.sb;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.foi.nwtis.thop.rest.klijenti.GMKlijent;
import org.foi.nwtis.thop.rest.klijenti.OWMKlijent;
import org.foi.nwtis.thop.web.podaci.Lokacija;
import org.foi.nwtis.thop.web.podaci.MeteoPodaci;
import org.foi.nwtis.thop.web.podaci.MeteoPrognoza;

/**
 *
 * @author NWTiS_1
 */
@Stateless
@LocalBean
public class MeteoAdresniKlijent {

    private String apiKey;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void postaviKorisnickePodatke(String apiKey) {
        this.apiKey = apiKey;
    }

    public MeteoPodaci dajVazeceMeteoPodatke(String adresa) {
        GMKlijent gmk = new GMKlijent();
        Lokacija l = gmk.getGeoLocation(adresa);

        OWMKlijent owmk = new OWMKlijent(this.apiKey);

        MeteoPodaci mp = owmk.getRealTimeWeather(l.getLatitude(),
                l.getLongitude());     
        
        return mp;
    }

    public MeteoPrognoza[] dajMeteoPrognoze(String adresa, int brojDana) {
        Lokacija l = dajLokaciju(adresa);
        
        
        //TODO api key je hardkodiran
        OWMKlijent owmk = new OWMKlijent("2e9347d77e1b419858b1707cc02b6ae3");
        MeteoPrognoza[] mp = owmk.getWeatherForecast(l.getLatitude(), l.getLongitude(), brojDana, adresa);
        
        return mp;
    }

    public Lokacija dajLokaciju(String adresa) {
        GMKlijent gmk = new GMKlijent();
        Lokacija l = gmk.getGeoLocation(adresa);
        return l;
    }
}
