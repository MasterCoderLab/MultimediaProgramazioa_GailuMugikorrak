package com.ikasle.hiruen_handiena;

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
 * Hiru zenbaki osoen artean handiena bilatu eta pantailan erakusten du.
 * Irakasleak emandako mayor_tres metodoa bere horretan mantentzen da.
 */
public class MainActivity extends AppCompatActivity {

    private EditText lehena;
    private EditText bigarrena;
    private EditText hirugarrena;
    private TextView emaitza;

    /**
     * Interfazea hasieratu eta ERAKUTSI botoiaren ekintza konfiguratzen du.
     *
     * @param savedInstanceState aurreko jardueraren egoera
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Android egoera-barrari tokia utzi, aurreko ariketen diseinua bezala.
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

        lehena = findViewById(R.id.edtLehena);
        bigarrena = findViewById(R.id.edtBigarrena);
        hirugarrena = findViewById(R.id.edtHirugarrena);
        emaitza = findViewById(R.id.txtEmaitza);
        Button erakutsi = findViewById(R.id.btnErakutsi);
        erakutsi.setOnClickListener(v -> erakutsiHandiena());

        // Datuak aldatzean aurreko kalkuluaren emaitza ezkutatzen da.
        TextWatcher aldaketenEntzulea = new TextWatcher() {
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
        lehena.addTextChangedListener(aldaketenEntzulea);
        bigarrena.addTextChangedListener(aldaketenEntzulea);
        hirugarrena.addTextChangedListener(aldaketenEntzulea);
    }

    /**
     * Moodleko eskakizuna: hiru zenbakiren artean handiena itzultzen du.
     * Irakaslearen jatorrizko metodoa erabiltzen da, aldatu gabe.
     *
     * @param num1 lehenengo zenbakia
     * @param num2 bigarren zenbakia
     * @param num3 hirugarren zenbakia
     * @return hiruren artean handiena
     */
    long hiruen_handiena(long num1, long num2, long num3) {
        return mayor_tres(num1, num2, num3);
    }

    // Irakaslearen 'Mayor3 con metodo.java' fitxategiko metodoa, HITZEZ HITZ.
    private long mayor_tres(long num1, long num2, long num3) {
        long numMax;

        if(num1 > num2 && num1 > num3) {
            numMax = num1;
        } else if(num2 > num3) {
            numMax = num2;
        } else {
            numMax = num3;
        }

        return numMax;
    }

    /**
     * Hiru eremuak baliozkotu eta zenbaki handiena erakusten du.
     */
    private void erakutsiHandiena() {
        EditText[] eremuak = {lehena, bigarrena, hirugarrena};
        long[] balioak = new long[3];

        for (int i = 0; i < eremuak.length; i++) {
            EditText eremua = eremuak[i];
            String testua = eremua.getText().toString().trim();
            if (testua.isEmpty()) {
                eremua.setError(getString(R.string.errorea_hutsa));
                eremua.requestFocus();
                return;
            }
            try {
                // Irakaslearen lehen soluzioan bezala, long zenbaki osoak.
                balioak[i] = Long.parseLong(testua);
            } catch (NumberFormatException e) {
                eremua.setError(getString(R.string.errorea_formatua));
                eremua.requestFocus();
                return;
            }
        }

        long maximoa = hiruen_handiena(balioak[0], balioak[1], balioak[2]);
        emaitza.setText(getString(R.string.emaitza_handiena, maximoa));
        emaitza.setVisibility(View.VISIBLE);
    }
}
