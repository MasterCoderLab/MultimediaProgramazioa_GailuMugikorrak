package com.ikasle.kalkulagailua;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import java.math.BigDecimal;

/**
 * Bi zenbaki naturalekin lau eragiketa egiteko jarduera.
 * Irakaslearen lau kalkulu-metodoak aldatu gabe mantentzen ditu.
 */
public class MainActivity extends AppCompatActivity {

    private EditText edt1;
    private EditText edt2;
    private RadioGroup rdg;
    private TextView txtEmaitza;

    /**
     * Jarduera prestatzen du, interfaze-elementuak lotzen ditu eta
     * botoien gertaerak ezartzen ditu.
     *
     * @param savedInstanceState aurreko jardueraren egoera
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Sistemaren barrak aurreko Android ariketen modu berean doitzen dira.
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
        WindowInsetsControllerCompat kontrola = new WindowInsetsControllerCompat(
                getWindow(), getWindow().getDecorView());
        kontrola.setAppearanceLightStatusBars(false);
        kontrola.setAppearanceLightNavigationBars(true);

        edt1 = findViewById(R.id.edtNum1);
        edt2 = findViewById(R.id.edtNum2);
        rdg = findViewById(R.id.rdgEragiketak);
        txtEmaitza = findViewById(R.id.txtEmaitza);
        Button kalkulatu = findViewById(R.id.btnKalkulatu);
        Button garbitu = findViewById(R.id.btnGarbitu);

        // Klik egitean kalkulatzen da; RadioGroup-ek hautaketa soilik gordetzen du.
        kalkulatu.setOnClickListener(v -> kalkulatu());
        garbitu.setOnClickListener(v -> eremuakGarbitu());

        // Balioak edo eragiketa aldatzen direnean emaitza zaharra ezkutatzen da.
        rdg.setOnCheckedChangeListener((group, checkedId) -> emaitzaEzkutatu());
        edt1.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(android.text.Editable s) { emaitzaEzkutatu(); }
        });
        edt2.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(android.text.Editable s) { emaitzaEzkutatu(); }
        });
    }

    /**
     * Bi zenbaki naturalak eta hautatutako eragiketa baliozkotzen ditu.
     * Bigarren zenbakia zero izatea zatiketan bakarrik da debekatua.
     */
    private void kalkulatu() {
        String lehenTestua = edt1.getText().toString().trim();
        String bigarrenTestua = edt2.getText().toString().trim();
        if (lehenTestua.isEmpty()) {
            errorea(edt1, R.string.errorea_hutsa);
            return;
        }
        if (bigarrenTestua.isEmpty()) {
            errorea(edt2, R.string.errorea_hutsa);
            return;
        }

        long lehena;
        long bigarrena;
        try {
            // Zenbaki naturalak: 0 edo handiago diren zenbaki osoak.
            if (!lehenTestua.matches("[0-9]+")) {
                errorea(edt1, R.string.errorea_naturala);
                return;
            }
            lehena = Long.parseLong(lehenTestua);
        } catch (NumberFormatException e) {
            errorea(edt1, R.string.errorea_naturala);
            return;
        }
        try {
            if (!bigarrenTestua.matches("[0-9]+")) {
                errorea(edt2, R.string.errorea_naturala);
                return;
            }
            bigarrena = Long.parseLong(bigarrenTestua);
        } catch (NumberFormatException e) {
            errorea(edt2, R.string.errorea_naturala);
            return;
        }

        int hautatua = rdg.getCheckedRadioButtonId();
        if (hautatua == -1) {
            txtEmaitza.setText(R.string.errorea_eragiketa);
            txtEmaitza.setVisibility(View.VISIBLE);
            return;
        }
        if (hautatua == R.id.rdbZatiketa && bigarrena == 0) {
            errorea(edt2, R.string.errorea_zero_zatiketa);
            return;
        }

        // Kalkuluak irakaslearen metodoak erabiliz egiten dira, aldatu gabe.
        double emaitza;
        if (hautatua == R.id.rdbBatuketa) {
            emaitza = sumar(lehena, bigarrena);
        } else if (hautatua == R.id.rdbKenketa) {
            emaitza = restar(lehena, bigarrena);
        } else if (hautatua == R.id.rdbBiderketa) {
            emaitza = multiplicar(lehena, bigarrena);
        } else if (hautatua == R.id.rdbZatiketa) {
            emaitza = dividir(lehena, bigarrena);
        } else {
            txtEmaitza.setText(R.string.errorea_eragiketa);
            txtEmaitza.setVisibility(View.VISIBLE);
            return;
        }

        // Ez erakutsi Infinity/NaN bezalako emaitza baliogabeak.
        if (!Double.isFinite(emaitza)) {
            txtEmaitza.setText(R.string.errorea_kalkulua);
        } else {
            // 5.0 -> 5, 3.5 -> 3.5; gehiegizko zeroak kendu.
            String balioa = BigDecimal.valueOf(emaitza)
                    .stripTrailingZeros().toPlainString().replace('.', ',');
            txtEmaitza.setText(getString(R.string.emaitza, balioa));
        }
        txtEmaitza.setVisibility(View.VISIBLE);
    }

    /**
     * Eremu batean akatsa adierazten du eta fokua bertara mugitzen du.
     *
     * @param eremua baliozkotu ez den testu-eremua
     * @param mezua errore-mezuaren baliabidea
     */
    private void errorea(EditText eremua, int mezua) {
        eremua.setError(getString(mezua));
        eremua.requestFocus();
        emaitzaEzkutatu();
    }

    /**
     * Emaitza zaharra ezkutatzen du.
     */
    private void emaitzaEzkutatu() {
        txtEmaitza.setText("");
        txtEmaitza.setVisibility(View.GONE);
    }

    /**
     * Datuak, hautatutako eragiketa eta erroreak ezabatzen ditu.
     */
    private void eremuakGarbitu() {
        edt1.setText("");
        edt2.setText("");
        edt1.setError(null);
        edt2.setError(null);
        rdg.clearCheck();
        emaitzaEzkutatu();
        edt1.requestFocus();
    }

    // IRAKASLEAREN METODO ORIGINALAK - Calculadora con radiogroup.java
    // Lau metodoak bere horretan, sinadura eta gorputza aldatu gabe.
    public double sumar(double a,double b){
        double resultado;
		resultado=a+b;
		return resultado;
    }
    public double restar(double a,double b){
        return(a-b);
    }
    public double multiplicar(double a,double b){
        return(a*b);
    }
    public double dividir(double a,double b){
		return (a/b);
    }
}
