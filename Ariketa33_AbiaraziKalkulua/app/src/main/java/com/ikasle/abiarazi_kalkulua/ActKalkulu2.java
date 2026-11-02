package com.ikasle.abiarazi_kalkulua;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;

/**
 * Bigarren jarduera: jasotako izena erakutsi eta zenbakiaren bikoitza kalkulatzen du.
 */
public class ActKalkulu2 extends AppCompatActivity {

    /**
     * Izenaren mezua eta bikoizteko botoia prestatzen ditu.
     * @param savedInstanceState aurreko egoera gordea
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutkalkulu2);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        String izena = getIntent().getStringExtra(ActiKalkulu1.EXTRA_IZENA);
        if (izena == null) izena = "";
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
                BigDecimal bikoitza = new BigDecimal(sartutakoa.replace(',', '.'))
                        .multiply(BigDecimal.valueOf(2));
                emaitza.setText(getString(R.string.emaitza_mezua,
                        bikoitza.stripTrailingZeros().toPlainString()));
            } catch (NumberFormatException salbuespena) {
                zenbakia.setError(getString(R.string.zenbakia_baliogabea));
                zenbakia.requestFocus();
            }
        });
    }
}
