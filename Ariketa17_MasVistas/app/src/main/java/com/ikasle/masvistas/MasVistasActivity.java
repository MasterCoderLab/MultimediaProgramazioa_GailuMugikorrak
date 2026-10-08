package com.ikasle.masvistas;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * MasVistas aplikazioaren jarduera nagusia.
 *
 * <p>Klase honek main.xml fitxategian definitutako interfaze grafikoa
 * kargatzen du eta botoi grafikoaren klik-gertaera kudeatzen du.</p>
 *
 * <p>Ariketa honetan ImageButton pertsonalizatu baten egoerak,
 * android:onClick atributua, listener kontzeptua eta Toast mezuak
 * lantzen dira.</p>
 */
public class MasVistasActivity extends AppCompatActivity {

    /**
     * Jarduera sortzen denean exekutatzen den metodoa.
     *
     * <p>Edge-to-Edge modua aktibatzen du, main.xml layout-a kargatzen
     * du eta sistemaren barrak kontuan hartzeko padding-a aplikatzen du.</p>
     *
     * @param savedInstanceState jardueraren aurreko egoera gordetzen duen
     *                           Bundle objektua; lehen exekuzioan null izan daiteke
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Edge-to-Edge modua aktibatzen da.
        EdgeToEdge.enable(this);

        // main.xml fitxategia jardueraren interfaze grafiko gisa kargatzen da.
        setContentView(R.layout.main);

        /*
         * Sistemaren egoera- eta nabigazio-barren tamaina lortzen da
         * eta layout-ari padding egokia aplikatzen zaio.
         */
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );

            return insets;
        });
    }

    /**
     * Botoi grafikoa sakatzean exekutatzen den listener-a.
     *
     * <p>Metodo hau main.xml fitxategiko android:onClick atributuaren
     * bidez lotuta dago ImageButton-arekin. Botoia sakatzean
     * "Pulsado" mezua duen Toast labur bat erakusten du.</p>
     *
     * @param view klik-gertaera sortu duen bista
     */
    public void sePulsa(View view) {
        Toast.makeText(this, "Pulsado", Toast.LENGTH_SHORT).show();
    }
}