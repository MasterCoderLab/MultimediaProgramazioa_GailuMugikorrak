package com.ikasle.abiarazi_kalkulua;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Lehen jarduera: erabiltzailearen izena egiaztatu eta kalkulura bidaltzen du.
 */
public class ActiKalkulu1 extends AppCompatActivity {

    /** Bigarren jarduerari bidaltzeko izenaren gakoa. */
    public static final String EXTRA_IZENA = "com.ikasle.abiarazi_kalkulua.IZENA";

    /**
     * Izenaren eremua eta bi botoiak prestatzen ditu.
     * @param savedInstanceState aurreko egoera gordea
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutkalkulu1);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        EditText izena = findViewById(R.id.edtIzena);
        findViewById(R.id.btnKalkulatu).setOnClickListener(v -> {
            String sartutakoIzena = izena.getText().toString().trim();
            if (sartutakoIzena.isEmpty()) {
                izena.setError(getString(R.string.izena_derrigorrezkoa));
                izena.requestFocus();
                return;
            }
            Intent intent = new Intent(this, ActKalkulu2.class);
            intent.putExtra(EXTRA_IZENA, sartutakoIzena);
            startActivity(intent);
        });
        findViewById(R.id.btnIrten).setOnClickListener(v -> finish());
    }
}
