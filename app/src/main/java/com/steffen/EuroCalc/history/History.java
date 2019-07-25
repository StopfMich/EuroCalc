package com.steffen.EuroCalc.history;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.steffen.EuroCalc.MainActivity;
import com.steffen.EuroCalc.ErrorClass;
import com.steffen.EuroCalc.Logic;
import com.steffen.EuroCalc.NetHelper;
import com.steffen.EuroCalc.R;

import org.json.JSONException;

public class History extends AppCompatActivity {
    DataWarehouseSmall data = new DataWarehouseSmall();
    Logic logic = new Logic();
    NetHelper netHelp = new NetHelper();
    ErrorClass errorMsg = new ErrorClass();
    String url;
    private TextView historyInfo;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        this.historyInfo = findViewById(R.id.historyInfo);
        logic.init(this);
        errorMsg.setContext(this);
        data.setDateBeginn(null);
        data.setDateEnd(null);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
             View.SYSTEM_UI_FLAG_IMMERSIVE
                     | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                     | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                     | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                     | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                     | View.SYSTEM_UI_FLAG_FULLSCREEN);


        //Setze Werte in DataWareHouseSmall vom intetn
        Intent intent = getIntent();
        data.setCurrency(intent.getStringExtra(MainActivity.EXTRA_CURRENCY));
        data.setDateBeginn(logic.convertDateHistory(intent.getStringExtra(MainActivity.EXTRA_DATEBEGINN),getParent()));
        data.setDateEnd(logic.convertDateHistory(intent.getStringExtra(MainActivity.EXTRA_DATEEND),getParent()));
        /*
         Aufruf zur Seite mit NetHelper und passender URL
         1. set Nethelper URL
         2. ausführen und auf antwort warten
         */
        url = logic.createUrl(data.getDateBeginn(), data.getDateEnd(), data.getCurrency());
        if (data.getDateBeginn() == null && data.getDateEnd() == null) {
            historyInfo.setText(url + "\n" + getResources().getString(R.string.hisErrorNoDate));
            return;
        }
        netHelp.setURL(url);
        netHelp.getWechselkurs(new NetHelper.VolleyCallback() {


            @Override
            public void onSuccess(String resultFromWebload) throws JSONException {

                //Ausführung nach dem die Werte geladen wurden
                data.setJsObject(logic.stringToJsonObj(resultFromWebload));

                //versuche die Keys aus dem Object zu bekommen
                try {
                    data.setKeys(logic.getCurrencyKeys(data.getJsObject()));
                } catch (JSONException e) {
                    errorMsg.errorToast(getResources().getString(R.string.hisErrorCouldntFetchObj), true);
                }
                //builds Graph
                    buildGraph();
                int daysGone = data.getKeys().length;
                String firstDay = logic.convertDate(data.getKeys()[0],getParent());
                String lastDay = logic.convertDate(data.getKeys()[daysGone - 1],getParent());
                historyInfo.setText(getResources().getString(R.string.hisBeginDate)+" "+ firstDay +
                        "\n"+ getResources().getString(R.string.hisCountedDays)+" "+ daysGone +
                        "\n"+ getResources().getString(R.string.hisEndDate) + lastDay);
            }

            @Override
            public void onError() {

            }
        }, this);

    }

    private void buildGraph() throws JSONException {
        //todo Datum als x-Achsen einheit einfügen

        GraphView grafik = findViewById(R.id.grafik);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (int i = 0; i < data.getKeys().length; i++) {
            Double y = data.getJsObject().getJSONObject(data.getKeys()[i]).getDouble(data.getCurrency());
            series.appendData(new DataPoint(i+1,y),false,data.getKeys().length+15);
        }
        Viewport graViewport = grafik.getViewport();
        graViewport.setScrollable(true); // enables horizontal scrolling
        graViewport.setScrollableY(true); // enables vertical scrolling
        graViewport.setScalable(true); // enables horizontal zooming and scrolling
        graViewport.setScalableY(true); // enables vertical zooming and scrolling
        graViewport.setMinX(1);
        graViewport.setMaxX(data.getKeys().length);
        graViewport.setXAxisBoundsManual(true);


        //Achsen-Label
        GridLabelRenderer gridLabel = grafik.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(getResources().getString(R.string.day));
        gridLabel.setVerticalAxisTitle(data.getCurrency() + " " + getResources().getString(R.string.value));

        //Style
        grafik.setTitle(data.getCurrency() + getResources().getString(R.string.hisCompared));
        grafik.setTitleColor(getResources().getColor(android.R.color.white));
        grafik.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
        gridLabel.setGridColor(getResources().getColor(android.R.color.white));
        gridLabel.setHorizontalAxisTitleColor(getResources().getColor(android.R.color.white));
        gridLabel.setVerticalAxisTitleColor(getResources().getColor(android.R.color.white));
        gridLabel.setVerticalLabelsColor(getResources().getColor(android.R.color.white));
        gridLabel.setHorizontalLabelsColor(getResources().getColor(android.R.color.white));
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Double x = dataPoint.getX();
                Double y = dataPoint.getY();
                String yString = Double.toString(y);
                int xInt = (int)Math.round(x);
                String rawDate = data.getKeys()[xInt];
                String date = logic.convertDate(rawDate,getParent());
                historyInfo.setText(getResources().getString(R.string.Date)+" " + date +" "+getResources().getString(R.string.value) +": " + yString);

            }
        });
        grafik.addSeries(series);
    }
    /* https://dzone.com/articles/how-to-add-graph-in-android
       Dates als Labels --> https://github.com/jjoe64/GraphView/wiki/Dates-as-labels
       style optionen --> https://github.com/jjoe64/GraphView/wiki/Style-options
     */


}
