package com.ikasle.ikaslearenkalifikazioa_b;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

/**
 * 26 B ariketa: nota baliozkotu, maila kalkulatu eta emaitza pantailan erakusten du.
 *
 * A bertsiotik abiatuta, botoien egoera eta emaitza iraunkorra gehitu dira.
 */
public class MainActivity extends AppCompatActivity {

    private EditText ikaslea;
    private EditText nota;
    private Button garbitu;
    private Button erakutsi;
    private TextView emaitza;

    /**
     * Interfazea prestatzen du, barra sistemikoak doitzen ditu eta entzuleak ezartzen ditu.
     *
     * @param savedInstanceState aurreko egoera, halakorik badago
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Egoera- eta nabigazio-barren tarteak egokitu.
        View barra = findViewById(R.id.fondoBarraEstado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets sistemarenBarrak = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.LayoutParams neurriak = barra.getLayoutParams();
            if (neurriak.height != sistemarenBarrak.top) {
                neurriak.height = sistemarenBarrak.top;
                barra.setLayoutParams(neurriak);
            }
            v.setPadding(sistemarenBarrak.left, 0,
                    sistemarenBarrak.right, sistemarenBarrak.bottom);
            return insets;
        });
        WindowInsetsControllerCompat kontrolatzailea =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        kontrolatzailea.setAppearanceLightStatusBars(false);
        kontrolatzailea.setAppearanceLightNavigationBars(true);

        ikaslea = findViewById(R.id.edtIkaslea);
        nota = findViewById(R.id.edtNota);
        garbitu = findViewById(R.id.btnGarbitu);
        erakutsi = findViewById(R.id.btnErakutsi);
        emaitza = findViewById(R.id.txtEmaitza);

        garbitu.setEnabled(false);
        erakutsi.setEnabled(true);
        emaitza.setText("");
        emaitza.setVisibility(View.GONE);

        // Daturik badago, GARBITU gaitzen da; emaitza erakutsi ondoren ere erabilgarri.
        TextWatcher eremuenEntzulea = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                garbitu.setEnabled(ikaslea.length() > 0 || nota.length() > 0);
            }
        };
        ikaslea.addTextChangedListener(eremuenEntzulea);
        nota.addTextChangedListener(eremuenEntzulea);

        garbitu.setOnClickListener(v -> garbituEremuak());
        erakutsi.setOnClickListener(v -> erakutsiKalifikazioa());
    }

    /**
     * Izena, nota, emaitza eta erroreak ezabatzen ditu; botoien egoera berrezartzen du.
     */
    private void garbituEremuak() {
        ikaslea.setText("");
        nota.setText("");
        ikaslea.setError(null);
        nota.setError(null);
        emaitza.setText("");
        emaitza.setVisibility(View.GONE);
        erakutsi.setEnabled(true);
        garbitu.setEnabled(false);
        ikaslea.requestFocus();
    }

    /**
     * Sarrerak baliozkotu eta emaitza pantailan erakusten du.
     * Eragiketa zuzena denean, ERAKUTSI desgaitu egiten da GARBITU sakatu arte.
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
        emaitza.setText(mezua);
        emaitza.setVisibility(View.VISIBLE);
        garbitu.setEnabled(true);
        erakutsi.setEnabled(false);
    }

    /**
     * Irakaslearen adibideen muga inklusiboak errespetatzen ditu.
     * 18 baino handiagoa eta 19 baino txikiagoa 'Oso Ona' izatea diseinu-erabakia da.
     *
     * @param balioa 0 eta 20 arteko nota baliozkotua
     * @return kalifikazioaren izena euskaraz
     */
    private String sailkatuNota(double balioa) {
        if (balioa <= 10.5) {
            return getString(R.string.txarra);
        } else if (balioa <= 14) {
            return getString(R.string.erdipurdikoa);
        } else if (balioa <= 18) {
            return getString(R.string.ona);
        } else if (balioa < 19) {
            return getString(R.string.oso_ona);
        } else {
            return getString(R.string.bikaina);
        }
    }
}
