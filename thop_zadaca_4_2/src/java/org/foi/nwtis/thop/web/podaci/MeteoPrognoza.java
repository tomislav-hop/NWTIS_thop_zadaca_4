/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.thop.web.podaci;

import java.util.Date;

/**
 *
 * @author dkermek
 */
public class MeteoPrognoza {

    private String adresa;
    private int dan;
    private Date datum;
    private MeteoPrognozaPodaci prognoza;

    public MeteoPrognoza() {
    }

    public MeteoPrognoza(String adresa, int dan, MeteoPrognozaPodaci prognoza) {
        this.adresa = adresa;
        this.dan = dan;
        this.prognoza = prognoza;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public int getDan() {
        return dan;
    }

    public void setDan(int dan) {
        this.dan = dan;
    }

    public MeteoPrognozaPodaci getPrognoza() {
        return prognoza;
    }

    public void setPrognoza(MeteoPrognozaPodaci prognoza) {
        this.prognoza = prognoza;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }


}
