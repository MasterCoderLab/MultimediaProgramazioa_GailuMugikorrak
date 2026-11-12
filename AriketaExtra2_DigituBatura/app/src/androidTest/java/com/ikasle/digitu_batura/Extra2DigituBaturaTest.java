package com.ikasle.digitu_batura;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/** Digituen batura eta sarrera-baliozkotzea egiaztatzen dituzten probak. */
@RunWith(AndroidJUnit4.class)
public class Extra2DigituBaturaTest {

    /** 267 zenbakiaren digituen batura 15 dela frogatzen du. */
    @Test
    public void adibidea267() {
        emaitzaEgiaztatu("267", "Emaitza: 15");
    }

    /** Zeroaren batura zero dela frogatzen du. */
    @Test
    public void zeroa() {
        emaitzaEgiaztatu("0", "Emaitza: 0");
    }

    /** Zenbaki negatiboen digituak positibo gisa batzen direla frogatzen du. */
    @Test
    public void zenbakiNegatiboa() {
        emaitzaEgiaztatu("-267", "Emaitza: 15");
    }

    /** int minimoan ere gainezkatzerik ez dagoela frogatzen du. */
    @Test
    public void intMinimoa() {
        emaitzaEgiaztatu("-2147483648", "Emaitza: 47");
    }

    /** Hutsik dagoen sarrera baztertzen dela frogatzen du. */
    @Test
    public void sarreraHutsa() {
        sarreraOkerraEgiaztatu("");
    }

    /** int motatik kanpoko balioa baztertzen dela frogatzen du. */
    @Test
    public void sarreraHandiegia() {
        sarreraOkerraEgiaztatu("2147483648");
    }

    /**
     * Zenbaki bat sartu eta pantailan espero den batura egiaztatzen du.
     *
     * @param sarrera erabiltzailearen zenbakia
     * @param esperoDa espero den emaitza-testua
     */
    private void emaitzaEgiaztatu(String sarrera, String esperoDa) {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(jarduera -> {
                EditText zenbakia = jarduera.findViewById(R.id.edtZenbakia);
                Button botoia = jarduera.findViewById(R.id.btnExekutatu);
                TextView emaitza = jarduera.findViewById(R.id.txtEmaitza);
                zenbakia.setText(sarrera);
                botoia.performClick();
                assertEquals(esperoDa, emaitza.getText().toString());
            });
        }
    }

    /**
     * Sarrera oker baten ondoren EditText-ek errorea daukala egiaztatzen du.
     *
     * @param sarrera probatu beharreko sarrera okerra
     */
    private void sarreraOkerraEgiaztatu(String sarrera) {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(jarduera -> {
                EditText zenbakia = jarduera.findViewById(R.id.edtZenbakia);
                zenbakia.setText(sarrera);
                jarduera.findViewById(R.id.btnExekutatu).performClick();
                assertNotNull(zenbakia.getError());
            });
        }
    }
}
