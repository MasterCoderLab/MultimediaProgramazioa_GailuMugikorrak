package com.ikasle.jarduerenartekokomunikazioa2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Bigarren jarduera: erabiltzaileari baldintzak galdetzen dizkio eta erantzuna itzultzen du.
 */
public class BigarrenActivity extends AppCompatActivity {

    /**
     * Jasotako izena erakusten du eta ONARTU/BAZTERTU botoiak prestatzen ditu.
     * @param savedInstanceState aurreko egoera gordea
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigarren);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        String izena = getIntent().getStringExtra(MainActivity.EXTRA_IZENA);
        if (izena == null) izena = "";

        TextView galdera = findViewById(R.id.txtGaldera);
        galdera.setText(getString(R.string.baldintzen_galdera, izena));

        findViewById(R.id.btnOnartu).setOnClickListener(
                v -> erantzunaItzuli(getString(R.string.onartua)));
        findViewById(R.id.btnBaztertu).setOnClickListener(
                v -> erantzunaItzuli(getString(R.string.baztertua)));
    }

    /**
     * Hautatutako erantzuna jarduera nagusira bidali eta pantaila ixten du.
     * @param erantzuna erabiltzaileak hautatutako aukera
     */
    private void erantzunaItzuli(String erantzuna) {
        Intent datuak = new Intent();
        datuak.putExtra(MainActivity.EXTRA_ERANTZUNA, erantzuna);
        setResult(RESULT_OK, datuak);
        finish();
    }
}
