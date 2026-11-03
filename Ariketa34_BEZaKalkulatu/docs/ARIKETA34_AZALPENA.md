<!-- ARIKETA34_GIDOI_V1 -->
# Ariketa 34 — BEZa kalkulatu

## Antolaketa

- Proiektua: `Ariketa34_BEZaKalkulatu`.
- Aplikazioa: `AbiaraziJarduera`.
- Paketea: `com.ikasle.beza_kalkulatu`.
- Jarduerak: `ActProduktu_Produktua` eta `ActRegistroBEZ`.
- Layout-ak: `layout_produktua.xml` eta `layout_registro_bez.xml`.
- Ariketa 33ren Gradle/Java/XML egitura kopiatu da; jatorrizkoa ez da aldatu.

## PDFko bost eskakizunak

1. Produktuaren izena eta hasierako zenbatekoa sartzen dira.
2. `+BEZ` botoiak bigarren jarduera irekitzen du, `Intent` bidez datuak bidalita.
3. `4 %`, `10 %` edo `21 %` hautatzean guztizkoa kalkulatu eta bistaratzen da.
4. Izena eta zenbatekoa balioztatzen dira (hutsik, zenbaki baliogabea edo negatiboa).
5. Interfazea, Javadoc eta XML iruzkinak euskaraz daude.

## Erabaki teknikoak

- Aplikazioak bi Activity ditu. Ez da `ActivityResultLauncher` behar, emaitza ez baita lehenera itzultzen.
- Formula: `guztira = oinarria * (1 + tasa / 100.0)`.
- `double` eta `String.format` erabili dira, kodea erraza izan dadin.
- BEZa aukeratzeko `RadioGroup` eta hiru `RadioButton` erabili dira.
- Baliogabeko datuak `setError()` bidez jakinarazten dira.
- Java dokumentazioa Javadoc da eta XML komentarioen sintaxia `<!-- ... -->` da.

## Probak

`Ariketa34EntregaTest` proba instrumentatuak: hasierako pantaila,
bi jardueren komunikazioa, hiru BEZ tasak, produktu hutsa,
zenbateko hutsa eta zenbateko baliogabea. Kapturak eta proben
txostena ZIPean daude.
