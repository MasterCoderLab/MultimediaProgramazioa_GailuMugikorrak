package com.ikasle.digitu_batura;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Erabiltzaileak sartutako zenbaki osoaren digituen batura kalkulatzen du.
 */
public class MainActivity extends AppCompatActivity {

    private EditText edtZenbakia;
    private TextView txtEmaitza;

    /**
     * Pantaila prestatzen du eta kalkulatzeko botoia lotzen du.
     *
     * @param savedInstanceState aurreko egoera gordea, egonez gero
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtZenbakia = findViewById(R.id.edtZenbakia);
        txtEmaitza = findViewById(R.id.txtEmaitza);
        Button btnExekutatu = findViewById(R.id.btnExekutatu);
        btnExekutatu.setOnClickListener(v -> kalkulatu());
    }

    /** Erabiltzailearen sarrera baliozkotu eta digituen batura erakusten du. */
    private void kalkulatu() {
        String testua = edtZenbakia.getText().toString().trim();
        if (testua.isEmpty()) {
            edtZenbakia.setError(getString(R.string.zenbakia_derrigorrezkoa));
            return;
        }

        int zenbakia;
        try {
            zenbakia = Integer.parseInt(testua);
        } catch (NumberFormatException salbuespena) {
            edtZenbakia.setError(getString(R.string.zenbakia_baliogabea));
            return;
        }

        edtZenbakia.setError(null);
        int batura = batu_digitu(zenbakia);
        txtEmaitza.setText(getString(R.string.emaitza, batura));
        txtEmaitza.setVisibility(View.VISIBLE);
    }

    /**
     * Zenbaki osoaren digitu guztien batura itzultzen du.
     * Zenbaki negatiboen digituen zeinua baztertzen da; 0 zenbakiaren batura 0 da.
     *
     * @param zenbakia aztertu beharreko zenbaki osoa
     * @return digituen batura
     */
    private int batu_digitu(int zenbakia) {
        int batura = 0;
        do {
            batura += Math.abs(zenbakia % 10);
            zenbakia /= 10;
        } while (zenbakia != 0);
        return batura;
    }
}
