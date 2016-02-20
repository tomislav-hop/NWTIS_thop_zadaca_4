/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.thop.rest.klijenti;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.foi.nwtis.thop.web.podaci.MeteoPodaci;
import org.foi.nwtis.thop.web.podaci.MeteoPrognoza;
import org.foi.nwtis.thop.web.podaci.MeteoPrognozaPodaci;

/**
 *
 * @author nwtis_1
 */
public class OWMKlijent {

    String apiKey;
    OWMRESTHelper helper;
    Client client;

    public OWMKlijent(String apiKey) {
        this.apiKey = apiKey;
        helper = new OWMRESTHelper(apiKey);
        client = ClientBuilder.newClient();
    }

    public MeteoPodaci getRealTimeWeather(String latitude, String longitude) {
        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_Current_Path());
        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        try {
            JSONObject jo = new JSONObject(odgovor);
            MeteoPodaci mp = new MeteoPodaci();
            mp.setSunRise(new Date(jo.getJSONObject("sys").getLong("sunrise")));
            mp.setSunSet(new Date(jo.getJSONObject("sys").getLong("sunset")));

            mp.setTemperatureValue(Float.parseFloat(jo.getJSONObject("main").getString("temp")));
            mp.setTemperatureMin(Float.parseFloat(jo.getJSONObject("main").getString("temp_min")));
            mp.setTemperatureMax(Float.parseFloat(jo.getJSONObject("main").getString("temp_max")));
            mp.setTemperatureUnit("celsius");

            mp.setHumidityValue(Float.parseFloat(jo.getJSONObject("main").getString("pressure")));
            mp.setHumidityUnit("%");

            mp.setPressureValue(Float.parseFloat(jo.getJSONObject("main").getString("humidity")));
            mp.setHumidityUnit("hPa");

            mp.setWindSpeedValue(Float.parseFloat(jo.getJSONObject("wind").getString("speed")));
            mp.setWindSpeedName("");

            mp.setWindDirectionValue(Float.parseFloat(jo.getJSONObject("wind").getString("deg")));
            mp.setWindDirectionCode("");
            mp.setWindDirectionName("");

            mp.setCloudsValue(jo.getJSONObject("clouds").getInt("all"));
            mp.setCloudsName(jo.getJSONArray("weather").getJSONObject(0).getString("description"));
            mp.setPrecipitationMode("");

            mp.setWeatherNumber(jo.getJSONArray("weather").getJSONObject(0).getInt("id"));
            mp.setWeatherValue(jo.getJSONArray("weather").getJSONObject(0).getString("description"));
            mp.setWeatherIcon(jo.getJSONArray("weather").getJSONObject(0).getString("icon"));

            mp.setLastUpdate(new Date(jo.getLong("dt")));

            return mp;

        } catch (JSONException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Metoda dohvaća json i sprema ga u posebno kreiranu klasu za podatke
     * prognoze
     *
     * @param latitude
     * @param longitude
     * @param noDays
     * @param adresa
     * @return
     */
    public MeteoPrognoza[] getWeatherForecast(String latitude, String longitude, int noDays, String adresa) {
        WebTarget webResource = client.target(OWMRESTHelper.getOWM_BASE_URI())
                .path(OWMRESTHelper.getOWM_ForecastDaily_Path());

        webResource = webResource.queryParam("lat", latitude);
        webResource = webResource.queryParam("lon", longitude);
        webResource = webResource.queryParam("lang", "hr");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("APIKEY", apiKey);
        webResource = webResource.queryParam("cnt", noDays);

        MeteoPrognoza[] mp = new MeteoPrognoza[noDays];

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        //System.out.println("WEB LINK: " + webResource.getUri());

        try {
            JSONObject jo = new JSONObject(odgovor);
            JSONArray lista = jo.getJSONArray("list");
            //System.out.println("LISTA LENGHT: " + lista.length());

            for (int i = 0; i < noDays; i++) {
                JSONObject dan = lista.getJSONObject(i);

                MeteoPrognozaPodaci mpp = new MeteoPrognozaPodaci();
                mpp.setTemp(Float.parseFloat(dan.getJSONObject("temp").getString("day")));
                mpp.setPressure(Float.parseFloat(dan.getString("pressure")));
                mpp.setHumidity(Float.parseFloat(dan.getString("humidity")));
                mpp.setWeather(dan.getJSONArray("weather").getJSONObject(0).getString("description"));
                mpp.setClouds(Float.parseFloat(dan.getString("clouds")));
                mpp.setWindSpeed(Float.parseFloat(dan.getString("speed")));
                mpp.setWindDeg(Float.parseFloat(dan.getString("speed")));
                mpp.setDt(dan.getString("dt"));

                MeteoPrognoza metPro = new MeteoPrognoza();
                metPro.setAdresa(adresa);

                /**
                 * Na osnovi iteracije spremam datum za koji se odnosi prognoza
                 */
                //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();

                if (i > 0) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    c.add(Calendar.DATE, i);
                    date = c.getTime();
                }

                metPro.setDan(i);
                metPro.setDatum(date);
                metPro.setPrognoza(mpp);
                mp[i] = metPro;
            }
        } catch (JSONException ex) {
            Logger.getLogger(OWMKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mp;
    }
}
