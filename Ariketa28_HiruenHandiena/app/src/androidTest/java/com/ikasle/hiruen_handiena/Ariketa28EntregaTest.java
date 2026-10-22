package com.ikasle.hiruen_handiena;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
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

import static org.junit.Assert.*;

/**
 * Ariketa 28: hiru zenbakien maximoa eta sarrerak, interfaze-egoerak eta kapturak.
 * EditText testuak UI harian sartzen dira; ez da Gboard erabiltzen.
 */
@RunWith(AndroidJUnit4.class)
public class Ariketa28EntregaTest {

    private final StringBuilder txostena = new StringBuilder(
            "Ariketa 28 - Hiruen handiena - Proba automatizatuak\n\n");

    /**
     * Gainditutako proba idazten du.
     *
     * @param mezua testaren deskribapena
     */
    private void ondo(String mezua) {
        txostena.append("OK: ").append(mezua).append('\n');
        System.out.println("OK PROBA: " + mezua);
    }

    /**
     * MediaStore erabiliz fitxategi bat Androiden Download karpetan gordetzen du.
     *
     * @param izena fitxategiaren izena
     * @param mota MIME mota
     * @param irudia PNG irudia, edo null
     * @param datuak testu-edukia, edo null
     * @throws IOException ezin bada gorde
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
                Environment.DIRECTORY_DOWNLOADS + "/Ariketa28_Entrega_" + runId + "/");
        balioak.put(MediaStore.MediaColumns.IS_PENDING, 1);
        Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, balioak);
        if (uri == null) throw new IOException("MediaStore ezin izan da sortu: " + izena);
        boolean eginda = false;
        try {
            try (OutputStream stream = resolver.openOutputStream(uri)) {
                if (stream == null) throw new IOException("Stream-a falta da: " + izena);
                if (irudia != null) {
                    if (!irudia.compress(Bitmap.CompressFormat.PNG, 100, stream))
                        throw new IOException("PNG ezin izan da sortu: " + izena);
                } else {
                    if (datuak == null) throw new IOException("Daturik ez: " + izena);
                    stream.write(datuak);
                }
            }
            ContentValues amaituta = new ContentValues();
            amaituta.put(MediaStore.MediaColumns.IS_PENDING, 0);
            if (resolver.update(uri, amaituta, null, null) < 1)
                throw new IOException("MediaStore ezin izan da argitaratu: " + izena);
            eginda = true;
        } finally {
            if (!eginda) resolver.delete(uri, null, null);
        }
    }

    /**
     * Pantaila osoa PNG gisa gordetzen du.
     *
     * @param izena kapturaren izena
     * @throws Exception ezin bada kapturatu edo idatzi
     */
    private void kaptura(String izena) throws Exception {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        Thread.sleep(350);
        Bitmap bitmap = InstrumentationRegistry.getInstrumentation()
                .getUiAutomation().takeScreenshot();
        assertNotNull("Pantaila ez dago erabilgarri", bitmap);
        try { gorde(izena, "image/png", bitmap, null); }
        finally { bitmap.recycle(); }
    }

    /**
     * Hiru zenbakien maximoa, metodoaren deia eta interfazea probatzen ditu.
     *
     * @param eszenatokia proban dagoen jarduera
     * @param a lehenengo zenbakia
     * @param b bigarren zenbakia
     * @param c hirugarren zenbakia
     * @param espero espero den handiena
     * @param fitxategia kapturaren izena, edo null
     * @throws Exception kapturaren hutsegitea
     */
    private void kasua(ActivityScenario<MainActivity> eszenatokia,
                       long a, long b, long c, long espero, String fitxategia) throws Exception {
        eszenatokia.onActivity(jarduera -> {
            EditText e1 = jarduera.findViewById(R.id.edtLehena);
            EditText e2 = jarduera.findViewById(R.id.edtBigarrena);
            EditText e3 = jarduera.findViewById(R.id.edtHirugarrena);
            TextView emaitza = jarduera.findViewById(R.id.txtEmaitza);
            e1.setText(Long.toString(a));
            e2.setText(Long.toString(b));
            e3.setText(Long.toString(c));
            assertEquals(View.GONE, emaitza.getVisibility());
            assertEquals("hiruen_handiena metodoa", espero,
                    jarduera.hiruen_handiena(a, b, c));
            jarduera.findViewById(R.id.btnErakutsi).performClick();
            assertEquals(jarduera.getString(R.string.emaitza_handiena, espero),
                    emaitza.getText().toString());
            assertEquals(View.VISIBLE, emaitza.getVisibility());
        });
        ondo(a + ", " + b + ", " + c + " -> " + espero);
        if (fitxategia != null) kaptura(fitxategia);
    }

    /**
     * Sarrera hutsa edo zenbaki baliogabea behar bezala baztertzen dela egiaztatzen du.
     *
     * @param eszenatokia proban dagoen jarduera
     * @param a lehenengo sarrera
     * @param b bigarren sarrera
     * @param c hirugarren sarrera
     * @param erroreIndizea errorea eduki behar duen eremuaren indizea (0-2)
     * @param fitxategia kapturaren izena, edo null
     * @throws Exception kapturaren hutsegitea
     */
    private void baliogabea(ActivityScenario<MainActivity> eszenatokia,
                            String a, String b, String c, int erroreIndizea,
                            String fitxategia) throws Exception {
        eszenatokia.onActivity(jarduera -> {
            EditText[] eremuak = {
                    jarduera.findViewById(R.id.edtLehena),
                    jarduera.findViewById(R.id.edtBigarrena),
                    jarduera.findViewById(R.id.edtHirugarrena)
            };
            eremuak[0].setText(a);
            eremuak[1].setText(b);
            eremuak[2].setText(c);
            jarduera.findViewById(R.id.btnErakutsi).performClick();
            assertNotNull("Errorea falta da", eremuak[erroreIndizea].getError());
            assertTrue("Fokua falta da", eremuak[erroreIndizea].hasFocus());
            TextView emaitza = jarduera.findViewById(R.id.txtEmaitza);
            assertEquals(View.GONE, emaitza.getVisibility());
        });
        ondo("Sarrera baliogabea: '" + a + "', '" + b + "', '" + c + "'");
        if (fitxategia != null) kaptura(fitxategia);
    }

    /**
     * Moodleko eskakizunak eta ertzeko kasuak modu automatikoan probatzen ditu.
     *
     * @throws Exception ezin bada fitxategia gorde
     */
    @Test
    public void maximoaBaliozkotzeaEtaKapturak() throws Exception {
        try (ActivityScenario<MainActivity> eszenatokia =
                     ActivityScenario.launch(MainActivity.class)) {
            eszenatokia.onActivity(jarduera -> {
                assertNotNull(jarduera.findViewById(R.id.edtLehena));
                assertNotNull(jarduera.findViewById(R.id.edtBigarrena));
                assertNotNull(jarduera.findViewById(R.id.edtHirugarrena));
                assertNotNull(jarduera.findViewById(R.id.btnErakutsi));
                TextView emaitza = jarduera.findViewById(R.id.txtEmaitza);
                assertEquals(View.GONE, emaitza.getVisibility());
            });
            kaptura("01_Hasierako_Pantaila.png");
            ondo("Hasierako pantaila: hiru eremu, botoia eta emaitza ezkutuan");

            kasua(eszenatokia, 9, 4, 2, 9, "02_Lehena_Handiena.png");
            kasua(eszenatokia, 4, 9, 2, 9, "03_Bigarrena_Handiena.png");
            kasua(eszenatokia, 4, 2, 9, 9, "04_Hirugarrena_Handiena.png");
            kasua(eszenatokia, 7, 7, 3, 7, "05_Berdinketa.png");
            kasua(eszenatokia, 5, 5, 5, 5, null);
            kasua(eszenatokia, -3, -8, -1, -1, "06_Negatiboak.png");
            kasua(eszenatokia, Long.MAX_VALUE, 0, -1, Long.MAX_VALUE, null);
            kasua(eszenatokia, Long.MIN_VALUE, -2, -3, -2, null);
            kasua(eszenatokia, 0, 0, 0, 0, null);

            baliogabea(eszenatokia, "1", "", "3", 1, "07_Eremu_Hutsa.png");
            baliogabea(eszenatokia, "1", "2", "3.5", 2, "08_Hamartar_Errorea.png");
            baliogabea(eszenatokia, "abc", "2", "3", 0, null);
            baliogabea(eszenatokia, "1", "9223372036854775808", "3", 1, null);
            baliogabea(eszenatokia, "1", "-9223372036854775809", "3", 1, null);
        }
        gorde("09_Proben_Emaitzak.txt", "text/plain", null,
                txostena.toString().getBytes(StandardCharsets.UTF_8));
    }
}
