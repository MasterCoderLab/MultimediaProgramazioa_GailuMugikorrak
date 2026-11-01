package com.ikasle.jarduerenartekokomunikazioa2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Jarduera nagusia: izena bidaltzen du eta bigarren jardueraren erantzuna erakusten du.
 */
public class MainActivity extends AppCompatActivity {

    /** Bigarren jarduerari bidaltzen zaion izenaren gakoa. */
    public static final String EXTRA_IZENA = "com.ikasle.jarduerenartekokomunikazioa2.IZENA";

    /** Jarduera nagusira itzultzen den erantzunaren gakoa. */
    public static final String EXTRA_ERANTZUNA = "com.ikasle.jarduerenartekokomunikazioa2.ERANTZUNA";

    /** Bigarren jardueraren emaitza modu eguneratuan jasotzeko erregistratzailea. */
    private final ActivityResultLauncher<Intent> bigarrenJarduera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), emaitza -> {
                if (emaitza.getResultCode() != RESULT_OK || emaitza.getData() == null) {
                    return;
                }
                String erantzuna = emaitza.getData().getStringExtra(EXTRA_ERANTZUNA);
                if (erantzuna != null) {
                    TextView testua = findViewById(R.id.txtEmaitza);
                    testua.setText(getString(R.string.emaitza_mezua, erantzuna));
                }
            });

    /**
     * Pantaila prestatzen du eta EGIAZTATU botoiaren ekintza lotzen du.
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
            // Izen hutsa ez da bigarren jarduerara bidaltzen.
            if (sartutakoIzena.trim().isEmpty()) {
                izena.setError(getString(R.string.errorea_izena));
                izena.requestFocus();
                return;
            }
            Intent intent = new Intent(MainActivity.this, BigarrenActivity.class);
            intent.putExtra(EXTRA_IZENA, sartutakoIzena);
            bigarrenJarduera.launch(intent);
        });
    }
}
