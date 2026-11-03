package com.ikasle.beza_kalkulatu;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

/**
 * Produktuari %4, %10 edo %21eko BEZa aplikatu eta guztizkoa erakusten du.
 */
public class ActRegistroBEZ extends AppCompatActivity {

    /**
     * BEZ aukerak prestatzen ditu eta aukeratutako tasarekin guztizkoa kalkulatzen du.
     *
     * @param savedInstanceState aurreko egoera gordea
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_registro_bez);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        String izena = getIntent().getStringExtra(ActProduktu_Produktua.EXTRA_PRODUKTUA);
        double oinarria = getIntent().getDoubleExtra(ActProduktu_Produktua.EXTRA_ZENBATEKOA, 0);
        TextView produktua = findViewById(R.id.txtProduktua);
        TextView emaitza = findViewById(R.id.txtGuztira);
        produktua.setText(getString(R.string.produktua_mezua, izena == null ? "" : izena));

        RadioGroup aukerak = findViewById(R.id.rgBEZ);
        aukerak.setOnCheckedChangeListener((taldea, hautatutakoa) -> {
            int tasa;
            if (hautatutakoa == R.id.rb4) {
                tasa = 4;
            } else if (hautatutakoa == R.id.rb10) {
                tasa = 10;
            } else if (hautatutakoa == R.id.rb21) {
                tasa = 21;
            } else {
                return;
            }

            double guztira = oinarria * (1 + tasa / 100.0);
            String zenbatekoa = String.format(Locale.forLanguageTag("eu-ES"), "%.2f", guztira);
            emaitza.setText(getString(R.string.guztira_mezua, zenbatekoa));
        });
    }
}
