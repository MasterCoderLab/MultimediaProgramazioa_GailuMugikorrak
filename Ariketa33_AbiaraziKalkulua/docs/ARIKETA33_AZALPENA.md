<!-- ARIKETA33_GIDOI_V1 -->
# Ariketa 33 — Abiarazi kalkulua

## Antolaketa

- Proiektua: `Ariketa33_AbiaraziKalkulua`.
- Paketea: `com.ikasle.abiarazi_kalkulua`.
- Aplikazioa: `Kalkulua`.
- Jarduerak: `ActiKalkulu1` eta `ActKalkulu2`.
- Layout-ak: `layoutkalkulu1.xml` eta `layoutkalkulu2.xml`.
- Ariketa 32ren Gradle/Java/XML egitura erabili da oinarri gisa, jatorrizkoa aldatu gabe.

## PDFko baldintzak

1. Lehen jardueran izena idazteko eremua, `KALKULATU` eta `IRTEN` botoiak daude.
2. Izenik idatzi ezean, errorea erakusten da eta ez da bigarren jarduera irekitzen.
3. Bigarren jardueran zenbakiaren bikoitza kalkulatzen da; hutsik badago errorea erakusten da.
4. Lehen jarduerako izena `Intent.putExtra()` erabiliz bidaltzen eta bigarrenean erakusten da.
5. Pantaila, Java dokumentazioa eta XML iruzkinak euskaraz daude.

## Erabaki teknikoak

- Kalkulua `BigDecimal` erabiliz egiten da, zenbaki oso eta hamartarrak onartzeko.
- Zenbakirik ez badago, `setError()` erabiliz jakinarazten da.
- `IRTEN` botoiak lehen jarduera ixten du (`finish()`).
- Testu guztiak `strings.xml` fitxategian bildu dira.
- Java klaseek Javadoc laburra dute eta XML fitxategiek iruzkin laburrak.

## Probak

`Ariketa33EntregaTest` probak: hasierako pantaila, izen hutsa, izenaren
bidalketa, zenbaki hutsa, bikoizketa osoa eta hamartarra, eta atzera egitea.
Kapturak eta `10_Proben_Emaitzak.txt` ZIPean gordetzen dira.
