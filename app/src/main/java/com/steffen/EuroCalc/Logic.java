package com.steffen.EuroCalc;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.orm.dsl.Ignore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Logic {
    ErrorClass errorMsg = new ErrorClass();

    private Context context;

    public void init(Context context) {
        this.context = context;
        errorMsg.setContext(context);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public String convertDate(String date, Context context) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd.mm.yyyy");

        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (Exception e) {
            errorMsg.errorToast("Fehler beim Formatieren des Datums", false);
            return date;
        }
    }

    public int setImage(String currency) {
        int imgId = 0;
        switch (currency) {
            case "THB":
                imgId = R.drawable.th;
                break;
            case "PHP":
                imgId = R.drawable.ph;
                break;
            case "CZK":
                imgId = R.drawable.cz;
                break;
            case "BRL":
                imgId = R.drawable.br;
                break;
            case "CHF":
                imgId = R.drawable.ch;
                break;
            case "INR":
                imgId = R.drawable.in;
                break;
            case "ISK":
                imgId = R.drawable.is;
                break;
            case "HRK":
                imgId = R.drawable.hr;
                break;
            case "BGN":
                imgId = R.drawable.bg;
                break;
            case "NOK":
                imgId = R.drawable.no;
                break;
            case "USD":
                imgId = R.drawable.us;
                break;
            case "CNY":
                imgId = R.drawable.cn;
                break;
            case "RUB":
                imgId = R.drawable.ru;
                break;
            case "SEK":
                imgId = R.drawable.se;
                break;
            case "MYR":
                imgId = R.drawable.my;
                break;
            case "SGD":
                imgId = R.drawable.sg;
                break;
            case "ILS":
                imgId = R.drawable.il;
                break;
            case "TRY":
                imgId = R.drawable.tr;
                break;
            case "PLN":
                imgId = R.drawable.pl;
                break;
            case "NZD":
                imgId = R.drawable.nz;
                break;
            case "HKD":
                imgId = R.drawable.hk;
                break;
            case "RON":
                imgId = R.drawable.ro;
                break;
            case "MXN":
                imgId = R.drawable.mx;
                break;
            case "CAD":
                imgId = R.drawable.ca;
                break;
            case "AUD":
                imgId = R.drawable.au;
                break;
            case "GBP":
                imgId = R.drawable.gb;
                break;
            case "KRW":
                imgId = R.drawable.kr;
                break;
            case "ZAR":
                imgId = R.drawable.za;
                break;
            case "JPY":
                imgId = R.drawable.jp;
                break;
            case "DKK":
                imgId = R.drawable.dk;
                break;
            case "IDR":
                imgId = R.drawable.id;
                break;
            case "HUF":
                imgId = R.drawable.hu;
                break;
            case "clear":
                imgId = 0;
        }
        return imgId;
    }

    public String calculateOtherCurrency(String euro, String currency) {
        DecimalFormat dcF = new DecimalFormat("#.##");
        double result = 0;
        if (!euro.equals("") && !currency.equals("")) {
            if (euro.equals(".")) {
                euro = "0.";
            }
            try {
                double thisEuro = Double.parseDouble(euro);
                double thisCurrency = Double.parseDouble(currency);
                result = round(thisEuro * thisCurrency, 2);
                return dcF.format(result);
            } catch (NumberFormatException e) {
                errorMsg.errorToast("Fehler: versuchen sie ein anderes Zahlen-Format", false);
            }
        }
        return "0";
    }

    public String calculateToEuro(String currentCurencyValue, String currencyAmount) {
        DecimalFormat dcF = new DecimalFormat("#.##");
        if (!currentCurencyValue.equals("") && !currencyAmount.equals("")) {
            if (currencyAmount.equals(".")) {
                currencyAmount = "0.";
            }
            try {
                double value = Double.parseDouble(currentCurencyValue);
                double amount = Double.parseDouble(currencyAmount);
                double result = round(amount / value, 2);
                return dcF.format(result);
            } catch (NumberFormatException e) {
                errorMsg.errorToast("Fehler: versuchen sie ein anderes Zahlen-Format", false);
            }
        }
        return "0";
    }

    public int[] getFlaggsArray(String[] countrys) {
        int[] result = new int[countrys.length];
        for (int i = 0; i < countrys.length; i++) {
            result[i] = this.setImage(countrys[i]);
        }
        return result;
    }

    public boolean timeToUpdate(String Date) {
        SimpleDateFormat sdfnext = new SimpleDateFormat("dd.mm.yyyy hh:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy hh:mm");
        Calendar current = Calendar.getInstance(), next = Calendar.getInstance();

        try {
            Date lastDate = sdfnext.parse(Date + " 18:00");
            next.setTime(lastDate);
            next.set(Calendar.MONTH, current.get(Calendar.MONTH));
            if (current.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                next.add(Calendar.DAY_OF_MONTH, 1);
            } else {
                next.add(Calendar.DAY_OF_MONTH, 3);
            }
            Log.i("Dates", "Current: " + current.getTime());
            Log.i("Dates", "Future: " + next.getTime());
            if (current.after(next) && current.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && current.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                return true;
            } else return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
     ***********************************************************************************************
     ***********************************Funktionen NUR für History**********************************
     ***********************************************************************************************
     */

    public String createUrl(String dateBeginn, String dateEnd, String currency) {
        String url = "https://api.exchangeratesapi.io/history?";
        String dateStart = "start_at=" + dateBeginn;
        String dateStop = "&end_at=" + dateEnd;
        String symbols = "&symbols=" + currency;
        String result = url + dateStart + dateStop + symbols;
        return result;
    }

    public JSONObject stringToJsonObj(String string) {

        try {
            String dumpString = new JSONObject(string).getString("rates");
            JSONObject obj = new JSONObject(dumpString);
            return obj;
        } catch (JSONException e) {
            errorMsg.errorToast("Fehler beim umwandelm vom String in Json: App neustarten", true);
        }
        return null;
    }

    public String[] getCurrencyKeys(JSONObject object) throws JSONException {
        ArrayList<String> keysFinished = new ArrayList<String>();
        JSONArray keys = object.names();
        for (int i = 0; i < keys.length(); i++) {
            keysFinished.add(keys.getString(i));
        }
        String[] result = new String[keysFinished.size()];
        keysFinished.toArray(result);
        Arrays.sort(result);
        return result;
    }

    public String convertDateHistory(String date, Context context) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-mm-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-mm-dd");

        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (Exception e) {
            errorMsg.errorToast("Fehler beim Formatieren des Datums", false);
            return date;
        }
    }


    /*
     ***********************************************************************************************
     ***********************************Funktionen NUR für Kamera***********************************
     ***********************************************************************************************
     */

    public String createCamString(String currency, boolean toEuro, Context context) {
        // todo hier euro mit variablen ersetzen
        String result;
        if (toEuro) {
            result = context.getResources().getString(R.string.camInfBottem, currency, "EUR");
        } else {
            result = context.getResources().getString(R.string.camInfBottem, "EUR", currency);
        }
        return result;
    }

    public String tryToTransformInput(String input, String currencyValue, boolean toEuro) {
        Double fInput = new Double(0);
        boolean couldTransfrom = false;
        String result = "";
        try {
            fInput = Double.parseDouble(input);
            couldTransfrom = true;
            result = String.valueOf(fInput);
        } catch (Exception e) {
            Double errorHandling = transfromInputERRORhandling(input);
            if (errorHandling == 0) {
                couldTransfrom = false;
            } else {
                result = longNumberShorter(errorHandling);
                couldTransfrom = true;
            }
        }
        if (couldTransfrom) {
            if (toEuro) {
                return calculateOtherCurrency(currencyValue, result);
            } else {
                return calculateToEuro(currencyValue, result);
            }
        }
        return "No Number";
    }

    private double transfromInputERRORhandling(String input) {
        int length = input.length();
        char c;
        String dots = ",.";
        String result = "";
        Double resultDouble;

        int dotAmount = 0, comAmount = 0;
        for (int i = 0; i < length; i++) {
            c = input.charAt(i);
            if (Character.isDigit(c)) {
                result = result + c;
            } else if (c == dots.charAt(0)) {
                result = result + ".";
                dotAmount++;
            } else if (c == dots.charAt(1)) {
                result = result + ",";
                comAmount++;
            }
        }
        //todo PUNKT und KOMMA sortieren
        if ((dotAmount == 1 && comAmount == 1) || dotAmount > 1 || comAmount > 1) {
            result = sortDot(dotAmount, comAmount, result);
        }
        try {
            resultDouble = Double.parseDouble(result);
        } catch (Exception e) {
            return 0;
        }


        return resultDouble;
    }

    private String sortDot(int dotAmount, int comAmount, String result) {
        char disPoint, notDisPoint, c;
        int length = result.length();
        String resultFinal = "";
        if (dotAmount < comAmount) {
            disPoint = '.';
            notDisPoint = ',';
        } else {
            disPoint = ',';
            notDisPoint = '.';
        }
        for (int i = 0; i < length; i++) {
            c = result.charAt(i);
            if (c == disPoint) {
                resultFinal = resultFinal + ".";
                //resultFinal = resultFinal + "";
            } else if (c == notDisPoint) {
                resultFinal = resultFinal + "";
            } else {
                resultFinal = resultFinal + c;
                //resultFinal = resultFinal + c ;
            }
        }
        return resultFinal;
    }

    public boolean isStringEuro(String string1) {
        //todo first string für OcrGraphic.java
        String string = string1;
        String[] str = string.split("\\s+");
        if (str[0].equals("EUR")) {
            return true;
        } else {
            return false;
        }
    }

    public String longNumberShorter(Double input) {
        DecimalFormat dcF = new DecimalFormat("#.####");
        String string = dcF.format(input);
        return string;
    }

    /*public Double longStringToDouble(String input) {
        double resultD = 0;
        String resultS = input.toString();
        try {
            resultD = Double.parseDouble(resultS);

        } catch (Exception ignored) {}
        DecimalFormat dcF = new DecimalFormat("#.####");
        try {
            resultS = dcF.parse(resultS);
        } catch (ParseException ignored) {}
    }*/
}