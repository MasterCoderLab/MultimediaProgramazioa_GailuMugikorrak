package com.ikasle.beza_kalkulatu;

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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Produktuaren bidalketa, BEZ tasak eta balidazioak egiaztatzeko probak.
 */
@RunWith(AndroidJUnit4.class)
public class Ariketa34EntregaTest {

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
                Environment.DIRECTORY_DOWNLOADS + "/Ariketa34_Entrega_" + runId + "/");
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
    /**
     * Bi jardueren arteko komunikazioa, hiru BEZak eta datuen balidazioak probatzen ditu.
     *
     * @throws Exception kapturak edo txostena gorde ezin badira
     */
    @Test
    public void bezKalkuluaEtaKapturak() throws Exception {
        try (ActivityScenario<ActProduktu_Produktua> eszenatokia =
                     ActivityScenario.launch(ActProduktu_Produktua.class)) {
            onView(withId(R.id.edtProduktua)).check(matches(isDisplayed()));
            onView(withId(R.id.edtZenbatekoa)).check(matches(isDisplayed()));
            onView(withId(R.id.btnBEZ)).check(matches(isDisplayed()));
            ondo("Lehen jarduera: produktuaren izena, zenbatekoa eta +BEZ botoia");
            kaptura("01_Hasierako_Pantaila.png");

            eszenatokia.onActivity(jarduera -> {
                ((EditText) jarduera.findViewById(R.id.edtProduktua)).setText("Liburua");
                ((EditText) jarduera.findViewById(R.id.edtZenbatekoa)).setText("100");
            });
            ondo("Produktuaren datuak: Liburua eta 100 euro");
            kaptura("02_Produktua_Eta_Zenbatekoa.png");

            onView(withId(R.id.btnBEZ)).perform(click());
            onView(withId(R.id.txtProduktua))
                    .check(matches(withText("Produktua: Liburua")));
            onView(withId(R.id.rb4)).check(matches(isDisplayed()));
            onView(withId(R.id.rb10)).check(matches(isDisplayed()));
            onView(withId(R.id.rb21)).check(matches(isDisplayed()));
            onView(withId(R.id.txtGuztira)).check(matches(withText("Guztira:")));
            ondo("Intent: produktua eta hasierako zenbatekoa bigarren jarduerara");
            kaptura("03_BEZ_Aukerak.png");

            onView(withId(R.id.rb4)).perform(click());
            onView(withId(R.id.txtGuztira)).check(matches(withText("Guztira: 104,00 €")));
            ondo("100 euro + %4 BEZ = 104,00 euro");
            kaptura("04_BEZ_4_Emaitza.png");

            onView(withId(R.id.rb10)).perform(click());
            onView(withId(R.id.txtGuztira)).check(matches(withText("Guztira: 110,00 €")));
            ondo("100 euro + %10 BEZ = 110,00 euro");
            kaptura("05_BEZ_10_Emaitza.png");

            onView(withId(R.id.rb21)).perform(click());
            onView(withId(R.id.txtGuztira)).check(matches(withText("Guztira: 121,00 €")));
            ondo("100 euro + %21 BEZ = 121,00 euro");
            kaptura("06_BEZ_21_Emaitza.png");

            pressBack();
            onView(withId(R.id.edtProduktua)).check(matches(withText("Liburua")));
            onView(withId(R.id.edtZenbatekoa)).check(matches(withText("100")));
            ondo("Atzera: hasierako datuak mantentzen dira");

            eszenatokia.onActivity(jarduera -> {
                EditText produktua = jarduera.findViewById(R.id.edtProduktua);
                EditText zenbatekoa = jarduera.findViewById(R.id.edtZenbatekoa);
                produktua.setText(" ");
                produktua.setError(null);
                zenbatekoa.setText("100");
                zenbatekoa.setError(null);
            });
            onView(withId(R.id.btnBEZ)).perform(click());
            onView(withId(R.id.edtProduktua))
                    .check(matches(hasErrorText("Sartu produktuaren izena.")));
            ondo("Produktuaren izena hutsik: ez da bigarren jarduerara joaten");
            kaptura("07_Produktu_Hutsa_Errorea.png");

            eszenatokia.onActivity(jarduera -> {
                EditText produktua = jarduera.findViewById(R.id.edtProduktua);
                EditText zenbatekoa = jarduera.findViewById(R.id.edtZenbatekoa);
                produktua.setText("Liburua");
                produktua.setError(null);
                zenbatekoa.setText("");
                zenbatekoa.setError(null);
            });
            onView(withId(R.id.btnBEZ)).perform(click());
            onView(withId(R.id.edtZenbatekoa))
                    .check(matches(hasErrorText("Sartu zenbatekoa.")));
            ondo("Zenbatekoa hutsik: errorea erakusten da");
            kaptura("08_Zenbateko_Hutsa_Errorea.png");

            eszenatokia.onActivity(jarduera -> {
                EditText zenbatekoa = jarduera.findViewById(R.id.edtZenbatekoa);
                zenbatekoa.setText("-5");
                zenbatekoa.setError(null);
            });
            onView(withId(R.id.btnBEZ)).perform(click());
            onView(withId(R.id.edtZenbatekoa))
                    .check(matches(hasErrorText("Sartu baliozko zenbatekoa.")));
            ondo("Zenbateko negatiboa: errorea erakusten da");
            kaptura("09_Zenbateko_Negatiboa.png");

            eszenatokia.onActivity(jarduera -> {
                EditText zenbatekoa = jarduera.findViewById(R.id.edtZenbatekoa);
                zenbatekoa.setText("abc");
                zenbatekoa.setError(null);
            });
            onView(withId(R.id.btnBEZ)).perform(click());
            onView(withId(R.id.edtZenbatekoa))
                    .check(matches(hasErrorText("Sartu baliozko zenbatekoa.")));
            ondo("Zenbateko ez-numerikoa: errorea erakusten da");
            kaptura("10_Zenbateko_EzNumerikoa.png");
        }
        gorde("11_Proben_Emaitzak.txt", "text/plain", null,
                txostena.toString().getBytes(StandardCharsets.UTF_8));
    }
}
