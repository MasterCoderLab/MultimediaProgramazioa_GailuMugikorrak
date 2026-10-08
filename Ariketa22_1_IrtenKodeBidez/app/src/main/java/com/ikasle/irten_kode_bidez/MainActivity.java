package com.ikasle.irten_kode_bidez;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Aplikazioaren jarduera nagusia.
 *
 * <p>Ariketa honetan "Irten" botoiaren klik-gertaera
 * Java kodearen bidez kudeatzen da setOnClickListener()
 * metodoa erabiliz.</p>
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Jarduera sortzen denean exekutatzen den metodoa.
     *
     * <p>activity_main.xml interfazea kargatzen du,
     * "Irten" botoiaren erreferentzia lortzen du eta
     * klik-gertaeraren entzulea erregistratzen du.</p>
     *
     * @param savedInstanceState jardueraren aurreko egoera gordetzen duen
     *                           Bundle objektua; lehen exekuzioan null izan daiteke
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Jardueraren interfaze grafikoa kargatzen da.
        setContentView(R.layout.activity_main);

        // XML fitxategian definitutako "Irten" botoiaren erreferentzia lortzen da.
        Button bIrten = findViewById(R.id.buttonIrten);

        /*
         * Botoiaren klik-gertaeraren entzulea erregistratzen da.
         * Botoia sakatzen denean onClick() metodoa exekutatuko da.
         */
        bIrten.setOnClickListener(new OnClickListener() {

            /**
             * "Irten" botoia sakatzen denean exekutatzen den metodoa.
             *
             * @param view klik-gertaera sortu duen bista
             */
            @Override
            public void onClick(View view) {

                // Uneko jarduera amaitzen da.
                finish();
            }
        });
    }
}