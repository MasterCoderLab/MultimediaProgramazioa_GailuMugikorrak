<!-- ARIKETA32_GIDOI_V1 -->
# Ariketa 32 — Jardueren arteko komunikazioa 2

## Proiektuaren antolaketa

- Proiektua: `Ariketa32_JarduerenArtekoKomunikazioa2`
- Paketea: `com.ikasle.jarduerenartekokomunikazioa2`
- Oinarria: Ariketa 31ren iturburuko Java/XML egitura. Ariketa 31 ez da aldatu.
- Bi jarduera: `MainActivity` eta `BigarrenActivity`.

## PDFko 5 baldintzak

1. Aurreko ariketaren egitura hartu eta jardueren arteko komunikazioa zabaldu da.
2. Bigarren jardueran, izenarekin `Kaixo <izena>. Baldintzak onartzen dituzu?` agertzen da.
3. `ONARTU` botoiak `Onartua` itzultzen du, eta `BAZTERTU` botoiak `Baztertua`. Bi kasuetan jarduera ixten da.
4. Jarduera nagusiko azken `TextView`-ak `Emaitza: Onartua` edo `Emaitza: Baztertua` erakusten du.
5. Erabiltzaile-interfazea, Java Javadoc eta XML iruzkinak euskaraz daude.

## Egindako erabaki teknikoak

- `Intent.putExtra()` izena bidaltzeko eta erantzuna itzultzeko erabiltzen da.
- `setResult(RESULT_OK, intent)` eta `finish()` erabiltzen dira bigarren jardueratik itzultzeko.
- `ActivityResultLauncher` erabiltzen da `onActivityResult()` zaharkituaren ordez.
- Erabiltzaileari erakutsitako testuak `strings.xml` fitxategian zentralizatuta daude.
- Diseinu-erabakia (ez da PDFko baldintza): izen hutsa `setError()` bidez egiaztatzen da.

## Proba automatizatuak

`Ariketa32EntregaTest`-ek izenaren bidalketa, bi aukerak, emaitzen itzulera,
izen hutsaren errorea eta jardueren arteko nabigazioa egiaztatzen ditu.

Kapturak eta `09_Proben_Emaitzak.txt` ZIPean gehitzen dira proba guztiak gainditu ondoren.
