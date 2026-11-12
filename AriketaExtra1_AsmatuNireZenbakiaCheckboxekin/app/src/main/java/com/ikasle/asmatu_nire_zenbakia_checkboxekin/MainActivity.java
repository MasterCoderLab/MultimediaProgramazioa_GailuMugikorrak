package com.ikasle.asmatu_nire_zenbakia_checkboxekin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

/**
 * 0 eta 100 arteko ausazko zenbakia asmatzeko jokoa kudeatzen du.
 */
public class MainActivity extends AppCompatActivity {

    private int zenbakiSekretua;
    private int saiakerak;
    private EditText zenbakia;
    private TextView mezua;
    private CheckBox hamarSaiakera;
    private CheckBox laguntza;
    private Button egiaztatu;

    /**
     * Jokoa prestatzen du eta erabiltzailearen aukerak konektatzen ditu.
     *
     * @param savedInstanceState aurreko egoera gordea
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        zenbakiSekretua = new Random().nextInt(101);
        saiakerak = 0;

        zenbakia = findViewById(R.id.edtZenbakia);
        mezua = findViewById(R.id.txtMezua);
        hamarSaiakera = findViewById(R.id.chkHamarSaiakera);
        laguntza = findViewById(R.id.chkLaguntza);
        egiaztatu = findViewById(R.id.btnEgiaztatu);

        egiaztatu.setOnClickListener(v -> saiakeraEgiaztatu());
    }

    /** Sartutako zenbakia egiaztatzen du eta saiakeraren emaitza erakusten du. */
    private void saiakeraEgiaztatu() {
        String testua = zenbakia.getText().toString().trim();
        if (testua.isEmpty()) {
            zenbakia.setError(getString(R.string.zenbakia_derrigorrezkoa));
            zenbakia.requestFocus();
            return;
        }

        int proposamena;
        try {
            proposamena = Integer.parseInt(testua);
        } catch (NumberFormatException salbuespena) {
            zenbakia.setError(getString(R.string.zenbakia_baliogabea));
            zenbakia.requestFocus();
            return;
        }

        if (proposamena < 0 || proposamena > 100) {
            zenbakia.setError(getString(R.string.zenbakia_baliogabea));
            zenbakia.requestFocus();
            return;
        }

        saiakerak++;
        if (proposamena == zenbakiSekretua) {
            mezua.setText(getString(R.string.asmatuta, zenbakiSekretua, saiakerak));
            jokoaAmaitu();
        } else if (hamarSaiakera.isChecked() && saiakerak >= 10) {
            mezua.setText(getString(R.string.muga_gaindituta, saiakerak, zenbakiSekretua));
            jokoaAmaitu();
        } else if (laguntza.isChecked()) {
            int mezuaId = proposamena < zenbakiSekretua
                    ? R.string.handiagoa : R.string.txikiagoa;
            mezua.setText(mezuaId);
        } else {
            mezua.setText(R.string.ez_da_asmatu);
        }
    }

    /** Jokoa amaitzen denean zenbaki gehiago sartzea eragozten du. */
    private void jokoaAmaitu() {
        egiaztatu.setEnabled(false);
        zenbakia.setEnabled(false);
    }
}
