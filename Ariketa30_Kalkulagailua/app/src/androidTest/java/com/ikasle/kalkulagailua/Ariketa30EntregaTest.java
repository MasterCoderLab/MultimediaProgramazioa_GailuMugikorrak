package com.ikasle.kalkulagailua;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
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
 * Kalkulagailuaren lau eragiketak, 0, erroreak eta garbiketa egiaztatzen ditu.
 * UI sarrerak hari nagusitik aldatzen dira, Gboard erabili gabe.
 */
@RunWith(AndroidJUnit4.class)
public class Ariketa30EntregaTest {

    private final StringBuilder txostena = new StringBuilder(
            "Ariketa 30 - RadioGroup kalkulagailua - Proba automatizatuak\n\n");

    /**
     * Gainditutako proba txostenean erregistratzen du.
     * @param mezua probaren azalpena
     */
    private void ondo(String mezua) {
        txostena.append("OK: ").append(mezua).append('\n');
        System.out.println("OK PROBA: " + mezua);
    }

    /**
     * Android Download partekatuan fitxategia gordetzen du MediaStore-rekin.
     * @param izena fitxategiaren izena
     * @param mota MIME mota
     * @param irudia PNGaren bitmap-a, edo null
     * @param datuak testu-fitxategiaren edukia, edo null
     * @throws IOException gordetzeak huts egiten badu
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
                Environment.DIRECTORY_DOWNLOADS + "/Ariketa30_Entrega_" + runId + "/");
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
     * Pantailaren PNG kaptura sortu eta partekatutako karpetan gordetzen du.
     * @param izena kapturaren fitxategia
     * @throws Exception irudia ezin bada sortu
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
     * Sarrerak prestatu, eragiketa hautatu eta emaitza ziurtatzen du.
     * @param eszenatokia ActivityScenario
     * @param a lehen zenbakia
     * @param b bigarren zenbakia
     * @param radio eragiketaren RadioButton IDa
     * @param espero pantailan agertu behar duen emaitza
     * @param izena kapturaren izena edo null
     * @throws Exception kapturak huts egiten badu
     */
    private void kasua(ActivityScenario<MainActivity> eszenatokia,
            String a, String b, int radio, String espero, String izena) throws Exception {
        eszenatokia.onActivity(jarduera -> {
            EditText e1 = jarduera.findViewById(R.id.edtNum1);
            EditText e2 = jarduera.findViewById(R.id.edtNum2);
            RadioGroup eragiketak = jarduera.findViewById(R.id.rdgEragiketak);
            TextView emaitza = jarduera.findViewById(R.id.txtEmaitza);
            e1.setText(a);
            e2.setText(b);
            eragiketak.check(radio);
            assertEquals("Eragiketa hautatuta", radio,
                    eragiketak.getCheckedRadioButtonId());
            jarduera.findViewById(R.id.btnKalkulatu).performClick();
            assertEquals(View.VISIBLE, emaitza.getVisibility());
            assertEquals("Emaitza okerra", "Emaitza: " + espero,
                    emaitza.getText().toString());
            assertNull("1. eremuak ez luke errorerik izan behar", e1.getError());
            assertNull("2. eremuak ez luke errorerik izan behar", e2.getError());
        });
        ondo(a + " eta " + b + ", " + radio + " -> " + espero);
        if (izena != null) kaptura(izena);
    }

    /**
     * Eremu huts edo baliogabe batek akatsa eta fokua jasotzen dituela ziurtatu.
     * @param eszenatokia ActivityScenario
     * @param a lehen testua
     * @param b bigarren testua
     * @param okerra errorea jaso behar duen IDa
     * @param izena kaptura edo null
     * @throws Exception kaptura ezin bada sortu
     */
    private void sarreraOkerra(ActivityScenario<MainActivity> eszenatokia,
            String a, String b, int okerra, String izena) throws Exception {
        eszenatokia.onActivity(jarduera -> {
            EditText e1 = jarduera.findViewById(R.id.edtNum1);
            EditText e2 = jarduera.findViewById(R.id.edtNum2);
            e1.setText(a);
            e2.setText(b);
            RadioGroup rdg = jarduera.findViewById(R.id.rdgEragiketak);
            rdg.check(R.id.rdbBatuketa);
            jarduera.findViewById(R.id.btnKalkulatu).performClick();
            EditText helburua = jarduera.findViewById(okerra);
            assertNotNull("Errore-mezua falta da", helburua.getError());
            assertTrue("Fokua eremu okerrean behar da", helburua.hasFocus());
            assertEquals(View.GONE,
                    jarduera.findViewById(R.id.txtEmaitza).getVisibility());
        });
        ondo("Sarrera baliogabea: '" + a + "' eta '" + b + "'");
        if (izena != null) kaptura(izena);
    }

    /**
     * Metodo originalak, botoiak, eremuak eta errore kasuak probatzen ditu.
     * @throws Exception probetako fitxategiak ezin badira gorde
     */
    @Test
    public void lauEragiketakRadioGroupEtaKapturak() throws Exception {
        try (ActivityScenario<MainActivity> eszenatokia =
                     ActivityScenario.launch(MainActivity.class)) {
            eszenatokia.onActivity(jarduera -> {
                assertNotNull(jarduera.findViewById(R.id.rdbBatuketa));
                assertNotNull(jarduera.findViewById(R.id.rdbKenketa));
                assertNotNull(jarduera.findViewById(R.id.rdbBiderketa));
                assertNotNull(jarduera.findViewById(R.id.rdbZatiketa));
                assertNotNull(jarduera.findViewById(R.id.edtNum1));
                assertNotNull(jarduera.findViewById(R.id.edtNum2));
                assertNotNull(jarduera.findViewById(R.id.btnGarbitu));
                assertNotNull(jarduera.findViewById(R.id.btnKalkulatu));
                assertEquals(-1,
                        ((RadioGroup) jarduera.findViewById(R.id.rdgEragiketak))
                                .getCheckedRadioButtonId());
                assertEquals(View.GONE,
                        jarduera.findViewById(R.id.txtEmaitza).getVisibility());
                // Irakaslearen lau metodo originalen logika zuzenean probatuta.
                assertEquals(5.0, jarduera.sumar(0, 5), 0.0);
                assertEquals(-5.0, jarduera.restar(2, 7), 0.0);
                assertEquals(12.0, jarduera.multiplicar(3, 4), 0.0);
                assertEquals(3.5, jarduera.dividir(7, 2), 0.0);
            });
            ondo("Hasierako egoera eta lau metodo originalak");
            kaptura("01_Hasierako_Pantaila.png");

            kasua(eszenatokia, "0", "5", R.id.rdbBatuketa, "5", "02_Batuketa_0_5.png");
            kasua(eszenatokia, "2", "7", R.id.rdbKenketa, "-5", "03_Kenketa_2_7.png");
            kasua(eszenatokia, "3", "4", R.id.rdbBiderketa, "12", "04_Biderketa_3_4.png");
            kasua(eszenatokia, "7", "2", R.id.rdbZatiketa, "3,5", "05_Zatiketa_7_2.png");
            kasua(eszenatokia, "0", "5", R.id.rdbBiderketa, "0", null);
            kasua(eszenatokia, "5", "0", R.id.rdbKenketa, "5", null);
            kasua(eszenatokia, "0", "4", R.id.rdbZatiketa, "0", null);

            // 0 zenbakia bigarrenean: zatiketan bakarrik errorea.
            eszenatokia.onActivity(jarduera -> {
                ((EditText) jarduera.findViewById(R.id.edtNum1)).setText("8");
                EditText b = jarduera.findViewById(R.id.edtNum2);
                b.setText("0");
                ((RadioGroup) jarduera.findViewById(R.id.rdgEragiketak))
                        .check(R.id.rdbZatiketa);
                jarduera.findViewById(R.id.btnKalkulatu).performClick();
                assertNotNull("Zatiketa zeroren artean debekatua", b.getError());
                assertTrue("Bigarren eremuan fokua", b.hasFocus());
                assertEquals(View.GONE,
                        jarduera.findViewById(R.id.txtEmaitza).getVisibility());
            });
            ondo("8 / 0: errorea eta fokua 2. zenbakian");
            kaptura("06_Zatiketa_0_Errorea.png");

            // Bi balio daude baina eragiketarik ez da hautatu.
            eszenatokia.onActivity(jarduera -> {
                ((EditText) jarduera.findViewById(R.id.edtNum1)).setText("8");
                ((EditText) jarduera.findViewById(R.id.edtNum2)).setText("2");
                ((RadioGroup) jarduera.findViewById(R.id.rdgEragiketak)).clearCheck();
                jarduera.findViewById(R.id.btnKalkulatu).performClick();
                TextView emaitza = jarduera.findViewById(R.id.txtEmaitza);
                assertEquals(View.VISIBLE, emaitza.getVisibility());
                assertEquals(jarduera.getString(R.string.errorea_eragiketa),
                        emaitza.getText().toString());
            });
            ondo("Eragiketa hautatu gabe: abisu zuzena");
            kaptura("07_Eragiketa_Gabe.png");

            sarreraOkerra(eszenatokia, "", "5", R.id.edtNum1, null);
            sarreraOkerra(eszenatokia, "5", "", R.id.edtNum2, null);
            sarreraOkerra(eszenatokia, "-1", "5", R.id.edtNum1, null);
            sarreraOkerra(eszenatokia, "1.5", "5", R.id.edtNum1, null);
            sarreraOkerra(eszenatokia, "3", "abc", R.id.edtNum2, null);
            sarreraOkerra(eszenatokia, "9223372036854775808", "5", R.id.edtNum1, null);

            // Emaitza, bi sarrerak, RadioGroup eta erroreak garbitu.
            eszenatokia.onActivity(jarduera -> {
                jarduera.findViewById(R.id.btnGarbitu).performClick();
                EditText a = jarduera.findViewById(R.id.edtNum1);
                EditText b = jarduera.findViewById(R.id.edtNum2);
                RadioGroup rdg = jarduera.findViewById(R.id.rdgEragiketak);
                assertEquals("", a.getText().toString());
                assertEquals("", b.getText().toString());
                assertNull(a.getError());
                assertNull(b.getError());
                assertEquals(-1, rdg.getCheckedRadioButtonId());
                assertTrue(a.hasFocus());
                assertEquals(View.GONE,
                        jarduera.findViewById(R.id.txtEmaitza).getVisibility());
            });
            ondo("GARBITU: bi sarrerak, hautaketa, emaitza, erroreak eta fokua");
            kaptura("08_Garbitu_Ondoren.png");
        }
        gorde("09_Proben_Emaitzak.txt", "text/plain", null,
                txostena.toString().getBytes(StandardCharsets.UTF_8));
    }
}
