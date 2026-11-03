package com.ikasle.beza_kalkulatu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Produktuaren izena eta hasierako zenbatekoa jaso eta BEZa aukeratzeko jarduera irekitzen du.
 */
public class ActProduktu_Produktua extends AppCompatActivity {

    /** Bigarren jarduerara bidaltzeko datuen gakoak. */
    public static final String EXTRA_PRODUKTUA = "com.ikasle.beza_kalkulatu.PRODUKTUA";
    public static final String EXTRA_ZENBATEKOA = "com.ikasle.beza_kalkulatu.ZENBATEKOA";

    /**
     * Produktuaren datuak eta +BEZ botoia prestatzen ditu.
     *
     * @param savedInstanceState aurreko egoera gordea
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_produktua);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        EditText produktua = findViewById(R.id.edtProduktua);
        EditText zenbatekoa = findViewById(R.id.edtZenbatekoa);

        findViewById(R.id.btnBEZ).setOnClickListener(v -> {
            String izena = produktua.getText().toString().trim();
            String testua = zenbatekoa.getText().toString().trim();

            if (izena.isEmpty()) {
                produktua.setError(getString(R.string.produktua_derrigorrezkoa));
                produktua.requestFocus();
                return;
            }
            if (testua.isEmpty()) {
                zenbatekoa.setError(getString(R.string.zenbatekoa_derrigorrezkoa));
                zenbatekoa.requestFocus();
                return;
            }

            double oinarria;
            try {
                oinarria = Double.parseDouble(testua.replace(',', '.'));
            } catch (NumberFormatException salbuespena) {
                zenbatekoa.setError(getString(R.string.zenbatekoa_baliogabea));
                zenbatekoa.requestFocus();
                return;
            }
            if (!Double.isFinite(oinarria) || oinarria < 0) {
                zenbatekoa.setError(getString(R.string.zenbatekoa_baliogabea));
                zenbatekoa.requestFocus();
                return;
            }

            Intent intent = new Intent(this, ActRegistroBEZ.class);
            intent.putExtra(EXTRA_PRODUKTUA, izena);
            intent.putExtra(EXTRA_ZENBATEKOA, oinarria);
            startActivity(intent);
        });
    }
}
