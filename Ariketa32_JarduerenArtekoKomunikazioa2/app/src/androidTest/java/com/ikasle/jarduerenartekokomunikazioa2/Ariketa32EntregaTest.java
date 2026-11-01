package com.ikasle.jarduerenartekokomunikazioa2;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Bi jardueren arteko joan-etorriko komunikazioa egiaztatzen duen proba.
 */
@RunWith(AndroidJUnit4.class)
public class Ariketa32EntregaTest {

    /** Proben azalpen laburrak gordetzeko testu-biltegia. */
    private final StringBuilder txostena = new StringBuilder();

    /**
     * Egiaztatutako urrats bat txostenean gehitzen du.
     * @param testua egiaztatutako urratsa
     */
    private void ondo(String testua) {
        txostena.append("OK: ").append(testua).append('\n');
        System.out.println("OK PROBA: " + testua);
    }

    /**
     * Fitxategia Android-en Download karpeta partekatuan sortzen du.
     * @param izena fitxategiaren izena
     * @param mota MIME mota
     * @param irudia gorde beharreko irudia, edo null
     * @param datuak fitxategiaren testu-datuak, edo null
     * @throws IOException fitxategia ezin bada gorde
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
                Environment.DIRECTORY_DOWNLOADS + "/Ariketa32_Entrega_" + runId + "/");
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
            ContentValues bukatuta = new ContentValues();
            bukatuta.put(MediaStore.MediaColumns.IS_PENDING, 0);
            if (resolver.update(uri, bukatuta, null, null) < 1) {
                throw new IOException("MediaStore ezin izan da argitaratu: " + izena);
            }
            eginda = true;
        } finally {
            if (!eginda) resolver.delete(uri, null, null);
        }
    }

    /**
     * Emuladorearen pantaila osoaren irudia hartzen du.
     * @param izena PNG fitxategiaren izena
     * @throws Exception kaptura ezin bada hartu edo gorde
     */
    private void kaptura(String izena) throws Exception {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        Thread.sleep(450);
        Bitmap irudia = InstrumentationRegistry.getInstrumentation()
                .getUiAutomation().takeScreenshot();
        assertNotNull("Kaptura falta da", irudia);
        try { gorde(izena, "image/png", irudia, null); }
        finally { irudia.recycle(); }
    }

    /**
     * Izen bat ezartzen du eta bigarren jarduerara sartzen da.
     * @param eszenatokia jarduera nagusiaren eszenatokia
     * @param izena bidali beharreko izena
     */
    private void bigarreneraJoan(ActivityScenario<MainActivity> eszenatokia, String izena) {
        eszenatokia.onActivity(jarduera ->
                ((EditText) jarduera.findViewById(R.id.edtIzena)).setText(izena));
        onView(withId(R.id.btnEgiaztatu)).perform(click());
        onView(withId(R.id.txtGaldera))
                .check(matches(withText("Kaixo " + izena + ". Baldintzak onartzen dituzu?")));
        ondo("Intent: '" + izena + "' bidalita; baldintzen galdera zuzena");
    }

    /**
     * Onarpenaren eta baztertzearen emaitzak itzultzen direla egiaztatzen du.
     * @throws Exception kapturak edo txostena gordetzean errorea gertatzen bada
     */
    @Test
    public void komunikazioaEtaKapturak() throws Exception {
        try (ActivityScenario<MainActivity> eszenatokia =
                     ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.edtIzena)).check(matches(isDisplayed()));
            onView(withId(R.id.btnEgiaztatu)).check(matches(isDisplayed()));
            onView(withId(R.id.txtEmaitza)).check(matches(withText("Emaitza:")));
            ondo("Pantaila nagusia: izena, egiaztatu eta emaitza");
            kaptura("01_Hasierako_Pantaila.png");

            eszenatokia.onActivity(jarduera ->
                    ((EditText) jarduera.findViewById(R.id.edtIzena)).setText("Ane"));
            kaptura("02_Ane_Sartuta.png");
            bigarreneraJoan(eszenatokia, "Ane");
            onView(withId(R.id.btnOnartu)).check(matches(isDisplayed()));
            onView(withId(R.id.btnBaztertu)).check(matches(isDisplayed()));
            kaptura("03_Ane_Baldintzak.png");

            onView(withId(R.id.btnOnartu)).perform(click());
            onView(withId(R.id.txtEmaitza))
                    .check(matches(withText("Emaitza: Onartua")));
            ondo("ONARTU: Emaitza: Onartua, jarduera nagusian");
            kaptura("04_Onartua_Emaitza.png");

            bigarreneraJoan(eszenatokia, "Iker");
            kaptura("05_Iker_Baldintzak.png");
            onView(withId(R.id.btnBaztertu)).perform(click());
            onView(withId(R.id.txtEmaitza))
                    .check(matches(withText("Emaitza: Baztertua")));
            ondo("BAZTERTU: Emaitza: Baztertua, jarduera nagusian");
            kaptura("06_Baztertua_Emaitza.png");

            eszenatokia.onActivity(jarduera -> {
                EditText edit = jarduera.findViewById(R.id.edtIzena);
                edit.setText("   ");
                jarduera.findViewById(R.id.btnEgiaztatu).performClick();
                assertNotNull("Izen hutsaren errorea falta da", edit.getError());
                assertTrue("Fokua izenean mantendu behar da", edit.hasFocus());
            });
            onView(withId(R.id.txtEmaitza))
                    .check(matches(withText("Emaitza: Baztertua")));
            ondo("Izen hutsa ez da bigarren jarduerara pasatzen");
            kaptura("07_Izen_Hutsa_Errorea.png");
        }
        gorde("09_Proben_Emaitzak.txt", "text/plain", null,
                txostena.toString().getBytes(StandardCharsets.UTF_8));
    }
}
