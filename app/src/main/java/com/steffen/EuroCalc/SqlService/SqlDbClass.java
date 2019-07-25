package com.steffen.EuroCalc.SqlService;

import com.orm.SugarRecord;

public class SqlDbClass extends SugarRecord {
    String jsStrings;
    String lastUpdated;
    int lastRightId;
    boolean firstStartup;


    public SqlDbClass(){}

    public SqlDbClass(String jsStrings,String lastUpdatet ,int lastRightId, boolean firstStartup) {
        this.jsStrings = jsStrings;
        this.lastUpdated = lastUpdatet;
        this.lastRightId = lastRightId;
        this.firstStartup = firstStartup;
    }

    public void setJsStrings(String jsStrings) {
        this.jsStrings = jsStrings;
    }
    public String getJsStrings() {
        return this.jsStrings;
    }

    public void setLastRightId(int lastRightId) {
        this.lastRightId = lastRightId;
    }
    public int getLastRightId() {
        return lastRightId;
    }

    public boolean isFirstStartup() {
        return firstStartup;
    }
    public void setFirstStartup(boolean firstStartup) {
        this.firstStartup = firstStartup;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public String getLastUpdated() {
        return lastUpdated;
    }


    public SqlDbClass getSQL() {
        return SqlDbClass.findById(SqlDbClass.class,1L);
    }
}
