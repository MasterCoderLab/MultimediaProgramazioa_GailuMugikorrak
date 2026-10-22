package com.ikasle.urte_bisurtea;

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
 * Ariketa 27: bisurtearen logika, sarrerak, interfaze-egoerak eta kapturak.
 * EditText testuak UI harian sartzen dira; ez da Gboard erabiltzen.
 */
@RunWith(AndroidJUnit4.class)
public class Ariketa27EntregaTest {

    private final StringBuilder txostena = new StringBuilder(
            "Ariketa 27 - Urte bisurtea - Proba automatizatuak\n\n");

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
                Environment.DIRECTORY_DOWNLOADS + "/Ariketa27_Entrega_" + runId + "/");
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
     * Urtearen emaitza eta metodoaren logika egiaztatzen ditu.
     *
     * @param eszenatokia uneko jarduera
     * @param urtea sarrerako urtea
     * @param espero espero den bisurte egoera
     * @param fitxategia kapturaren izena, edo null
     * @throws Exception kaptura huts egiten badu
     */
    private void baliozkoa(ActivityScenario<MainActivity> eszenatokia,
                           int urtea, boolean espero, String fitxategia) throws Exception {
        eszenatokia.onActivity(jarduera -> {
            EditText eremua = jarduera.findViewById(R.id.edtUrtea);
            TextView emaitza = jarduera.findViewById(R.id.txtEmaitza);
            eremua.setText(Integer.toString(urtea));
            assertTrue("Aurreko emaitza ez da ezabatu", emaitza.getText().length() == 0);
            assertEquals("bisurtea_da metodoa", espero, jarduera.bisurtea_da(urtea));
            jarduera.findViewById(R.id.btnEgiaztatu).performClick();
            int resId = espero ? R.string.emaitza_bisurtea : R.string.emaitza_ez_bisurtea;
            assertEquals(jarduera.getString(resId, urtea), emaitza.getText().toString());
            assertEquals(View.VISIBLE, emaitza.getVisibility());
        });
        ondo("Urtea " + urtea + " -> " + (espero ? "bisurtea" : "ez da bisurtea"));
        if (fitxategia != null) kaptura(fitxategia);
    }

    /**
     * Sarrera okerrak errore gisa agertzen direla egiaztatzen du.
     *
     * @param eszenatokia uneko jarduera
     * @param sarrera testu baliogabea
     * @param fitxategia kaptura izena edo null
     * @throws Exception kaptura huts egiten badu
     */
    private void baliogabea(ActivityScenario<MainActivity> eszenatokia,
                            String sarrera, String fitxategia) throws Exception {
        eszenatokia.onActivity(jarduera -> {
            EditText eremua = jarduera.findViewById(R.id.edtUrtea);
            TextView emaitza = jarduera.findViewById(R.id.txtEmaitza);
            eremua.setText(sarrera);
            jarduera.findViewById(R.id.btnEgiaztatu).performClick();
            assertNotNull("Sarrera baliogabearen errorea falta da", eremua.getError());
            assertTrue("Fokua falta da", eremua.hasFocus());
            assertEquals(View.GONE, emaitza.getVisibility());
        });
        ondo("Sarrera baliogabea: '" + sarrera + "'");
        if (fitxategia != null) kaptura(fitxategia);
    }

    /**
     * Irakaslearen eskakizunak eta ertzeko kasuak frogatzen ditu.
     *
     * @throws Exception kaptura edo fitxategi idazketa huts egiten bada
     */
    @Test
    public void bisurteakBaliozkotzeaEtaKapturak() throws Exception {
        try (ActivityScenario<MainActivity> eszenatokia =
                     ActivityScenario.launch(MainActivity.class)) {
            eszenatokia.onActivity(jarduera -> {
                assertNotNull(jarduera.findViewById(R.id.edtUrtea));
                assertNotNull(jarduera.findViewById(R.id.btnEgiaztatu));
                TextView emaitza = jarduera.findViewById(R.id.txtEmaitza);
                assertEquals(View.GONE, emaitza.getVisibility());
            });
            kaptura("01_Hasierako_Pantaila.png");
            ondo("Hasierako pantaila: kontrolak eta emaitza ezkutuan");

            baliozkoa(eszenatokia, 2024, true, "02_Bisurtea_2024.png");
            baliozkoa(eszenatokia, 2025, false, "03_Ez_Bisurtea_2025.png");
            baliozkoa(eszenatokia, 1900, false, "04_Mendea_1900.png");
            baliozkoa(eszenatokia, 2000, true, "05_Mendea_2000.png");
            baliozkoa(eszenatokia, 2100, false, null);
            baliozkoa(eszenatokia, 2400, true, null);
            baliozkoa(eszenatokia, 4, true, null);
            baliozkoa(eszenatokia, 100, false, null);
            baliozkoa(eszenatokia, 400, true, null);
            baliozkoa(eszenatokia, 1, false, null);

            baliogabea(eszenatokia, "0", "06_Errorea_0.png");
            baliogabea(eszenatokia, "-5", null);
            baliogabea(eszenatokia, "", null);
            baliogabea(eszenatokia, "abc", null);
            baliogabea(eszenatokia, "2147483648", null);
        }
        gorde("09_Proben_Emaitzak.txt", "text/plain", null,
                txostena.toString().getBytes(StandardCharsets.UTF_8));
    }
}
