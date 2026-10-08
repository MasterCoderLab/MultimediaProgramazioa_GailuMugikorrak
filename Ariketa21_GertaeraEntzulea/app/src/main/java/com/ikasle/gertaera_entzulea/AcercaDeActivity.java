package com.ikasle.gertaera_entzulea;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Aplikazioari buruzko informazioa bistaratzen duen jarduera.
 *
 * <p>Jarduera honek acercade.xml fitxategian definitutako
 * interfazea erakusten du.</p>
 */
public class AcercaDeActivity extends AppCompatActivity {

    /**
     * Jarduera sortzen denean exekutatzen den metodoa.
     *
     * @param savedInstanceState jardueraren aurreko egoera gordetzen duen
     *                           Bundle objektua; lehen exekuzioan null izan daiteke
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // acercade.xml fitxategia jardueraren interfaze gisa kargatzen da.
        setContentView(R.layout.acercade);
    }
}