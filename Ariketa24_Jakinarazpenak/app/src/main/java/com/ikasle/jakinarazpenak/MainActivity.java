package com.ikasle.jakinarazpenak;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

/**
 * Toast jakinarazpenen hiru adibide erakusten dituen jarduera.
 *
 * Toast lehenetsia, kokapen pertsonalizatua eta
 * XML diseinu pertsonalizatua erabiltzen dira.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Jardueraren interfazea prestatzen du eta
     * hiru botoien gertaera-entzuleak ezartzen ditu.
     *
     * @param savedInstanceState jardueraren aurreko egoera
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Egoera-barrako ikonoak zuri erakusten dira.
        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(
                        getWindow(),
                        getWindow().getDecorView()
                );

        controller.setAppearanceLightStatusBars(false);
        controller.setAppearanceLightNavigationBars(true);

        // Egoera-barraren altuera egokitzen da.
        View fondoBarra = findViewById(R.id.fondoBarraEstado);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {

                    Insets barras = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    ViewGroup.LayoutParams params =
                            fondoBarra.getLayoutParams();

                    if (params.height != barras.top) {
                        params.height = barras.top;
                        fondoBarra.setLayoutParams(params);
                    }

                    v.setPadding(
                            barras.left,
                            0,
                            barras.right,
                            barras.bottom
                    );

                    return insets;
                });

        // Lehenengo botoia: Toast lehenetsia.
        findViewById(R.id.btnDefecto).setOnClickListener(
                v -> erakutsiToastLehenetsia());

        // Bigarren botoia: Toast kokapen bereziarekin.
        findViewById(R.id.btnGravity).setOnClickListener(
                v -> erakutsiToastGravity());

        // Hirugarren botoia: Toast pertsonalizatua.
        findViewById(R.id.btnLayout).setOnClickListener(
                v -> erakutsiToastPertsonalizatua());
    }

    /**
     * Toast lehenetsia erakusten du.
     */
    private void erakutsiToastLehenetsia() {

        Toast toast1 = Toast.makeText(
                getApplicationContext(),
                R.string.toast_defecto,
                Toast.LENGTH_SHORT
        );

        toast1.show();
    }

    /**
     * Toast jakinarazpena pantailaren ezkerraldeko
     * erdialdean erakusten du.
     *
     * Android bertsio berrietan ikuspegi pertsonalizatua
     * beharrezkoa da kokapena aldatzeko.
     */
    @SuppressWarnings("deprecation")
    private void erakutsiToastGravity() {

        TextView mezua = new TextView(this);

        mezua.setText(R.string.toast_gravity);
        mezua.setTextColor(Color.WHITE);
        mezua.setTextSize(14);
        mezua.setBackgroundColor(Color.DKGRAY);

        int tartea = dp(10);
        mezua.setPadding(tartea, tartea, tartea, tartea);

        Toast toast2 = new Toast(getApplicationContext());

        toast2.setDuration(Toast.LENGTH_SHORT);
        toast2.setView(mezua);

        // Pantailaren ezkerraldeko erdialdea.
        toast2.setGravity(
                Gravity.CENTER_VERTICAL | Gravity.LEFT,
                0,
                0
        );

        toast2.show();
    }

    /**
     * XML diseinu propioa duen Toast erakusten du.
     */
    @SuppressWarnings("deprecation")
    private void erakutsiToastPertsonalizatua() {

        Toast toast3 = new Toast(getApplicationContext());

        // Toastaren XML diseinua kargatzen da.
        View layout = getLayoutInflater().inflate(
                R.layout.toast_layout,
                null
        );

        // Testu-etiketa eskuratu eta mezua ezarri.
        TextView txtMsg =
                layout.findViewById(R.id.txtMensaje);

        txtMsg.setText(R.string.toast_personalizado);

        toast3.setDuration(Toast.LENGTH_SHORT);
        toast3.setView(layout);
        toast3.show();
    }

    /**
     * dp unitateak pixel bihurtzen ditu.
     *
     * @param balioa dp kopurua
     * @return pixel kopurua
     */
    private int dp(int balioa) {
        return (int) (
                balioa *
                getResources().getDisplayMetrics().density
                + 0.5f
        );
    }
}
