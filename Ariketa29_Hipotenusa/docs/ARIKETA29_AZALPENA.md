# Ariketa 29 - Hipotenusa kalkulatu

- **Ikasturtea:** 2026/27
- **Taldea:** 2AG3
- **Ikaslea:** Ion Beristain
- **Garapena:** Android Studio, Java eta XML

## Moodleko baldintzak

Triangelu angeluzuzen baten hipotenusa kalkulatu; datuak aurrez baliozkotu; errorea adierazi eta fokua zuzendu; bi metodo sortu:

- `double hipotenusa_kalkulatu(double b, double c)` (try-catch barne)
- `void eremuak_garbitu()`

Aplikazioaren mezu guztiak euskaraz daude. Irakasleak ariketa honetarako metodoen **sinadurak** eman ditu, ez haien kode osoa; izenak eta parametroak mantendu ditugu.

## Kalkuluaren diseinua

Hipotenusaren luzera Pitagorasen teoremaren bidez kalkulatzen da: `a = sqrt(b² + c²)`. Androiden `Math.hypot(b, c)` erabiltzen dugu, tarteko karratuek balio handietan sor ditzaketen gainezkatzeak saihesteko.

`hipotenusa_kalkulatu` metodoak `try-catch` dauka kalkuluaren aritmetika-errorea kontrolatzeko. Eremuetako kateak `double` bihurtzeko `try-catch(NumberFormatException)` ere badago, sarrera baliogabeak aplikazioa itxi ez dezan.

## Eremuak eta baliozkotzea

- Bi `EditText`: `c katetoa` eta `b katetoa`.
- `KALKULATU` eta `GARBITU`, irakaslearen irudian bezala.
- Kateto hutsak edo ez-numerikoak baztertu; dagokion eremuan `setError()` eta `requestFocus()` egin.
- Gehitutako baliozkotzea: luzera positibo eta finitua (0 edo negatiboa ez da onartzen).
- Koma eta puntua dituzten hamartarrak onartzen dira.
- `eremuak_garbitu()` metodoak bi katetoak, erroreak eta emaitza garbitzen ditu; fokua lehen eremura itzultzen du.

## Proba-kasu adierazgarriak

| b | c | Espero dena |
|---|---|---|
| 3 | 4 | 5 |
| 5 | 12 | 13 |
| 6 | 8 | 10 |
| 0,3 | 0,4 | 0,5 |
| 0 | 4 | Errorea b eremuan |
| -3 | 4 | Errorea b eremuan |
| hutsik | 4 | Errorea b eremuan |
| 3 | hutsik | Errorea c eremuan |
| abc | 4 | Errorea b eremuan |
| 1e308 | 1e308 | Emaitza oso handia, baina finitua |

Pantaila-argazkiak eta `09_Proben_Emaitzak.txt` ZIPeko lehen mailan daude; AndroidX probak `app/src/androidTest` direktorioan.
