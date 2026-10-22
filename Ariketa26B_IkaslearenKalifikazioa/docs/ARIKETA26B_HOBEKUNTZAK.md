# Ariketa 26 B - Hobekuntzak (irakaslearentzako azalpena)

- **Ikaslea:** Ion Beristain
- **Taldea:** 2AG3
- **Ikasturtea:** 2026/27
- **Ikasgaia:** Multimedia-programazioa eta gailu mugikorrak

## A eta B bertsioen arteko aldaketak

| Ezaugarria | 26 A (lehenengoa) | 26 B (hobekuntzak) |
|---|---|---|
| Emaitza | `AlertDialog` batean | Pantailan bertan, `TextView` iraunkorrean |
| ERAKUTSI | Beti gaituta | Baliozko kalkulu baten ondoren desgaituta |
| GARBITU | Eremuak hustu | Eremuak, emaitza, erroreak eta botoien egoera berrezarri |
| Baliozkotzea | `setError`, `try/catch`, koma eta puntua | Aurrekoa mantendu, emaitza blokeatu soilik baliozko kalkuluan |
| Muga inklusiboak | 10,5 / 14 / 18 hurrengo mailan | Irakaslearen hiru soluzioen `<=` irizpidea |
| Kode antolaketa | Metodo bereiziak | Metodo argiak eta Javadoc euskaraz mantendu |

## Noten sailkapena

| Tartea | Kalifikazioa |
|---|---|
| 0 <= n <= 10,5 | Txarra |
| 10,5 < n <= 14 | Erdipurdikoa |
| 14 < n <= 18 | Ona |
| 18 < n < 19 | Oso Ona |
| 19 <= n <= 20 | Bikaina |

**Diseinu-erabakia:** Enuntziatuak 18tik 19ra arteko tartea zehazten ez duenez, `Oso Ona` tarteko maila proposatu dugu. Ez da irakaslearen testutik zuzenean ateratako eskakizuna. Irakasleak emandako hiru Java adibideek, ordea, 10,5, 14 eta 18 mugak aurreko mailan uzten dituzte.

## Egiaztapenak eta entregako frogak

- `:app:assembleDebug`, `:app:testDebugUnitTest`, `:app:connectedDebugAndroidTest`.
- AndroidX instrumentazio probak: kalifikazioak, muga-balioak, eremu hutsak, gaizki idatzitako notak, ERAKUTSI desgaitzea eta GARBITUren berrezarpena.
- `01_` - `08_`: pantailako egoera errealen PNG kapturak.
- `09_Proben_Emaitzak.txt`: automatikoki egiaztatutako kasuen laburpena.
- ZIP osotasuna `unzip -t` erabiliz egiaztatua.

**Oharra:** A bertsioa bereizita gordetzen da; 26 B proiektu eta entrega berri bat da.
