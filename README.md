# MainActivity
## onCreate
> onCreate ist wie die Main in einem normalen Java-Programm, sie wird als erstes ausgeführt.
>
> (54-63) als erstes werden die Textfelder, Buttons usw. in die MainActivity initialisiert.
>
> (67-69) der context (in welcher activity man sich befindet) wird danach für die anderen Klassen übergeben, 
>         damit diese Auch das UI verändern können.
>
> (72-84) überprüft, ob es der erste Start der App ist, da sonst eine Datenbank vorhanden wäre.
>
> (87-103) ist es der erste Start, wird ein callback von Volley (mehr unter [NetHelper](#NetHelper)) versucht, welcher ein JSON-String zurückgibt.
> Wenn Callback erfolgreich, versuche es zu speichern. Die NullPointerException wird abgefangen. Wenn noch keine DB exextiert, wird dieser Fehler ausgelöst und es wird daraufhin eine erstellt.
> Zum DB Framework steht mehr unter [SqlDbClass](#SqlDbClass)
>
> (105-116) onError wird ausgeführt, wenn die verbindung nicht erfolgreich war. Es wird versucht die Daten aus der Db zu bekommen, wenn diese vorhanden sind, wird mit startAfterVolley alles danach ausgeführt, wenn nicht, wird einfach nur der retry-Button angezeigt.
> (118-126) wenn es nicht der erste Start ist, wird [Volley](##NetHelper) nicht ausgeführt um die App schneller starten zu lassen (es wird nicht auf ein Callback gewartet). Wenn nichts klappt, hört er auf zu laden und zeigt den retry-Button
>
## startAfterVolley
> Dies wird ausgeführt, wenn Daten aus [onCreate](#oncreate) übergeben worden sind.
> (130-133) Der Jsons-String wird ins [DataWarehouse](#datawarehouse) übergeben und dort aufgeteilt. lastUpdate bezieht sich nicht auf den Zeitpunkt des Volley-Callbacks sondern, wann die Daten online gestellt wurden. Zu pupolateDropdown findet man unter [CustomAdapter](#customAdapter) mehr.
>
> Der Rest von startAfterVolley ist im Source-Code mit kommentaren detaliert beschrieben. 
>```Java
>              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //Wenn ein Land ausgewählt wird
>                try { //Es wird das ausgewählte Land gespeichert. In der Regel besucht man nur ein Land -> es soll nicht immer die Währung gewechselt werden
>                    sql = sql.getSQL();
>                    sql.setLastRightId(currencyText.getSelectedItemPosition());
>                    sql.save();
>                } catch (Exception e) {
>                    errorMsg.errorToast("Error at: 103-113", true); //Debug, würde aber einen Crash verhindern
>                }
>
>                data.setCurrentCurrency(currencyText.getSelectedItem().toString()); //DataWarehouse.currentCurrency bekommt die abkürzung des ausgewählten Landes
>                data.setCurrentCurrencyValue(data.getCurrencyValue(data.getCurrentCurrency())); //Setzt den Umrechnungswert
>                int img = logic.setImage(data.getCurrentCurrency()); //bininhaltet die id des Flaggen-Bildes
>                if (img != 0) { //Wenn Bild vorhanden ist, setzte das bild
>                    currencyText.setBackgroundResource(img);
>                    valueCurrency.setText(logic.calculateOtherCurrency( //rechnet den Wert vom Feld euroValueView in die ausgewählte währung um und zeigt den Wert an
>                            euroValueView.getText().toString(), data.getCurrentCurrencyValue().toString())
>                    );
>                }
>            }
>```
> usw.

# history
## onCreate History
> (37-51) Setzt wieder context und gibt android vor, es in den Landscape modus zu wechseln und die Navigationbar auszublenden.
> (55-58) Übergibt [DataWarehouseSmall](#datawarehousesmall) die Daten, die über Inten mitgeschickt wurden.
> (64-84) Die Url zur abfrage wird durch den (vom Benutzer angegebenen) Zeitraum erstellt und es wird wieder eine Volly abfrage ausgeführt. Die Daten werden ins [DataWarehouseSmall](#datawarehousesmall) übergeben und dort gespeichert.
> (85-93) Baut das Overlay (über den Graphen).
## buildGraph
> [GraphView Doku](https://github.com/jjoe64/GraphView).
> (103-111) Baut den Graphen im GraphView Feld.
> (112-119) Einstellungen für de nGraphen, wie: Kann scrollen, kann zoomen, min & max Punkte pro x.
> (123-125) Beschriftung der Achsen (Können nur die vergangenen Tage angeben, ein Daten haben sonst falsche reihenfolge).
> (128-135) Design des Graphen wird festgelegt -> [Doku](https://github.com/jjoe64/GraphView/wiki/Style-options).
> (136-148) Wenn auf einen Punkt auf dem Graphen gedrückt wird, bekommt man den Wert und das Datum oben Angezeigt.
> (150) Die Punkte werden auf den Graphen "eingezeichnet".
# DataWarehouseSmall
> Ist wie DataWarehouse welcher in der [MainActivity](#aainactivity), nur mit weniger Daten, da (History)[#history] nur den Verlauf einer Währung anzeigen soll.
