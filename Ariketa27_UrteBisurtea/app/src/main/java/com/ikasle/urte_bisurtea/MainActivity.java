package com.ikasle.urte_bisurtea;

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
 * Urte bat bisurtea den ala ez egiaztatzen duen jarduera.
 * Bisurtearen arau gregoriarra erabiltzen du.
 */
public class MainActivity extends AppCompatActivity {

    private EditText urteaEremua;
    private TextView emaitza;

    /**
     * Pantaila prestatzen du, sistemaren barrak egokitu eta botoia lotzen du.
     *
     * @param savedInstanceState jardueraren aurreko egoera
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Sistema-barren tartea egokitzen da, aurreko ariketako ereduari jarraituz.
        View egoeraBarra = findViewById(R.id.fondoBarraEstado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barrak = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            ViewGroup.LayoutParams neurriak = egoeraBarra.getLayoutParams();
            if (neurriak.height != barrak.top) {
                neurriak.height = barrak.top;
                egoeraBarra.setLayoutParams(neurriak);
            }
            v.setPadding(barrak.left, 0, barrak.right, barrak.bottom);
            return insets;
        });
        WindowInsetsControllerCompat kontrola = new WindowInsetsControllerCompat(
                getWindow(), getWindow().getDecorView());
        kontrola.setAppearanceLightStatusBars(false);
        kontrola.setAppearanceLightNavigationBars(true);

        urteaEremua = findViewById(R.id.edtUrtea);
        emaitza = findViewById(R.id.txtEmaitza);
        Button egiaztatu = findViewById(R.id.btnEgiaztatu);
        egiaztatu.setOnClickListener(v -> egiaztatuUrtea());

        // Sarrera aldatuz gero, aurreko emaitza ez da pantailan geratuko.
        urteaEremua.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                emaitza.setText("");
                emaitza.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Irakasleak eskatutako metodoa: urtearen bisurte-izaera kalkulatzen du.
     *
     * @param urtea aztertu beharreko urte osoa
     * @return true, 4ren multiploa bada eta ez 100ena, edo 400ena bada
     */
    boolean bisurtea_da(int urtea) {
        return (urtea % 4 == 0 && urtea % 100 != 0) || urtea % 400 == 0;
    }

    /**
     * Urtea baliozkotu, bisurtea den kalkulatu eta emaitza erakusten du.
     */
    private void egiaztatuUrtea() {
        String testua = urteaEremua.getText().toString().trim();
        if (testua.isEmpty()) {
            urteaEremua.setError(getString(R.string.errorea_hutsa));
            urteaEremua.requestFocus();
            return;
        }

        int urtea;
        try {
            urtea = Integer.parseInt(testua);
        } catch (NumberFormatException e) {
            urteaEremua.setError(getString(R.string.errorea_formatua));
            urteaEremua.requestFocus();
            return;
        }

        // Diseinu-erabakia: urte positiboak bakarrik onartzen ditugu.
        if (urtea <= 0) {
            urteaEremua.setError(getString(R.string.errorea_tartea));
            urteaEremua.requestFocus();
            return;
        }

        int mezua = bisurtea_da(urtea)
                ? R.string.emaitza_bisurtea
                : R.string.emaitza_ez_bisurtea;
        emaitza.setText(getString(mezua, urtea));
        emaitza.setVisibility(View.VISIBLE);
    }
}
