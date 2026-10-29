<!-- ARIKETA31_GIDOI_V2 -->
# Ariketa 31 — Jardueren arteko komunikazioa 1

- Ikasturtea: 2026/27
- Taldea: 2AG3
- PDFko jatorrizko proiektu-izena: `ComunicacionActividades`
- Gure proiektuaren izena (euskaraz): `Ariketa31_JarduerenArtekoKomunikazioa`
- Paketea: `com.ikasle.jarduerenartekokomunikazioa`

## Moodle/PDFko 5 baldintzak

1. Proiektua euskaraz `Ariketa31_JarduerenArtekoKomunikazioa` izenarekin sortu da; PDFko izena `ComunicacionActividades` da.
2. Hasierako pantailan `Izena`, `EditText` eta `EGIAZTATU` botoia.
3. Botoia sakatzean `Intent`-en bidez bigarren jarduera abiarazten da; izena `putExtra`-rekin bidaltzen da.
4. Bigarren pantailak bere XML layout-a dauka: `activity_bigarren.xml`.
5. Bigarren jardueran, lehenengo agur-testua `Kaixo ` + jasotako izena da.

## Antolaketa eta erabakiak

- `MainActivity.java`: izena irakurri, `Intent` sortu eta parametroa bidali.
- `BigarrenActivity.java`: `getStringExtra` bidez hartu eta `TextView` batean erakutsi.
- `activity_main.xml` eta `activity_bigarren.xml`: PDFko bi pantailen antzeko antolaketa, euskaraz.
- Diseinu-erabakia (ez da PDFko eskakizun esplizitua): izen hutsa `setError`-ekin baliozkotu eta fokua izenean mantendu.
- Ez da aurreko ariketetako kodea berrerabili; Android Studio-n sortutako Java/XML proiektua erabili da.

## Froga automatizatuak

`app/src/androidTest/.../Ariketa31EntregaTest.java`-k egiaztatzen ditu hasierako eremuak, `Intent` bidezko komunikazioa, `Kaixo Ane` / `Kaixo Iker`, izen hutsaren errorea eta atzera egitea.

ZIPean pantaila-argazkiak eta `09_Proben_Emaitzak.txt` gehituko dira, probak ongi bukatu ondoren.
