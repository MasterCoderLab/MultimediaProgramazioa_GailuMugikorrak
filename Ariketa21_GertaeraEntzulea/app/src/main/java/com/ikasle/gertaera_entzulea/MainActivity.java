package com.ikasle.gertaera_entzulea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Aplikazioaren jarduera nagusia.
 *
 * <p>Jarduera honetan "Acerca de..." botoiaren klik-gertaera
 * Java kodearen bidez kudeatzen da. Horretarako,
 * setOnClickListener() metodoa erabiltzen da.</p>
 *
 * <p>Modu honetan ez da android:onClick atributua XML fitxategian
 * definitu behar.</p>
 */
public class MainActivity extends AppCompatActivity {

    /**
     * "Acerca de..." botoiaren erreferentzia gordetzen du.
     */
    private Button bAcercaDe;

    /**
     * Jarduera sortzen denean exekutatzen den metodoa.
     *
     * <p>activity_main.xml interfazea kargatzen du, botoiaren
     * erreferentzia lortzen du eta klik-gertaeraren entzulea
     * programatikoki erregistratzen du.</p>
     *
     * @param savedInstanceState jardueraren aurreko egoera gordetzen duen
     *                           Bundle objektua; lehen exekuzioan null izan daiteke
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Jardueraren interfaze grafikoa kargatzen da.
        setContentView(R.layout.activity_main);

        // XML fitxategian definitutako botoiaren erreferentzia lortzen da.
        bAcercaDe = findViewById(R.id.botonAcercaDe);

        /*
         * Botoiaren klik-gertaeraren entzulea erregistratzen da.
         * Erabiltzaileak botoia sakatzen duenean onClick() metodoa
         * automatikoki exekutatuko da.
         */
        bAcercaDe.setOnClickListener(new View.OnClickListener() {

            /**
             * Botoia sakatzen denean exekutatzen den metodoa.
             *
             * @param view klik-gertaera sortu duen bista
             */
            @Override
            public void onClick(View view) {

                // AcercaDeActivity jarduera irekitzeko metodoari deitzen zaio.
                lanzarAcercaDe(null);
            }
        });
    }

    /**
     * AcercaDeActivity jarduera irekitzen du.
     *
     * <p>Intent esplizitu bat erabiltzen da uneko jardueratik
     * AcercaDeActivity jarduerara nabigatzeko.</p>
     *
     * @param view metodoa aktibatu duen bista; kasu honetan ez da erabiltzen
     */
    public void lanzarAcercaDe(View view) {

        // Uneko jardueratik AcercaDeActivity-ra joateko Intent-a sortzen da.
        Intent intent = new Intent(this, AcercaDeActivity.class);

        // AcercaDeActivity jarduera abiarazten da.
        startActivity(intent);
    }
}