package com.steffen.EuroCalc;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.steffen.EuroCalc.SqlService.SqlDbClass;
import com.steffen.EuroCalc.history.History;
import com.steffen.EuroCalc.ocrreader.OcrCaptureActivity;
import com.steffen.EuroCalc.speechToText.StoredNames;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_CURRENCY = "com.steffen.geldkurs.CURRENCY";
    public static final String EXTRA_CURRENCY_VALUE = "com.steffen.geldkurs.CURRENCY_VALUE";
    public static final String EXTRA_DATEBEGINN = "com.steffen.geldkurs.DATEBEGINN";
    public static final String EXTRA_DATEEND = "com.steffen.geldkurs.DATEEND";
    public static final int REQ_CODE_SPEECH_INPUT = 100;
    boolean isUser = true;
    boolean firstStart;
    SqlDbClass sql;
    NetHelper netHelper = new NetHelper();
    Logic logic = new Logic();
    DataWarehouse data = new DataWarehouse();
    ErrorClass errorMsg = new ErrorClass();
    StoredNames stNames = new StoredNames();
    CustomAdapter adapter;
    EditText valueCurrency;
    EditText euroValueView;
    TextView lastUpdate;
    Spinner currencyText;
    Button retry;

    @Override
    // Checkt ob erster Start und checkt nach Updates
    protected void onCreate(Bundle savedInstanceState) {
        netHelper.setURL("https://api.exchangeratesapi.io/latest");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        valueCurrency = findViewById(R.id.valueCurrency);
        euroValueView = findViewById(R.id.valueEuro);
        lastUpdate = findViewById(R.id.lastUpdate);
        currencyText = findViewById(R.id.spinnerCurrency);
        retry = findViewById(R.id.retry);

        /*
        Übergibt context für die Klassen.
         */
        data.init(this);
        logic.init(this);
        errorMsg.setContext(this);
        try {
            sql = sql.getSQL();
            if (!sql.getJsStrings().equals("")) {
                firstStart = false;
            } else {
                errorMsg.errorToast("First startup - could take a minute", false);
                firstStart = true;
            }
        } catch (Exception e) {
            sql = new SqlDbClass(null, null, 0, true);
        }

        if (firstStart) {
            netHelper.getWechselkurs(new NetHelper.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        sql = sql.getSQL();
                        sql.setJsStrings(result);
                        sql.save();
                    } catch (NullPointerException e) {
                        sql = new SqlDbClass(result, null, 0, true);
                        sql.save();
                    }
                    startAfterVolley(result);
                    sql = sql.getSQL();
                    sql.setLastUpdated(data.getLastUpdated());
                    sql.save();
                }

                public void onError() {
                    try {
                        sql = sql.getSQL();
                        String result = sql.getJsStrings();
                        if (result != null)
                            startAfterVolley(result);
                    } catch (Exception e) {
                        errorMsg.errorToast(getResources().getString(R.string.updateError), true);
                        retry.setVisibility(View.VISIBLE);
                    }
                }
            }, this);
        } else if (!firstStart) {
            sql = sql.getSQL();
            String result = sql.getJsStrings();
            startAfterVolley(result);
        } else {
            errorMsg.errorToast(getResources().getString(R.string.updateError), true);
            errorMsg.errorToast("Fehler beim Start", true);
            retry.setVisibility(View.VISIBLE);
        }
    }

    // Wird mit OnCreate ausgeführt
    private void startAfterVolley(final String result) {
        data.storeStringToJsonobjects(result);
        lastUpdate.setText(getResources().getString(R.string.lastUpdatet, data.getLastUpdated()));
        euroValueView.setText("1");
        pupolateDropdown(currencyText);
        currencyText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    sql = sql.getSQL();
                    sql.setLastRightId(currencyText.getSelectedItemPosition());
                    sql.save();
                } catch (Exception e) {
                    errorMsg.errorToast("Error at: 103-113", true);
                }

                data.setCurrentCurrency(currencyText.getSelectedItem().toString());
                data.setCurrentCurrencyValue(data.getCurrencyValue(data.getCurrentCurrency()));
                int img = logic.setImage(data.getCurrentCurrency());
                if (img != 0) {
                    currencyText.setBackgroundResource(img);
                    valueCurrency.setText(logic.calculateOtherCurrency(
                            euroValueView.getText().toString(), data.getCurrentCurrencyValue().toString())
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        euroValueView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // todo update-Funktion mit Time-Check
                if (isUser) {
                    isUser = false;
                    valueCurrency.setText(logic.calculateOtherCurrency(
                            euroValueView.getText().toString(), data.getCurrentCurrencyValue().toString()));
                } else {
                    isUser = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        valueCurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // todo update-Funktion mit Time-Check
                if (isUser) {
                    isUser = false;
                    euroValueView.setText(logic.calculateToEuro(
                            data.getCurrentCurrencyValue().toString(), valueCurrency.getText().toString())
                    );
                } else {
                    isUser = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        if (data.getCurrencyRates() == null) {
            this.recreate();
        }
        sql = sql.getSQL();
        sql.setLastUpdated(data.getLastUpdated());
        sql.save();
        if (logic.timeToUpdate(data.getLastUpdated())) {
            retry.setVisibility(View.VISIBLE);
            errorMsg.errorToast(getResources().getString(R.string.updateFound), true);
        }
    }

    // Setzt im Spinner die Flaggen
    private void pupolateDropdown(Spinner spinner) {
        String[] countrys = data.getCurrencyKeys();
        this.adapter = new CustomAdapter(this, countrys);
        spinner.setDropDownHorizontalOffset(5);
        spinner.setDropDownVerticalOffset(5);
        spinner.setAdapter(adapter);
        try {
            sql = sql.getSQL();
            spinner.setSelection(sql.getLastRightId());
        } catch (Exception e) {
            errorMsg.errorToast("Error-Code: 185-190", true);
        }
        /*
          Beim start wird die Zuletzt gewählte Währung ausgewählt, wenn vorhanden ist
         */
    }

    // History Klasse (Datepicker & HistoryStart)
    public void openCalenderStart(View v) {
        final Button anfangsDatum = findViewById(R.id.datePickerBeginn);
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //anfangsDatum.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);    //todo String
                        String dayOfMonthS = String.valueOf(dayOfMonth);
                        String monthOfYearS = String.valueOf(monthOfYear + 1);
                        String yearS = String.valueOf(year);
                        anfangsDatum.setText(getResources().getString(R.string.DateWrittenOut, dayOfMonthS, monthOfYearS, yearS));    //todo String
                        data.setHistoryDateBeginn(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, year, month, day);
        picker.show();
    }

    public void openCalenderEnd(View v) {
        final Button endDatum = findViewById(R.id.datePickerEnd);
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dayOfMonthS = String.valueOf(dayOfMonth);
                        String monthOfYearS = String.valueOf(monthOfYear + 1);
                        String yearS = String.valueOf(year);
                        endDatum.setText(getResources().getString(R.string.DateWrittenOut, dayOfMonthS, monthOfYearS, yearS));
                        //endDatum.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);        //todo String
                        data.setHistoryDateEnd(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, year, month, day);
        picker.show();
    }

    public void historyStart(View view) {

        Intent intent = new Intent(this, History.class);
        String currency = data.getCurrentCurrency();
        String dateBeginn = data.getHistoryDateBeginn();
        String dateEnd = data.getHistoryDateEnd();
        intent.putExtra(EXTRA_CURRENCY, currency);
        intent.putExtra(EXTRA_DATEBEGINN, dateBeginn);
        intent.putExtra(EXTRA_DATEEND, dateEnd);
        startActivity(intent);
    }

    // Kamera-Live scan
    public void startCam(View view) {
        Intent intent = new Intent(this, OcrCaptureActivity.class);
        String currency = data.getCurrentCurrency();
        String currencyValue = String.valueOf(data.getCurrentCurrencyValue());
        intent.putExtra(EXTRA_CURRENCY, currency);
        intent.putExtra(EXTRA_CURRENCY_VALUE, currencyValue);
        startActivity(intent);
    }

    public void restart(View view) {
        this.recreate();
    }

    // Spracheingabe für Flaggen auswahl
    public void startVoiceInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        if (Locale.getDefault().getLanguage().equals("de")) {
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        } else {
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        }
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getResources().getString(R.string.SA_Prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            errorMsg.errorToast(getResources().getString(R.string.SA_noSupport), true);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.wikihow.tech/Activate-Google-Voice-Typing-on-Android"));
            startActivity(browserIntent);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int spinnerItemId;
        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS
                );
                String symbol;
                if (Locale.getDefault().getLanguage().equals("de")) {
                    symbol = stNames.wordToSymbolDe(result.get(0));
                    if (symbol == null) {
                        symbol = stNames.wordToSymbolDe(result.get(1));
                    }
                } else {
                    symbol = stNames.wordToSymbolEn(result.get(0));
                    if (symbol == null) {
                        symbol = stNames.wordToSymbolDe(result.get(1));
                    }
                }
                if (symbol == null) {
                    errorMsg.errorToast(getResources().getString(R.string.SA_notFound, result.get(0)), true);
                } else {
                    spinnerItemId = adapter.getItemByString(symbol);
                    sql = sql.getSQL();
                    sql.setLastRightId(spinnerItemId);
                    sql.save();
                    //this.recreate();
                    currencyText.setSelection(spinnerItemId);
                }
            }
        }
    }
}