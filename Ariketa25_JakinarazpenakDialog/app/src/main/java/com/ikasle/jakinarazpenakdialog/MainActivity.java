package com.ikasle.jakinarazpenakdialog;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

/**
 * Lau elkarrizketa-mota abiarazten dituen jarduera nagusia.
 * Irakaslearen activity_main.xml fitxategiko lau botoiak erabiltzen ditu.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Jardueraren interfazea kargatu eta botoien entzuleak ezartzen ditu.
     *
     * @param savedInstanceState jardueraren aurreko egoerari buruzko datuak
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Sistema-barren tartea eta irakaslearen 5dp-ko ertza mantentzen dira.
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.LinearLayout1), (v, insets) -> {
                    Insets barras = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());
                    int tartea = (int) (5 *
                            getResources().getDisplayMetrics().density + 0.5f);
                    v.setPadding(barras.left + tartea,
                            barras.top + tartea,
                            barras.right + tartea,
                            barras.bottom + tartea);
                    return insets;
                });

        // Alerta-elkarrizketa irekitzen duen botoia.
        findViewById(R.id.BtnAlerta).setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DialogoAlerta dialogo = new DialogoAlerta();
            dialogo.show(fragmentManager, "tagAlerta");
        });

        // Berrespen-elkarrizketa irekitzen duen botoia.
        findViewById(R.id.BtnConfirmacion).setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DialogoConfirmacion dialogo = new DialogoConfirmacion();
            dialogo.show(fragmentManager, "tagConfirmacion");
        });

        // Aukera bakarreko elkarrizketa irekitzen duen botoia.
        findViewById(R.id.BtnSeleccionSimple).setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DialogoSeleccion dialogo = new DialogoSeleccion();
            dialogo.show(fragmentManager, "tagSeleccionSimple");
        });

        // Aukera anitzeko elkarrizketa irekitzen duen botoia.
        findViewById(R.id.BtnSeleccionMultiple).setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            DialogoSeleccionMultiple dialogo = new DialogoSeleccionMultiple();
            dialogo.show(fragmentManager, "tagSeleccionMultiple");
        });
    }
}
