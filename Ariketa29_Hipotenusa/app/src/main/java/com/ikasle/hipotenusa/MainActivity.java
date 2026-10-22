package com.ikasle.hipotenusa;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

/**
 * Triangelu angeluzuzen baten hipotenusa kalkulatzen duen jarduera.
 * Katetoen sarrerak baliozkotu eta erroreen fokua kudeatzen du.
 */
public class MainActivity extends AppCompatActivity {

    private EditText katetoB;
    private EditText katetoC;
    private TextView emaitza;

    /**
     * Interfazea hasieratzen du eta botoien entzuleak prestatzen ditu.
     *
     * @param savedInstanceState aurreko jardueraren egoera
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Aurreko ariketetan bezala, sistemaren barrak doitzen dira.
        View barra = findViewById(R.id.fondoBarraEstado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barrak = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.LayoutParams neurriak = barra.getLayoutParams();
            if (neurriak.height != barrak.top) {
                neurriak.height = barrak.top;
                barra.setLayoutParams(neurriak);
            }
            v.setPadding(barrak.left, 0, barrak.right, barrak.bottom);
            return insets;
        });
        WindowInsetsControllerCompat kontrola = new WindowInsetsControllerCompat(
                getWindow(), getWindow().getDecorView());
        kontrola.setAppearanceLightStatusBars(false);
        kontrola.setAppearanceLightNavigationBars(true);

        katetoB = findViewById(R.id.edtKatetoB);
        katetoC = findViewById(R.id.edtKatetoC);
        emaitza = findViewById(R.id.txtEmaitza);
        Button kalkulatu = findViewById(R.id.btnKalkulatu);
        Button garbitu = findViewById(R.id.btnGarbitu);

        kalkulatu.setOnClickListener(v -> erakutsiHipotenusa());
        garbitu.setOnClickListener(v -> eremuak_garbitu());

        // Katetoren bat aldatzen denean, emaitza zaharra ezkutatzen da.
        TextWatcher entzulea = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                emaitza.setText("");
                emaitza.setVisibility(View.GONE);
            }
        };
        katetoB.addTextChangedListener(entzulea);
        katetoC.addTextChangedListener(entzulea);
    }

    /**
     * Moodleko lehen metodoa: bi katetoetatik hipotenusa itzultzen du.
     * Math.hypot-ek b*b + c*c kalkuluaren gainezkatze bitartekoa saihesten du.
     *
     * @param b lehen katetoaren luzera
     * @param c bigarren katetoaren luzera
     * @return hipotenusa, edo NaN emaitza ezin bada adierazi
     */
    double hipotenusa_kalkulatu(double b, double c) {
        try {
            double hipotenusa = Math.hypot(b, c);
            if (!Double.isFinite(hipotenusa)) {
                throw new ArithmeticException("Hipotenusa ezin da kalkulatu");
            }
            return hipotenusa;
        } catch (ArithmeticException e) {
            return Double.NaN;
        }
    }

    /**
     * Moodleko bigarren metodoa: bi sarrerak eta aurreko emaitza garbitzen ditu.
     */
    void eremuak_garbitu() {
        katetoB.setText("");
        katetoC.setText("");
        katetoB.setError(null);
        katetoC.setError(null);
        emaitza.setText("");
        emaitza.setVisibility(View.GONE);
        katetoB.requestFocus();
    }

    /**
     * Sarrerak baliozkotu, kalkulua egin eta emaitza bistaratzen du.
     */
    private void erakutsiHipotenusa() {
        // c da pantailan lehenengo eremua: lehenik haren erroreak egiaztatzen dira.
        EditText[] eremuak = {katetoC, katetoB};
        double[] balioak = new double[2];

        for (int i = 0; i < eremuak.length; i++) {
            EditText eremua = eremuak[i];
            String testua = eremua.getText().toString().trim();
            if (testua.isEmpty()) {
                eremua.setError(getString(R.string.errorea_hutsa));
                eremua.requestFocus();
                return;
            }
            try {
                // Koma eta puntua onartzen dira hamartarrak idazteko.
                balioak[i] = Double.parseDouble(testua.replace(',', '.'));
            } catch (NumberFormatException e) {
                eremua.setError(getString(R.string.errorea_formatua));
                eremua.requestFocus();
                return;
            }
            if (!Double.isFinite(balioak[i]) || balioak[i] <= 0) {
                eremua.setError(getString(R.string.errorea_positiboa));
                eremua.requestFocus();
                return;
            }
            eremua.setError(null);
        }

        double hipotenusa = hipotenusa_kalkulatu(balioak[1], balioak[0]);
        if (!Double.isFinite(hipotenusa)) {
            katetoB.setError(getString(R.string.errorea_kalkulua));
            katetoB.requestFocus();
            return;
        }

        DecimalFormat formatua = new DecimalFormat("0.##",
                DecimalFormatSymbols.getInstance(Locale.forLanguageTag("eu")));
        emaitza.setText(getString(R.string.emaitza_hipotenusa, formatua.format(hipotenusa)));
        emaitza.setVisibility(View.VISIBLE);
    }
}
