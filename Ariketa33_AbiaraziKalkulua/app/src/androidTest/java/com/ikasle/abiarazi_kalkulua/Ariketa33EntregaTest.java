package com.ikasle.abiarazi_kalkulua;

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
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Izenaren bidalketa eta zenbakiaren bikoitza egiaztatzen dituen probak.
 */
@RunWith(AndroidJUnit4.class)
public class Ariketa33EntregaTest {

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
                Environment.DIRECTORY_DOWNLOADS + "/Ariketa33_Entrega_" + runId + "/");
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
     * Bi jardueren arteko komunikazioa eta bikoizketa egiaztatzen ditu.
     * @throws Exception kapturak edo txostena gordetzean errorea gertatzen bada
     */
    @Test
    public void kalkuluaEtaKapturak() throws Exception {
        try (ActivityScenario<ActiKalkulu1> eszenatokia =
                     ActivityScenario.launch(ActiKalkulu1.class)) {
            onView(withId(R.id.edtIzena)).check(matches(isDisplayed()));
            onView(withId(R.id.btnKalkulatu)).check(matches(isDisplayed()));
            onView(withId(R.id.btnIrten)).check(matches(isDisplayed()));
            ondo("Lehen jarduera: bi botoiak eta izenaren eremua");
            kaptura("01_Hasierako_Pantaila.png");

            eszenatokia.onActivity(jarduera ->
                    ((EditText) jarduera.findViewById(R.id.edtIzena)).setText("Ane"));
            onView(withId(R.id.edtIzena)).check(matches(withText("Ane")));
            kaptura("02_Ane_Sartuta.png");
            onView(withId(R.id.btnKalkulatu)).perform(click());
            onView(withId(R.id.txtErabiltzailea))
                    .check(matches(withText("Erabiltzailea: Ane")));
            onView(withId(R.id.txtEmaitza)).check(matches(withText("Emaitza:")));
            ondo("Intent: Ane izena bigarren jardueran agertzen da");
            kaptura("03_Bigarren_Pantaila_Ane.png");

            onView(withId(R.id.btnBikoiztu)).perform(click());
            onView(withId(R.id.edtZenbakia))
                    .check(matches(hasErrorText("Sartu zenbaki bat.")));
            ondo("Zenbaki hutsa: kalkulua gelditu eta errorea erakusten da");
            kaptura("04_Zenbaki_Hutsa_Errorea.png");

            onView(withId(R.id.edtZenbakia)).perform(replaceText("25"), closeSoftKeyboard());
            kaptura("05_Zenbakia_25.png");
            onView(withId(R.id.btnBikoiztu)).perform(click());
            onView(withId(R.id.txtEmaitza))
                    .check(matches(withText("Emaitza: 50")));
            ondo("25 zenbakiaren bikoitza: 50");
            kaptura("06_Bikoitza_50.png");

            onView(withId(R.id.edtZenbakia)).perform(replaceText("-2.5"), closeSoftKeyboard());
            onView(withId(R.id.btnBikoiztu)).perform(click());
            onView(withId(R.id.txtEmaitza))
                    .check(matches(withText("Emaitza: -5")));
            ondo("-2.5 zenbakiaren bikoitza: -5");
            kaptura("07_Bikoitza_Hamartarra.png");

            pressBack();
            onView(withId(R.id.edtIzena)).check(matches(withText("Ane")));
            onView(withId(R.id.btnIrten)).check(matches(isDisplayed()));
            ondo("Atzera: lehen jarduerara itzuli, izena mantenduz");
            kaptura("08_Lehen_Jarduerara_Itzuli.png");

            eszenatokia.onActivity(jarduera -> {
                EditText edit = jarduera.findViewById(R.id.edtIzena);
                edit.setText("  ");
                jarduera.findViewById(R.id.btnKalkulatu).performClick();
                assertNotNull("Izen hutsaren errorea falta da", edit.getError());
            });
            onView(withId(R.id.btnIrten)).check(matches(isDisplayed()));
            ondo("Izen hutsa: errorea eta bigarren jarduerarik ez");
            kaptura("09_Izen_Hutsa_Errorea.png");


        }
        gorde("10_Proben_Emaitzak.txt", "text/plain", null,
                txostena.toString().getBytes(StandardCharsets.UTF_8));
    }
}
