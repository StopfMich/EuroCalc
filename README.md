# **Hauptverzeichniss**

## MainActivity

#### onCreate

>onCreate ist wie die Main in einem normalen Java-Programm, sie wird als erstes ausgeführt.
>
>(54-63) als erstes werden die Textfelder, Buttons usw. in die MainActivity initialisiert.
>
>(67-69) der context (in welcher activity man sich befindet) wird danach für die anderen Klassen übergeben, damit diese Auch das UI verändern können.
>
>(72-84) überprüft, ob es der erste Start der App ist, da sonst eine Datenbank vorhanden wäre.
>
>(87-103) ist es der erste Start, wird ein callback von Volley (mehr unter [NetHelper](#NetHelper)) versucht, welcher ein JSON-String zurückgibt. Wenn Callback erfolgreich, versuche es zu speichern. Die NullPointerException wird abgefangen. Wenn noch keine DB exextiert, wird dieser Fehler ausgelöst und es wird daraufhin eine erstellt. Zum DB Framework steht mehr unter [SqlDbClass](#SqlDbClass)
>
>(105-116) onError wird ausgeführt, wenn die Verbindung nicht erfolgreich war. Es wird versucht die Daten aus der DB zu bekommen, wenn diese vorhanden sind, wird mit startAfterVolley alles danach ausgeführt, wenn nicht, wird einfach nur der retry-Button angezeigt. (118-126) wenn es nicht der erste Start ist, wird [Volley](#NetHelper) nicht ausgeführt um die App schneller starten zu lassen (es wird nicht auf ein Callback gewartet). Wenn nichts klappt, hört er auf zu laden und zeigt den retry-Button 

#### startAfterVolley

>Dies wird ausgeführt, wenn Daten aus [onCreate](#oncreate) übergeben worden sind. (130-133) Der Jsons-String wird ins [DataWarehouse](#datawarehouse) übergeben und dort aufgeteilt. lastUpdate bezieht sich nicht auf den Zeitpunkt des Volley-Callbacks sondern, wann die Daten online gestellt wurden. Zu pupolateDropdown findet man unter [CustomAdapter](#customAdapter) mehr.

>Der Rest von startAfterVolley ist im Source-Code mit Kommentaren detailliert beschrieben.
>
```Java
public void onItemSelected(AdapterView\&lt;?\&gt; parent, View view, int position, long id) { //Wenn ein Land ausgewählt wird

try { //Es wird das ausgewählte Land gespeichert. In der Regel besucht man nur ein Land -\&gt; es soll nicht immer die Währung gewechselt werden

sql = sql.getSQL();

sql.setLastRightId(currencyText.getSelectedItemPosition());

sql.save();

} catch (Exception e) {

errorMsg.errorToast(&quot;Error at: 103-113&quot;, true); //Debug, würde aber einen Crash verhindern

}

data.setCurrentCurrency(currencyText.getSelectedItem().toString()); //DataWarehouse.currentCurrency bekommt die abkürzung des ausgewählten Landes

data.setCurrentCurrencyValue(data.getCurrencyValue(data.getCurrentCurrency())); //Setzt den Umrechnungswert

int img = logic.setImage(data.getCurrentCurrency()); //bininhaltet die id des Flaggen-Bildes

if (img !=0) { //Wenn Bild vorhanden ist, setzte das bild

currencyText.setBackgroundResource(img);

valueCurrency.setText(logic.calculateOtherCurrency( //rechnet den Wert vom Feld euroValueView in die ausgewählte währung um und zeigt den Wert an

euroValueView.getText().toString(), data.getCurrentCurrencyValue().toString())

);

}

}
```
>usw.

## CustomAdapter

Der CustomAdapter bezieht sich auf das Menü, welches beim Druck auf die Rechte Flagge erscheint. Da es in Android kein Vordefiniertes Dropdown &amp; list Menü gibt, musste dies über einen CustomAdapter gemacht werden. Eine Doku findest sich [hier](https://developer.android.com/reference/android/widget/SpinnerAdapter) Die @Override sind Funktionen, bei AndroidStudio miterstellt werden.

#### getView

>Durch die ausgewählte Position wird das richtige Land rausgesucht und die Abkürzung im spinner\_selected main gesetzt z.B.(de, au).

#### getDropDownView

>Setzt die Flaggen als &quot;Hintergrund&quot; für spinner\_item\_layout flaggeSpinner. Befüllt wir der Spinner in der AminActivity Zeile 222.

#### getItemByString

>Diese Funktion wird dafür benutzt, über einen String das Item im Spinner zu finden und diese als int zurück zu geben. Diese Funktion wird bei der Sprachsteuerung verwendet, um das richtige Item (Land) auszuwählen.

## DataWarehouse

> Speichert den JsonString aus der API und teilt ihn auf. Besteht nur aus Daten und soll den JsonString und brauchbare Typen umwandeln.

## ErrorClass

>Es nur dafür da, eine Fehlermeldung anzuzeigen mit einem übergebenen String und einem Wahrheitswert (kurz oder lange anzeigen).

# Logic

>Diese Klasse unterteilt sich nochmal in 3 Teile auf
>
> 1: [Main-Logic](#main-logic) (22-226) Viele (aber nicht nur) Funktionen für die [MainActivity](#mainactivity).
>
>2: [History-Logic](#history-logic) (233-278) Funktionen für [History](history-verzeichniss).
>
>3: [Ocr-Logic](ocr-logic) (284-398) Funktionen für [OcrReader](#ocrreader-verzeichniss).

## Main Logic

#### init

>Wird bei [onCreate](#oncreate), bei [History](#history) und bei [DataWarehouse](#datawarehouse) benutzt. Es gibt die View an, welche die Logic verändern kann und setzt auch den Context für die [ErrorClasse](#errorclasse) und kann direkt Fehlermeldungen Anzeigen.

#### round

>Hiermit lässt sich die Zahl auf 2 Nachkommerstellen richtig runden. Keine Währung benutzt 6 Nachkommerstellen.

#### convertDate

>Wandelt das Datum yyyy-mm-dd in dd-mm-yyyy um.

#### setImage

>Bekommt einen String mit der Abkürzung des Landes. Wenn das Land (z.B. CZK) ist, wird die Bild ID (im Form eines int) zurückgegeben.

#### calculateOtherCurrency

>Bei Eingabe ins Euro-Feld wird diese Funktion aufgerufen. Sie rechnet den Wert für das andere Feld aus. Die Funktionen sind auch dafür verantwortlich, bei einem leeren Feld oder falscher Eingabe (Punkt) nicht abzustürzen.

#### calculateToEuro

>Wie [calculateOtherCurrency](#calculateothercurrency), nur halt von der fremden Währung zu Euro. Die Funktionen ist auch dafür verantwortlich, bei einem leerem Feld oder falscher Eingabe (Punkt) nicht abzustürzen.

#### getFlaggsArray

>Gibt ein Array mit den Flaggen-id`s zurück

#### timeToUpdate

>Vergliecht den jetzigen Zeitpunkot mit dem im Json-String übergebenen Datum. Wenn es nicht Samstag oder Sonntag ist und nach 18:00 uhr, wird der Update-Knopf sichtbar.

## History Logic

#### createUrl

>Erstellt eine URL aus den übertragenen Daten aus der [MainActivity](#mainactivity). Die Funtkion gibt den String für [Volley](#nethelper) zurück.

#### stringToJsonObj

>Die API gibt einen JSON-String zurück, dieser wird in ein JSON-Objekt umgewandelt

#### getCurrencyKeys

>Wandelt daten aus dem JSON-Objekt in ein String-Array um und gibt dieses Array zurück.

#### convertDataHistory

>Wie [convertDate](#convertdate), nur umgekehrt. Also von dd-mm-yyyy zu yyyy-mm-dd.

## Ocr Logic

#### createCamString

>verändert die Anzeige unten im ocr\_capture.xml. Also von welcher Währung in welche gerade umgerechnet wird.

#### tryToTransformInput

>Versucht das erkannte zu filtern und in den String nur noch Zahlen zu lassen.

#### transformInputERRORhandling & sortDot

>Falls in [tryToTransformInput](#trytotransforminput) in bestimmten Stelle ein Fehler auftritt ( Da Amerikaner die Komma oder Punkt Setzung anders machen als die meisten Länder, ist das ein Algorithmus nötig um zu gucken, welche Punkt/Komma Setzung hier verwendet wird.), wird diese Funktion gerufen. Wir haben die sortDot Funktion ausgelagert, da die tryToTransformInput Funktion sonst noch unübersichtlicher geworden wäre.

#### longNumberShorter

>Kürzt den String und verhindert unnötig lange Nachkommerstellen. Diese Funktion wird in [tryToTransformInput](#trytotransforminput) gerufen.

## NetHelper

>Der NetHelper benutzt das Framework Volley um die Daten online von der EZB zu bekommen. Wir haben uns für Volley entschieden, da Volley nach einer definierten Zeit die Anfragen automatisch abbricht. Beim onSuccess Callback sind Daten Garantiert und erleichtert so das Fehlerabfangen.
>
>Die Dokumentation über Volley ist [hier](https://developer.android.com/training/volley) zu finden.

# History Verzeichniss

## History

#### onCreate History

>(37-51) Setzt wieder context und gibt android vor, es in den Landscape modus zu wechseln und die Navigationbar auszublenden.
>
>(55-58) Übergibt [DataWarehouseSmall](#datawarehousesmall) die Daten, die über Inten mitgeschickt wurden.
>
>(64-84) Die URL zur Abfrage wird durch den (vom Benutzer angegebenen) Zeitraum erstellt und es wird wieder eine Volley Abfrage ausgeführt. Die Daten werden ins [DataWarehouseSmall](#datawarehousesmall) übergeben und dort gespeichert.
>
>(85-93) Baut das Overlay (über den Graphen).

#### buildGraph

>[GraphView Doku](https://github.com/jjoe64/GraphView).
>
>(103-111) Baut den Graphen im GraphView Feld.
>
>(112-119) Einstellungen für de nGraphen, wie: Kann scrollen, kann zoomen, min &amp; max Punkte pro x.
>
>(123-125) Beschriftung der Achsen (Können nur die vergangenen Tage angeben, ein Daten haben sonst falsche Reihenfolge).
>
>(128-135) Design des Graphen wird festgelegt -\&gt; [Doku](https://github.com/jjoe64/GraphView/wiki/Style-options).
>
>(136-148) Wenn auf einen Punkt auf dem Graphen gedrückt wird, bekommt man den Wert und das Datum oben Angezeigt.
>
>(150) Die Punkte werden auf den Graphen &quot;eingezeichnet&quot;.

## DataWarehouseSmall

>Ist wie [DataWarehouse](#datawarehouse) welcher in der [Hauptverzeichniss](#hauptverzeichniss) ist, nur mit weniger Daten, da [History](#history) nur den Verlauf einer Währung anzeigen soll.

# Ocrreader Verzeichniss

>Der OCR Reader ist ein Framework Namens Mobile Vision und ist ein alte Projekt, welches nun von cloud.google abgelöst wurde. Mobile Vision ist aber im Gegensatz zur Cloud-Lösung kostenlos. Im Folgenden werden nur die wichtigen, geänderten Sachen erwähnt. Eine Dokumentation über Mobile Vision findet sich [hier](https://developers.google.com/vision/introduction).

## OrcDetectorProcessor Datei

#### OrcDetectorProcessor

>Ist der Konstruktor und wird beim Start einmal ausgeführt
>
>(47-60) Dies ist für das richtige setzten im UI (Euro zu ...), im unteren Rande. Es setzt auch einen Listener, welcher immer ausgeführt wird, wenn anders herum umgerechnet werden soll.

#### setText

>Setzt den Text unten mit dem String aus der logic [createCamString](#createcamstring).

## OrcGraphic

>Diese Datei ist für das Zeichnen der Box zuständig. Hier werden auch über die Logic direkt die Änderungen der Anzeige vorgenommen. So steht in der Box nicht der alte Wert, sondern der neue umgerechnete Wert.

#### draw

>Die Funktion Zeichnet die Box und den inneren Text.
>
>Die Änderungen können von Zeile 119 bis 127 beobachtet werden. Hier sieht man auch die Aufrufe zur [Ocr-Logic](#ocr-logic) und dessen Funktionen.

# StoredNames

>Diese Datei enthält zwei Switch-Case Abfragen, welche Ländern (welche durch ein String übergeben werden), ihre Abkürzung zuweist.
>
>Diese Datei wird größtenteils von der Sprachsteuerung verwendet, um nachher das passende Land auszuwählen.

# SqlDbClass

>Die Datenbank wird mit dem [Sugar-Framework](https://satyan.github.io/sugar/index.html) erstellt, da wir nur 4 Werte speichern mussten.
>
>Dies wären Folgendes:
>
>1. jsString: Der letzte String, den man aus der [Volley](#nethelper) Abfrage bekommen hat
>
>2. lastUpdate: Wann das letzte Mal der Datensatz geupdatet wurde
>
>3. lastRightId: Speichert das zuletzt ausgewählte Land
>
>4. firstStartup: Wurde das Programm schonmal ausgeführt? Wichtig für den Start [onCreate](#oncreate).
