package com.ikasle.ikaslearenkalifikazioa;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

/**
 * Ikaslearen nota baliozkotu eta kalifikazioa erakusten duen jarduera.
 *
 * Notak 0 eta 20 artean egon behar du; bost mailatan sailkatzen da.
 */
public class MainActivity extends AppCompatActivity {

    private EditText ikaslea;
    private EditText nota;
    private Button garbitu;

    /**
     * Jardueraren interfazea prestatzen du eta botoien entzuleak ezartzen ditu.
     *
     * @param savedInstanceState jardueraren aurreko egoera
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Egoera-barraren eta nabigazio-barraren tarteak egokitzen dira.
        View barra = findViewById(R.id.fondoBarraEstado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets sistemarenBarrak = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());
                    ViewGroup.LayoutParams neurriak = barra.getLayoutParams();
                    if (neurriak.height != sistemarenBarrak.top) {
                        neurriak.height = sistemarenBarrak.top;
                        barra.setLayoutParams(neurriak);
                    }
                    v.setPadding(sistemarenBarrak.left, 0,
                            sistemarenBarrak.right, sistemarenBarrak.bottom);
                    return insets;
                });

        WindowInsetsControllerCompat barraKontrolatzailea =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        barraKontrolatzailea.setAppearanceLightStatusBars(false);
        barraKontrolatzailea.setAppearanceLightNavigationBars(true);

        ikaslea = findViewById(R.id.edtIkaslea);
        nota = findViewById(R.id.edtNota);
        garbitu = findViewById(R.id.btnGarbitu);
        Button erakutsi = findViewById(R.id.btnErakutsi);

        // Hasieran garbitzeko botoia desgaituta dago.
        garbitu.setEnabled(false);

        // Eremuetako bat betetzen denean GARBITU botoia gaitzen da.
        TextWatcher eremuenEntzulea = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                boolean datuakDaude = ikaslea.length() > 0 || nota.length() > 0;
                garbitu.setEnabled(datuakDaude);
            }
        };
        ikaslea.addTextChangedListener(eremuenEntzulea);
        nota.addTextChangedListener(eremuenEntzulea);

        garbitu.setOnClickListener(v -> garbituEremuak());
        erakutsi.setOnClickListener(v -> erakutsiKalifikazioa());
    }

    /**
     * Ikaslearen izena eta nota ezabatzen ditu, eta fokua izenean jartzen du.
     */
    private void garbituEremuak() {
        ikaslea.setText("");
        nota.setText("");
        ikaslea.requestFocus();
        // TextWatcher entzuleak GARBITU botoia berriro desgaitzen du.
    }

    /**
     * Sartutako datuak baliozkotu eta dagokien kalifikazioa erakusten du.
     */
    private void erakutsiKalifikazioa() {
        String ikaslearenIzena = ikaslea.getText().toString().trim();
        String notaTestua = nota.getText().toString().trim();

        if (ikaslearenIzena.isEmpty()) {
            ikaslea.setError(getString(R.string.errorea_ikaslea));
            ikaslea.requestFocus();
            return;
        }
        if (notaTestua.isEmpty()) {
            nota.setError(getString(R.string.errorea_nota_hutsa));
            nota.requestFocus();
            return;
        }

        // Koma eta puntua onartzen dira zenbaki hamartarrak idazteko.
        double notaBalioa;
        try {
            notaBalioa = Double.parseDouble(notaTestua.replace(',', '.'));
        } catch (NumberFormatException e) {
            nota.setError(getString(R.string.errorea_nota_formatua));
            nota.requestFocus();
            return;
        }

        if (!Double.isFinite(notaBalioa) || notaBalioa < 0 || notaBalioa > 20) {
            nota.setError(getString(R.string.errorea_nota_tartea));
            nota.requestFocus();
            return;
        }

        String kalifikazioa = sailkatuNota(notaBalioa);
        String mezua = getString(R.string.emaitzaren_mezua,
                ikaslearenIzena, notaTestua, kalifikazioa);

        // Kalifikazioa ikasleari jakinarazteko elkarrizketa erakusten da.
        new AlertDialog.Builder(this)
                .setTitle(R.string.emaitzaren_izenburua)
                .setMessage(mezua)
                .setPositiveButton(R.string.adostu, (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Nota dagokion mailan sailkatzen du, tarteak gainjarri gabe.
     *
     * @param balioa 0 eta 20 arteko nota baliozkotua
     * @return kalifikazioaren izena euskaraz
     */
    private String sailkatuNota(double balioa) {
        if (balioa < 10.5) {
            return getString(R.string.txarra);
        } else if (balioa < 14) {
            return getString(R.string.erdipurdikoa);
        } else if (balioa < 18) {
            return getString(R.string.ona);
        } else if (balioa < 19) {
            return getString(R.string.oso_ona);
        } else {
            return getString(R.string.bikaina);
        }
    }
}
