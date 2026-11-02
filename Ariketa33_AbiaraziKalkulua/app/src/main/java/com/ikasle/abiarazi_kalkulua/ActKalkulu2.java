package com.ikasle.abiarazi_kalkulua;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Bigarren jarduera: jasotako izena erakusten du
 * eta erabiltzaileak sartutako zenbakiaren bikoitza kalkulatzen du.
 */
public class ActKalkulu2 extends AppCompatActivity {

    /**
     * Erabiltzailearen izena erakusten du eta kalkulatzeko botoia prestatzen du.
     *
     * @param savedInstanceState aurreko egoera gordea
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutkalkulu2);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        String izena = getIntent().getStringExtra(ActiKalkulu1.EXTRA_IZENA);

        if (izena == null) {
            izena = "";
        }

        TextView erabiltzailea = findViewById(R.id.txtErabiltzailea);
        erabiltzailea.setText(getString(R.string.erabiltzaile_mezua, izena));

        EditText zenbakia = findViewById(R.id.edtZenbakia);
        TextView emaitza = findViewById(R.id.txtEmaitza);

        findViewById(R.id.btnBikoiztu).setOnClickListener(v -> {
            String sartutakoa = zenbakia.getText().toString().trim();

            if (sartutakoa.isEmpty()) {
                zenbakia.setError(getString(R.string.zenbakia_derrigorrezkoa));
                zenbakia.requestFocus();
                return;
            }

            try {
                double bikoitza = Double.parseDouble(
                        sartutakoa.replace(',', '.')) * 2;

                String bikoitzaTestua = bikoitza == (long) bikoitza
                        ? Long.toString((long) bikoitza)
                        : Double.toString(bikoitza);

                emaitza.setText(getString(R.string.emaitza_mezua, bikoitzaTestua));

            } catch (NumberFormatException salbuespena) {
                zenbakia.setError(getString(R.string.zenbakia_baliogabea));
                zenbakia.requestFocus();
            }
        });
    }
}
