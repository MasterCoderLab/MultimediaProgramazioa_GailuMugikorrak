# Ariketa 27 - Urte bisurtea

- **Ikasturtea:** 2026/27
- **Taldea:** 2AG3
- **Ikaslea:** Ion Beristain
- **Teknologia:** Android Studio, Java eta XML

## Ariketaren eskakizunak

Urte bat bisurtea den zehaztea. Bisurtea da 4z zatigarria eta 100ez zatigarria ez bada, edo 400ez zatigarria bada.

Eskatutako metodoa: `boolean bisurtea_da(int urtea)`.

```java
return (urtea % 4 == 0 && urtea % 100 != 0) || urtea % 400 == 0;
```

## Interfazea eta portaera

- Interfazea euskaraz: Urte bisurtea, Urtea eta EGIAZTATU.
- Urtea idatzi eta EGIAZTATU sakatuta, emaitza pantailan erakusten da.
- Emaitza aurreko urtea aldatzean ezabatzen da, zaharkituta geratu ez dadin.
- **Diseinu-erabaki gehigarria:** urtea hutsik, zenbakizkoa ez bada, 0 bada edo negatiboa bada, eremuan errorea erakusten da. Zenbaki osoaren mugak gainditzen dituen balioa ere baztertzen da.

## Adibideak eta proba automatizatuak

| Urtea | Emaitza | Zergatik |
|---|---|---|
| 2024 | Bisurtea | 4z zatigarria, ez 100ez |
| 2025 | Ez da bisurtea | Ez da 4z zatigarria |
| 1900 | Ez da bisurtea | 100ez zatigarria, ez 400ez |
| 2000 | Bisurtea | 400ez zatigarria |
| 2100 | Ez da bisurtea | 100ez bai, 400ez ez |
| 2400 | Bisurtea | 400ez zatigarria |

Pantaila-argazkiak eta proben emaitzak ZIParen goiko karpetan daude. `app/src/androidTest` barruan proba instrumentatuak daude, eta `app/src/main` barruan aplikazioaren kodea.
