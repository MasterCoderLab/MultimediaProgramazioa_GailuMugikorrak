package com.ikasle.jarduerenartekokomunikazioa;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Bigarren jarduera: lehenengo jardueratik jasotako izenarekin agurtzen du.
 */
public class BigarrenActivity extends AppCompatActivity {
    /**
     * Jasotako parametroa irakurri eta lehenengo TextView-an agurra erakusten du.
     * @param savedInstanceState aurreko egoera gordea
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigarren);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        String izena = getIntent().getStringExtra(MainActivity.EXTRA_IZENA);
        if (izena == null) izena = "";
        TextView agurra = findViewById(R.id.txtAgurra);
        agurra.setText(getString(R.string.agur_mezua, izena));
    }
}
