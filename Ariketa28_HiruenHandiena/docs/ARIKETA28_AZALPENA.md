# Ariketa 28 - Hiru zenbakiren arteko handiena

- **Ikasturtea:** 2026/27
- **Taldea:** 2AG3
- **Ikaslea:** Ion Beristain
- **Garapena:** Android Studio, Java eta XML

## Moodleko eskakizunak

Hiru zenbakiren artean handiena kalkulatu, `hiruen_handiena` izeneko metodoa sortu eta interfazea euskaratu.

## Irakaslearen kodea errespetatzea

`Mayor3 con metodo.java` fitxategiko `private long mayor_tres(long num1, long num2, long num3)` metodoa **bere horretan** kopiatu da, instrukziorik edo izenik aldatu gabe.

Moodleko izena betetzeko `long hiruen_handiena(long num1, long num2, long num3)` metodo gehigarria sortu da; honek irakaslearen `mayor_tres` metodoa deitzen du. Metodo originala ez da ordezkatu.

**Erabakia:** lehen erreferentziako metodoak `long` darabilenez, hiru **zenbaki oso** sartzen dira. Ez dugu `double` bihurtu, irakaslearen metodoaren sinadura mantentzeko. Bigarren Java eta PDFa soluzio alternatiboak dira, ez dira nahastu.

## Interfazea eta baliozkotzea

- Hiru `EditText`, ERAKUTSI botoia eta `TextView` emaitzarako, euskaraz.
- Eremu hutsak, baliogabeko testuak, hamartarrak eta `long` mugak gainditzen dituzten sarrerak `setError()` bidez adierazten dira.
- Sarrera aldatzen denean, aurreko emaitza ezkutatzen da.

## Proba-kasuak

| Sarrerak | Espero den emaitza |
|---|---|
| 9, 4, 2 | 9 (lehenengoa) |
| 4, 9, 2 | 9 (bigarrena) |
| 4, 2, 9 | 9 (hirugarrena) |
| 7, 7, 3 | 7 (berdinketa) |
| 5, 5, 5 | 5 (berdinketa hirukoitza) |
| -3, -8, -1 | -1 (negatiboak) |
| 9223372036854775807, 0, -1 | 9223372036854775807 (long max) |
| 1, 2, 3.5 | Errorea (ez da zenbaki osoa) |
| 1, hutsik, 3 | Errorea |

Kapturak eta `09_Proben_Emaitzak.txt` fitxategia ZIPeko lehen karpetan daude. `app/src/androidTest` barruan Androiden proba automatizatuak gordetzen dira.
