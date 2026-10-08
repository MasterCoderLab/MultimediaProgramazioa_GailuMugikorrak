package com.ikasle.asteroideak;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Aplikazioaren "Acerca de..." informazioa bistaratzen duen jarduera.
 *
 * Jarduera honek acercade.xml layout-a erabiltzen du aplikazioari
 * buruzko informazioa erabiltzaileari erakusteko.
 */
public class AcercaDeActivity extends AppCompatActivity {

    /**
     * Jarduera sortzen denean exekutatzen den metodoa.
     *
     * Metodo honek acercade.xml layout-a jardueraren ikuspegi
     * nagusi bezala ezartzen du.
     *
     * @param savedInstanceState Jardueraren aurreko egoera gordetzen duen
     *                           Bundle objektua. Egoerarik ez badago, null izan daiteke.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acercade);
    }
}