package com.steffen.EuroCalc;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class DataWarehouse {
    Logic logic = new Logic();
    ErrorClass errorMsg = new ErrorClass();
    private Context context;
    private JSONObject currencyArray;
    private JSONObject currencyRates;
    private String[] currencyKeys;
    private String lastUpdated;
    private String currentCurrency;
    private Double currentCurrencyValue;
    private String historyDateBeginn;
    private String historyDateEnd;

    public void init(Context context) {
        this.context = context;
        logic.init(context);
        errorMsg.setContext(context);
    }

    public void storeStringToJsonobjects(String string) {
        String dumpString;
        try {
            //todo Monat muss manuell gesetzt werden?????
            JSONObject obj = new JSONObject(string);
            currencyArray = obj;
            dumpString = obj.getString("date");
            lastUpdated = logic.convertDate(dumpString, this.context);

            dumpString = obj.getString("rates");
            currencyRates = obj.getJSONObject("rates");
            setCurrencyKeys();


        } catch (Throwable t) {
            Log.e("MyApp", "Konnte String nicht in Json umwandeln");
            errorMsg.errorToast("Fehler beim umwandeln der Datei, starte die App neu",false);
        }
    }

    public Double getCurrencyValue(String currency) {
        try {

            return currencyRates.getDouble(currency);
        } catch (Exception e) {
            Log.e("MyApp", "Konnte String nicht in Json umwandeln");
            return null;

        }
    }

    private void setCurrencyKeys() throws JSONException {
        ArrayList<String> keysFinished = new ArrayList<String>();
        JSONArray keys = currencyRates.names();
        for (int i = 0; i < keys.length(); i++) {
            keysFinished.add(keys.getString(i));
        }
        String[] result = new String[keysFinished.size()];
        keysFinished.toArray(result);
        Arrays.sort(result);
        this.currencyKeys = result;
    }

    public JSONObject getCurrencyRates() {
        return currencyRates;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getCurrentCurrency() {
        return currentCurrency;
    }

    public Double getCurrentCurrencyValue() {
        return currentCurrencyValue;
    }

    public void setCurrentCurrencyValue(Double value) {
        this.currentCurrencyValue = value;
    }

    public void setCurrentCurrency(String currency) {
        this.currentCurrency = currency;
    }

    public String[] getCurrencyKeys() {
        return currencyKeys;
    }

    public void setHistoryDateBeginn(String s) {
        this.historyDateBeginn = s;
    }
    public void setHistoryDateEnd (String s) {
        this.historyDateEnd = s;
    }
    public String getHistoryDateBeginn() {
        return historyDateBeginn;
    }
    public String getHistoryDateEnd() {
        return historyDateEnd;
    }
}

