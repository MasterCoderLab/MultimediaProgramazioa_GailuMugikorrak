package com.ikasle.jarduerenartekokomunikazioa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Lehen jarduera: izena hartu eta bigarren jarduerara Intent bidez bidaltzen du.
 */
public class MainActivity extends AppCompatActivity {
    /** Bigarren jarduerari bidaltzen zaion parametroaren gakoa. */
    public static final String EXTRA_IZENA = "com.ikasle.jarduerenartekokomunikazioa.IZENA";

    /**
     * Hasierako pantaila prestatzen du eta EGIAZTATU botoia konfiguratzen du.
     * @param savedInstanceState aurreko egoera gordea
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        EditText izena = findViewById(R.id.edtIzena);
        findViewById(R.id.btnEgiaztatu).setOnClickListener(v -> {
            String sartutakoIzena = izena.getText().toString();
            // Eremu hutsak ez dira bigarren jarduerara bidaltzen.
            if (sartutakoIzena.trim().isEmpty()) {
                izena.setError(getString(R.string.errorea_izena));
                izena.requestFocus();
                return;
            }
            Intent intent = new Intent(MainActivity.this, BigarrenActivity.class);
            intent.putExtra(EXTRA_IZENA, sartutakoIzena);
            startActivity(intent);
        });
    }
}
