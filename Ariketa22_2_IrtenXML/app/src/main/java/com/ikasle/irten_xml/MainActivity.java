package com.ikasle.irten_xml;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Aplikazioaren jarduera nagusia.
 *
 * <p>Ariketa honetan "Irten" botoiaren klik-gertaera
 * XML fitxategiko android:onClick atributuaren bidez
 * kudeatzen da.</p>
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Jarduera sortzen denean exekutatzen den metodoa.
     *
     * <p>activity_main.xml interfazea kargatzen du.</p>
     *
     * @param savedInstanceState jardueraren aurreko egoera gordetzen duen
     *                           Bundle objektua; lehen exekuzioan null izan daiteke
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Jardueraren interfaze grafikoa kargatzen da.
        setContentView(R.layout.activity_main);
    }

    /**
     * "Irten" botoia sakatzen denean jarduera amaitzen du.
     *
     * <p>Metodo hau activity_main.xml fitxategiko
     * android:onClick="irten" atributuaren bidez exekutatzen da.</p>
     *
     * @param view klik-gertaera sortu duen bista
     */
    public void irten(View view) {

        // Uneko jarduera amaitzen da.
        finish();
    }
}