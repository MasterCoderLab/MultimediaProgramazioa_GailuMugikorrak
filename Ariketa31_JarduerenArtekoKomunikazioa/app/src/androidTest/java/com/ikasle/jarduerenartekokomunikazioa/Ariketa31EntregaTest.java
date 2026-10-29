package com.ikasle.jarduerenartekokomunikazioa;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * PDFko bi jardueren komunikazioa egiaztatzen duen tresneria-proba.
 */
@RunWith(AndroidJUnit4.class)
public class Ariketa31EntregaTest {
    private final StringBuilder txostena = new StringBuilder(
            "Ariketa 31 - Jardueren arteko komunikazioa 1\n\n");

    /**
     * Proba gainditua idazten du.
     * @param testua probaren azalpena
     */
    private void ondo(String testua) {
        txostena.append("OK: ").append(testua).append('\n');
        System.out.println("OK PROBA: " + testua);
    }

    /**
     * Irudia edo txostena MediaStore bidez Download karpeta partekatuan gordetzen du.
     * @param izena fitxategiaren izena
     * @param mota MIME mota
     * @param irudia bitmap-a, edo null
     * @param datuak testu-datuak, edo null
     * @throws IOException idaztean arazorik badago
     */
    private void gorde(String izena, String mota, Bitmap irudia, byte[] datuak)
            throws IOException {
        String runId = InstrumentationRegistry.getArguments().getString("runId");
        assertNotNull("runId falta da", runId);
        assertTrue("runId baliogabea", runId.matches("[0-9_]+"));
        ContentResolver resolver = InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getContentResolver();
        ContentValues balioak = new ContentValues();
        balioak.put(MediaStore.MediaColumns.DISPLAY_NAME, izena);
        balioak.put(MediaStore.MediaColumns.MIME_TYPE, mota);
        balioak.put(MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + "/Ariketa31_Entrega_" + runId + "/");
        balioak.put(MediaStore.MediaColumns.IS_PENDING, 1);
        Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, balioak);
        if (uri == null) throw new IOException("MediaStore ezin izan da sortu: " + izena);
        boolean eginda = false;
        try {
            try (OutputStream stream = resolver.openOutputStream(uri)) {
                if (stream == null) throw new IOException("Stream-a falta da: " + izena);
                if (irudia != null) {
                    if (!irudia.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                        throw new IOException("PNG ezin izan da sortu: " + izena);
                    }
                } else {
                    if (datuak == null) throw new IOException("Daturik ez: " + izena);
                    stream.write(datuak);
                }
            }
            ContentValues amaituta = new ContentValues();
            amaituta.put(MediaStore.MediaColumns.IS_PENDING, 0);
            if (resolver.update(uri, amaituta, null, null) < 1) {
                throw new IOException("MediaStore ezin izan da argitaratu: " + izena);
            }
            eginda = true;
        } finally {
            if (!eginda) resolver.delete(uri, null, null);
        }
    }

    /**
     * Android pantailaren benetako irudi bat hartzen du.
     * @param izena fitxategiaren izena
     * @throws Exception kaptura ezin bada gorde
     */
    private void kaptura(String izena) throws Exception {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        Thread.sleep(400);
        Bitmap bm = InstrumentationRegistry.getInstrumentation()
                .getUiAutomation().takeScreenshot();
        assertNotNull("Kaptura falta da", bm);
        try { gorde(izena, "image/png", bm, null); }
        finally { bm.recycle(); }
    }

    /**
     * Izen bat idatzi eta bigarren jarduerako agurra konprobatzen du.
     * @param eszenatokia lehenengo jardueraren eszenatokia
     * @param izena pasatzen den izena
     * @throws Exception kaptura ezin bada gorde
     */
    private void bidali(ActivityScenario<MainActivity> eszenatokia, String izena)
            throws Exception {
        eszenatokia.onActivity(activity -> {
            EditText edit = activity.findViewById(R.id.edtIzena);
            edit.setText(izena);
            activity.findViewById(R.id.btnEgiaztatu).performClick();
        });
        onView(withId(R.id.txtAgurra)).check(matches(isDisplayed()));
        onView(withId(R.id.txtAgurra)).check(matches(withText("Kaixo " + izena)));
        ondo("Intent extra: '" + izena + "' -> 'Kaixo " + izena + "'");
    }

    /**
     * Bi jarduerak, izenaren pasaketa, fokua eta atzera egiteko nabigazioa egiaztatzen ditu.
     * @throws Exception fitxategiak sortzea ezinezkoa bada
     */
    @Test
    public void komunikazioaEtaKapturak() throws Exception {
        try (ActivityScenario<MainActivity> eszenatokia =
                     ActivityScenario.launch(MainActivity.class)) {
            eszenatokia.onActivity(activity -> {
                assertNotNull(activity.findViewById(R.id.edtIzena));
                assertNotNull(activity.findViewById(R.id.btnEgiaztatu));
            });
            ondo("Lehen jarduerako Izena/EditText/EGIAZTATU kontrolak");
            kaptura("01_Hasierako_Pantaila.png");

            eszenatokia.onActivity(activity ->
                    ((EditText) activity.findViewById(R.id.edtIzena)).setText("Ane"));
            kaptura("02_Izena_Sartuta.png");
            bidali(eszenatokia, "Ane");
            kaptura("03_Kaixo_Ane.png");
            pressBack();
            onView(withId(R.id.edtIzena)).check(matches(isDisplayed()));
            ondo("Bigarren jardueratik lehenengora itzultzea");

            eszenatokia.onActivity(activity -> {
                EditText edit = activity.findViewById(R.id.edtIzena);
                edit.setText("   ");
                activity.findViewById(R.id.btnEgiaztatu).performClick();
                assertNotNull("Izen hutsean errore-mezua falta", edit.getError());
                assertTrue("Fokua izenean mantendu behar da", edit.hasFocus());
            });
            onView(withId(R.id.edtIzena)).check(matches(isDisplayed()));
            ondo("Izen hutsaren baliozkotzea eta fokua");
            kaptura("04_Izen_Hutsa_Errorea.png");

            bidali(eszenatokia, "Iker");
            kaptura("05_Kaixo_Iker.png");
        }
        gorde("09_Proben_Emaitzak.txt", "text/plain", null,
                txostena.toString().getBytes(StandardCharsets.UTF_8));
    }
}
