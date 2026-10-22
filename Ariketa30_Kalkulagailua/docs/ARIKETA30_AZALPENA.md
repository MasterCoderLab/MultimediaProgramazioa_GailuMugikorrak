# Ariketa 30 — RadioGroup bidezko kalkulagailua

- **Ikaslea:** Ion Beristain
- **Taldea:** 2AG3
- **Ikasturtea:** 2026/27
- **Ikasgaia:** Multimedia-programazioa eta gailu mugikorrak

## Eskatutako baldintzak (Moodle)

Bi **zenbaki natural**, zero barne, sartu eta RadioGroup bidez lau eragiketetako bat aukeratu: batuketa, kenketa, biderketa edo zatiketa. Eragiketa aukeratu eta KALKULATU sakatzean emaitza agertzen da. Eragiketa bakoitzerako parametroak dituzten metodoak erabili dira. Aplikazioa euskaraz dago.

## Irakaslearen kodearekiko errespetua

`Calculadora con radiogroup.java` fitxategiko lau **metodo matematikoak bere horretan** mantendu dira (izena, `double` parametroak, itzulera eta gorputza):

- `public double sumar(double a,double b)`
- `public double restar(double a,double b)`
- `public double multiplicar(double a,double b)`
- `public double dividir(double a,double b)`

Euskara erabiltzen da interfazearen eta erabiltzailearentzako mezuetan. Kalkulatzeko botoiak hautatutako RadioButton-a irakurtzen du eta dagokion metodo originala deitzen du.

## Baliozkotzeak eta diseinu-erabakiak

- **0 onartzen da** sarrera gisa, Moodleko baldintzari jarraituz; `0+5`, `5-0` eta `0*5` zuzen kalkulatzen dira.
- **Zatiketan soilik** ez da bigarren zenbakia zero izatea onartzen (`8/0`), emaitza matematikoki definitu gabe dagoelako.
- Bi sarrerak hutsik ez daudela eta zenbaki oso ez-negatiboak direla egiaztatzen da. Zenbakiak Java-ko `long` tartean interpretatzen dira, eta irakaslearen `double` metodoetara pasatzen dira; oso zenbaki handiek `double`-ren doitasun-mugak dituzte.
- Eragiketarik hautatu gabe sakatzen bada, abisu bat agertzen da. **RadioGroup-ek hautaketa bakarra** onartzen du.
- GARBITU botoiak bi sarrerak, aukeratutako eragiketa, akatsak eta emaitza garbitu eta fokua lehen zenbakian uzten du.
- Pantailako emaitza `TextView` batean agertzen da. Moodleko irudiaren estilo urdina mantentzen da, baina elementuak euskaraz daude.

## Proba automatizatuak eta frogak

`app/src/androidTest/.../Ariketa30EntregaTest.java` probak Android emuladorean exekutatzen dira, Gboard erabili gabe. Lau metodoak, lau eragiketak, 0 kasuak, zatiketa zeroz, hutsuneak, balio okerrak, RadioGroup eta garbiketa frogatzen dira.

ZIPeko 01–08 PNG fitxategiek egoera nagusiak erakusten dituzte; `09_Proben_Emaitzak.txt` txostenak probak jasotzen ditu.
