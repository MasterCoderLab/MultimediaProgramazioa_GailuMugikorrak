package com.ikasle.ikaslearenkalifikazioa;

import android.graphics.Bitmap;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.OutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Ariketa 26ren amaierako egiaztapen instrumentatuak.
 *
 * Testuek EditText.setText() erabiltzen dute UI harian,
 * Gboard edo ADB keyevent bidezko karaktere-aldaketak saihesteko.
 */
@RunWith(AndroidJUnit4.class)
public class Ariketa26EntregaTest {

    private static final String IKASLEA = "Ane";
    private final StringBuilder emaitzak = new StringBuilder(
            "Ariketa 26 - Android instrumentazio proben emaitzak\n\n");

    /**
     * Egoera bakoitzaren ondorengo emaitza gordetzen du.
     *
     * @param mezua frogatutako portaeraren azalpena
     */
    private void egiaztatuta(String mezua) {
        emaitzak.append("OK: ").append(mezua).append('\n');
        System.out.println("OK PROBA: " + mezua);
    }

    /**
     * Ikaslearen izena eta nota UI hari nagusian idazten ditu.
     *
     * @param eszenatokia jarduera aktiboaren erreferentzia
     * @param ikaslea ikaslearen izena
     * @param nota ikaslearen kalifikazio numerikoa
     */
    private void bete(ActivityScenario<MainActivity> eszenatokia,
                      String ikaslea, String nota) {
        eszenatokia.onActivity(jarduera -> {
            EditText izena = jarduera.findViewById(R.id.edtIkaslea);
            EditText notaEremua = jarduera.findViewById(R.id.edtNota);
            izena.setText(ikaslea);
            notaEremua.setText(nota);
            assertEquals(ikaslea, izena.getText().toString());
            assertEquals(nota, notaEremua.getText().toString());
            Button garbitu = jarduera.findViewById(R.id.btnGarbitu);
            assertEquals(!ikaslea.isEmpty() || !nota.isEmpty(), garbitu.isEnabled());
        });
    }

    /**
     * Proba bakoitzaren fitxategiak Androiden Download karpetan
     * gordetzen ditu MediaStore erabiliz. Karpeta publikoak
     * probaren ondorengo aplikazioaren desinstalazioa jasaten du.
     *
     * @param izena fitxategiaren izena
     * @param mota MIME motaren izena
     * @param bitmap pantaila irudia edo null testuaren kasuan
     * @param edukia testu fitxategiaren edukia edo null irudiaren kasuan
     * @throws IOException fitxategia ezin bada gorde
     */
    private void gordePublikoki(String izena, String mota,
                                Bitmap bitmap, byte[] edukia) throws IOException {
        String runId = InstrumentationRegistry.getArguments().getString("runId");
        assertNotNull("runId ez da iritsi instrumentaziora", runId);
        assertTrue("runId baliogabea", runId.matches("[0-9_]+"));

        ContentResolver resolver = InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getContentResolver();
        ContentValues balioak = new ContentValues();
        balioak.put(MediaStore.MediaColumns.DISPLAY_NAME, izena);
        balioak.put(MediaStore.MediaColumns.MIME_TYPE, mota);
        balioak.put(MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + "/Ariketa26_Entrega_" + runId + "/");
        balioak.put(MediaStore.MediaColumns.IS_PENDING, 1);

        Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, balioak);
        if (uri == null) {
            throw new IOException("Ezin da MediaStore erregistroa sortu: " + izena);
        }

        boolean ondo = false;
        try {
            try (OutputStream irteera = resolver.openOutputStream(uri)) {
                if (irteera == null) {
                    throw new IOException("Irteera korrontea ez da ireki: " + izena);
                }
                if (bitmap != null) {
                    if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, irteera)) {
                        throw new IOException("PNG konpresioak huts egin du: " + izena);
                    }
                } else {
                    if (edukia == null) {
                        throw new IOException("Ez dago edukirik: " + izena);
                    }
                    irteera.write(edukia);
                }
            }
            ContentValues bukatuta = new ContentValues();
            bukatuta.put(MediaStore.MediaColumns.IS_PENDING, 0);
            if (resolver.update(uri, bukatuta, null, null) < 1) {
                throw new IOException("Fitxategia ez da argitaratu: " + izena);
            }
            ondo = true;
        } finally {
            if (!ondo) {
                resolver.delete(uri, null, null);
            }
        }
    }

    /**
     * Uneko Android pantailaren PNGa gordetzen du.
     *
     * @param izena kapturaren izena
     * @throws Exception irudia ezin bada gorde
     */
    private void kaptura(String izena) throws Exception {
        Espresso.onIdle();
        Thread.sleep(350);
        Bitmap bitmap = InstrumentationRegistry.getInstrumentation()
                .getUiAutomation().takeScreenshot();
        assertNotNull("Pantaila ezin izan da kapturatu: " + izena, bitmap);
        try {
            gordePublikoki(izena, "image/png", bitmap, null);
        } finally {
            bitmap.recycle();
        }
        System.out.println("OK CAPTURA: " + izena);
    }

    /**
     * Sailkapena eta emaitzaren AlertDialog-a egiaztatzen ditu.
     *
     * @param eszenatokia jarduera aktiboa
     * @param nota probatu beharreko nota
     * @param kalifikazioa espero den maila
     * @param argazkia kaptura izena edo null
     * @throws Exception kaptura huts egiten badu
     */
    private void baliozkoa(ActivityScenario<MainActivity> eszenatokia,
                           String nota, String kalifikazioa, String argazkia) throws Exception {
        bete(eszenatokia, IKASLEA, nota);
        eszenatokia.onActivity(jarduera ->
                jarduera.findViewById(R.id.btnErakutsi).performClick());
        String mezua = InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getString(R.string.emaitzaren_mezua,
                        IKASLEA, nota, kalifikazioa);
        onView(withText(mezua)).inRoot(isDialog()).check(matches(isDisplayed()));
        egiaztatuta("Nota " + nota + " -> " + kalifikazioa);
        if (argazkia != null) {
            kaptura(argazkia);
        }
        onView(withText(R.string.adostu)).inRoot(isDialog()).perform(click());
    }

    /**
     * Sarrera baliogabea gelditzen dela eta dagokion eremua
     * errorean dagoela frogatzen du.
     *
     * @param eszenatokia jarduera aktiboa
     * @param ikaslea sartutako izena
     * @param nota sartutako nota
     * @param ikasleErrorea true, izena bada oker dagoen eremua
     * @param argazkia kaptura izena edo null
     * @throws Exception kaptura huts egiten badu
     */
    private void baliogabea(ActivityScenario<MainActivity> eszenatokia,
                            String ikaslea, String nota, boolean ikasleErrorea,
                            String argazkia) throws Exception {
        bete(eszenatokia, ikaslea, nota);
        eszenatokia.onActivity(jarduera -> {
            jarduera.findViewById(R.id.btnErakutsi).performClick();
            EditText eremua = jarduera.findViewById(
                    ikasleErrorea ? R.id.edtIkaslea : R.id.edtNota);
            assertNotNull("Baliozkotze-errorea falta da", eremua.getError());
            assertTrue("Fokua ez da errore-eremuan jarri", eremua.hasFocus());
        });
        egiaztatuta("Balio baliogabea: " + ikaslea + ", " + nota);
        if (argazkia != null) {
            kaptura(argazkia);
        }
    }

    /**
     * Noten mugak, baliozkotzea, GARBITU eta irudiak egiaztatzen ditu.
     *
     * @throws Exception kaptura edo fitxategi-idazketa huts egiten bada
     */
    @Test
    public void notaGuztiakEtaKapturak() throws Exception {
        try (ActivityScenario<MainActivity> eszenatokia =
                     ActivityScenario.launch(MainActivity.class)) {
            eszenatokia.onActivity(jarduera -> {
                assertNotNull(jarduera.findViewById(R.id.imgIkaslea));
                assertNotNull(jarduera.findViewById(R.id.edtIkaslea));
                assertNotNull(jarduera.findViewById(R.id.edtNota));
                assertNotNull(jarduera.findViewById(R.id.btnErakutsi));
                assertFalse(jarduera.<Button>findViewById(R.id.btnGarbitu).isEnabled());
            });
            kaptura("01_Pantaila_Nagusia.png");
            egiaztatuta("Hasierako kontrolak eta GARBITU desgaituta");

            baliozkoa(eszenatokia, "0", "Txarra", null);
            baliozkoa(eszenatokia, "8", "Txarra", "02_Txarra_8.png");
            baliozkoa(eszenatokia, "10.49", "Txarra", null);
            baliozkoa(eszenatokia, "10.5", "Erdipurdikoa", "03_Erdipurdikoa_10_5.png");
            baliozkoa(eszenatokia, "13.99", "Erdipurdikoa", null);
            baliozkoa(eszenatokia, "14", "Ona", "04_Ona_14.png");
            baliozkoa(eszenatokia, "17.99", "Ona", null);
            baliozkoa(eszenatokia, "18", "Oso Ona", null);
            baliozkoa(eszenatokia, "18.5", "Oso Ona", "05_Oso_Ona_18_5.png");
            baliozkoa(eszenatokia, "18.99", "Oso Ona", null);
            baliozkoa(eszenatokia, "19", "Bikaina", "06_Bikaina_19.png");
            baliozkoa(eszenatokia, "19.99", "Bikaina", null);
            baliozkoa(eszenatokia, "20", "Bikaina", null);

            baliogabea(eszenatokia, IKASLEA, "-1", false, null);
            baliogabea(eszenatokia, IKASLEA, "20.01", false, null);
            baliogabea(eszenatokia, IKASLEA, "21", false, "07_Errorea_21.png");
            baliogabea(eszenatokia, "", "12", true, null);
            baliogabea(eszenatokia, IKASLEA, "", false, null);

            bete(eszenatokia, IKASLEA, "18.5");
            eszenatokia.onActivity(jarduera -> {
                jarduera.findViewById(R.id.btnGarbitu).performClick();
                EditText izena = jarduera.findViewById(R.id.edtIkaslea);
                EditText nota = jarduera.findViewById(R.id.edtNota);
                assertEquals("", izena.getText().toString());
                assertEquals("", nota.getText().toString());
                assertFalse(jarduera.<Button>findViewById(R.id.btnGarbitu).isEnabled());
                assertTrue(izena.hasFocus());
            });
            egiaztatuta("GARBITU: bi eremuak garbi eta fokua izenean");
            kaptura("08_Garbitu_Ondoren.png");
        }
        gordePublikoki("09_Proben_Emaitzak.txt", "text/plain", null,
                emaitzak.toString().getBytes(StandardCharsets.UTF_8));
    }
}
