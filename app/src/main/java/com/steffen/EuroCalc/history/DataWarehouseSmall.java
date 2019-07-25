package com.steffen.EuroCalc.history;

import org.json.JSONObject;
/*
DateWarehouse für History
 */
public class DataWarehouseSmall {

    private String dateBeginn;
    private String dateEnd;
    private String currency;
    private JSONObject jsObject;
    private String[] keys;

    /*
     * Get-Methoden
     */
    public String getDateBeginn() {
        return dateBeginn;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public String getCurrency() {
        return currency;
    }

    public String[] getKeys() {
        return keys;
    }

    public JSONObject getJsObject() {
        return jsObject;
    }

    /*
     * Set-Methoden
     */
    public void setDateBeginn(String dateBeginn) {
        this.dateBeginn = dateBeginn;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    public void setJsObject(JSONObject jsObject) {
        this.jsObject = jsObject;
    }
}
