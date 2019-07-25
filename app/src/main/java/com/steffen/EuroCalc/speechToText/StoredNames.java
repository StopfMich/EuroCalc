package com.steffen.EuroCalc.speechToText;

import java.util.Locale;

public class StoredNames {
    public String wordToSymbolDe(String input) {

        if (input.equals("Thailand") || input.equals("Thailändischer Baht")) {
            return "THB";
        }
        if (input.equals("Philippinen") || input.equals("Philippinischer Peso")) {
            return "PHP";
        }
        if (input.equals("Tschechien") || input.equals("Tschechische Kronen")) {
            return "CZK";
        }
        if (input.equals("Brasilien") || input.equals("Brasilianischer Real")) {
            return "BRL";
        }
        if (input.equals("Schweiz") || input.equals("Schweizer Franken")) {
            return "CHF";
        }
        if (input.equals("Indien") || input.equals("Indische Rupie")) {
            return "INR";
        }
        if (input.equals("Island") || input.equals("Isländische Krone")) {
            return "ISK";
        }
        if (input.equals("Kroatien") || input.equals("Kroatische Kuna")) {
            return "HRK";
        }
        if (input.equals("Bulgarien") || input.equals("Bulgarische Lew")) {
            return "BGN";
        }
        if (input.equals("Norwegen") || input.equals("Norwegische Kronen")) {
            return "NOK";
        }
        if (input.equals("USA") || input.equals("Amerika") || input.equals("US Dollar")) {
            return "USD";
        }
        if (input.equals("China") || input.equals("Chinesische Renminbi")) {
            return "CNY";
        }
        if (input.equals("Russland") || input.equals("Russischer Rubel")) {
            return "RUB";
        }
        if (input.equals("Schweden") || input.equals("Schwedische Krone")) {
            return "SEK";
        }
        if (input.equals("Malaysia") || input.equals("Malaysischer Ringgit")) {
            return "MYR";
        }
        if (input.equals("Singapur") || input.equals("Singapur Dollar")) {
            return "SGD";
        }
        if (input.equals("Israel") || input.equals("Schekel")) {
            return "ILS";
        }
        if (input.equals("Türkei") || input.equals("Türkische Lira")) {
            return "TRY";
        }
        if (input.equals("Polen") || input.equals("Polnische Złoty")) {
            return "PLN";
        }
        if (input.equals("Neuseeland") || input.equals("Neuseeländische Dollar")) {
            return "NZD";
        }
        if (input.equals("Hong Kong") || input.equals("Hong Kong Dollar")) {
            return "HKD";
        }
        if (input.equals("Rumänien") || input.equals("Rumänischer Leu")) {
            return "RON";
        }
        if (input.equals("Mexico") || input.equals("Mexikanischer Peso")) {
            return "MXN";
        }
        if (input.equals("Kanada") || input.equals("Kanadischer Dollar")) {
            return "CAD";
        }
        if (input.equals("Australien") || input.equals("Australischer Dollar")) {
            return "AUD";
        }
        if (input.equals("England") || input.equals("Groß Britanien") || input.equals("Vereinigtes Königreich") || input.equals("Pfund")) {
            return "GBP";
        }
        if (input.equals("Südkorea") || input.equals("Südkoreanischer Won")) {
            return "KRW";
        }
        if (input.equals("Südafrika") || input.equals("Afrika") || input.equals("Südafrikanischer Rand")) {
            return "ZAR";
        }
        if (input.equals("Japan") || input.equals("Japanischer Yen")) {
            return "JPY";
        }
        if (input.equals("Dänemarkt") || input.equals("Dänische Krone")) {
            return "DKK";
        }
        if (input.equals("Indonesien") || input.equals("Indonesische Rupiah")) {
            return "IDR";
        }
        if (input.equals("Ungarn") || input.equals("Forint") || input.equals("ungarische Forint")) {
            return "HUF";
        }
        return null;
    }

    public String wordToSymbolEn(String input) {

        if (input.equals("Thailand") || input.equals("Thai baht")) {
            return "THB";
        }
        if (input.equals("Philippines") || input.equals("Philippine peso")) {
            return "PHP";
        }
        if (input.equals("Czech Republic") || input.equals("Czech crowns") || input.equals("Czech")) {
            return "CZK";
        }
        if (input.equals("Brazil") || input.equals("Brazilian Real")) {
            return "BRL";
        }
        if (input.equals("Switzerland") || input.equals("Swiss franc")) {
            return "CHF";
        }
        if (input.equals("India") || input.equals("Indian Rupee")) {
            return "INR";
        }
        if (input.equals("Iceland") || input.equals("Icelandic crown")) {
            return "ISK";
        }
        if (input.equals("Croatia") || input.equals("Croatian Kuna")) {
            return "HRK";
        }
        if (input.equals("Bulgaria") || input.equals("Bulgarian lev")) {
            return "BGN";
        }
        if (input.equals("Norway") || input.equals("Norwegian crowns")) {
            return "NOK";
        }
        if (input.equals("USA") || input.equals("America") || input.equals("US Dollar")) {
            return "USD";
        }
        if (input.equals("China") || input.equals("Chinese renminbi")) {
            return "CNY";
        }
        if (input.equals("Russia") || input.equals("Russian rubles")) {
            return "RUB";
        }
        if (input.equals("Sweden") || input.equals("Swedish crown")) {
            return "SEK";
        }
        if (input.equals("Malaysia") || input.equals("Malaysian Ringgit")) {
            return "MYR";
        }
        if (input.equals("Singapore") || input.equals("Singapore dollars")) {
            return "SGD";
        }
        if (input.equals("Israel") || input.equals("Schekel")) {
            return "ILS";
        }
        if (input.equals("Turkey") || input.equals("Turkish lira")) {
            return "TRY";
        }
        if (input.equals("Poland") || input.equals("Polish złoty")) {
            return "PLN";
        }
        if (input.equals("New Zealand") || input.equals("New Zealand dollars")) {
            return "NZD";
        }
        if (input.equals("Hong Kong") || input.equals("Hong Kong Dollar")) {
            return "HKD";
        }
        if (input.equals("Romania") || input.equals("Romanian leu")) {
            return "RON";
        }
        if (input.equals("Mexico") || input.equals("Mexikanischer Peso")) {
            return "MXN";
        }
        if (input.equals("Canada") || input.equals("Canadian dollar")) {
            return "CAD";
        }
        if (input.equals("Australia") || input.equals("Australian dollar")) {
            return "AUD";
        }
        if (input.equals("England") || input.equals("Great Britain") || input.equals("United Kingdom") || input.equals("Pounds")) {
            return "GBP";
        }
        if (input.equals("South Korea") || input.equals("South Korean won")) {
            return "KRW";
        }
        if (input.equals("South Africa") || input.equals("Africa") || input.equals("South African Rand")) {
            return "ZAR";
        }
        if (input.equals("Japan") || input.equals("Japanese yen")) {
            return "JPY";
        }
        if (input.equals("Denmark") || input.equals("Danish crown")) {
            return "DKK";
        }
        if (input.equals("Indonesia") || input.equals("Indonesian rupiah")) {
            return "IDR";
        }
        if (input.equals("Hungary") || input.equals("Forint") || input.equals("ungarische Forint")) {
            return "HUF";
        }
        return null;
    }

}
