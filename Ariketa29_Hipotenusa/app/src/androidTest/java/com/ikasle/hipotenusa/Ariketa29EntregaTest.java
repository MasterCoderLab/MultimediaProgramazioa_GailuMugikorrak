package com.ikasle.hipotenusa;

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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Ariketa 29: bi katetoak, baliozkotzeak, garbiketa eta kapturak egiaztatzen ditu.
 * Probetako testuak UI harian jartzen dira; ez dugu Gboard erabiltzen.
 */
@RunWith(AndroidJUnit4.class)
public class Ariketa29EntregaTest {

    private final StringBuilder txostena = new StringBuilder(
            "Ariketa 29 - Hipotenusa - Proba automatizatuak\n\n");

    /**
     * Gainditutako proba txostenean erregistratzen du.
     * @param mezua probaren azalpena
     */
    private void ondo(String mezua) {
        txostena.append("OK: ").append(mezua).append('\n');
        System.out.println("OK PROBA: " + mezua);
    }

    /**
     * MediaStore bidez Androiden Download karpetan gorde.
     * @param izena fitxategiaren izena
     * @param mota MIME mota
     * @param irudia PNGa edo null
     * @param datuak testu-edukia edo null
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
                Environment.DIRECTORY_DOWNLOADS + "/Ariketa29_Entrega_" + runId + "/");
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
     * Pantaila argazki bat MediaStore bidez gordetzen du.
     * @param izena kapturaren izena
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
     * Katetoak idatzi eta hipotenusa baliozkotzen du.
     * @param eszenatokia ActivityScenario
     * @param b b katetoa
     * @param c c katetoa
     * @param esperoEspero espero den hipotenusa
     * @param izena kapturaren izena (edo null)
     * @throws Exception kapturak huts egiten badu
     */
    private void kasua(ActivityScenario<MainActivity> eszenatokia,
                       String b, String c, double esperoEspero, String izena) throws Exception {
        eszenatokia.onActivity(jarduera -> {
            EditText eb = jarduera.findViewById(R.id.edtKatetoB);
            EditText ec = jarduera.findViewById(R.id.edtKatetoC);
            TextView emaitza = jarduera.findViewById(R.id.txtEmaitza);
            eb.setText(b);
            ec.setText(c);
            assertEquals(View.GONE, emaitza.getVisibility());
            assertEquals("Hipotenusa metodoa", esperoEspero,
                    jarduera.hipotenusa_kalkulatu(
                            Double.parseDouble(b.replace(',', '.')),
                            Double.parseDouble(c.replace(',', '.'))), 1e-9);
            jarduera.findViewById(R.id.btnKalkulatu).performClick();
            assertEquals(View.VISIBLE, emaitza.getVisibility());
            String mezua = emaitza.getText().toString();
            DecimalFormat formatua = new DecimalFormat("0.##",
                    DecimalFormatSymbols.getInstance(Locale.forLanguageTag("eu")));
            assertEquals("Pantailako emaitza", "Hipotenusa: " + formatua.format(esperoEspero), mezua);
        });
        ondo("b=" + b + ", c=" + c + " -> " + esperoEspero);
        if (izena != null) kaptura(izena);
    }

    /**
     * Eremu okerrean errorea eta fokua agertzen direla egiaztatzen du.
     * @param eszenatokia ActivityScenario
     * @param b b sarrera
     * @param c c sarrera
     * @param okerra errorea jaso behar duen eremuaren IDa
     * @param izena kapturaren izena (edo null)
     * @throws Exception kaptura ezin bada egin
     */
    private void errorea(ActivityScenario<MainActivity> eszenatokia,
                        String b, String c, int okerra, String izena) throws Exception {
        eszenatokia.onActivity(jarduera -> {
            EditText eb = jarduera.findViewById(R.id.edtKatetoB);
            EditText ec = jarduera.findViewById(R.id.edtKatetoC);
            eb.setText(b);
            ec.setText(c);
            jarduera.findViewById(R.id.btnKalkulatu).performClick();
            EditText helburua = jarduera.findViewById(okerra);
            assertNotNull("Errorea falta da", helburua.getError());
            assertTrue("Fokua eremu okerrean behar da", helburua.hasFocus());
            assertEquals(View.GONE,
                    jarduera.findViewById(R.id.txtEmaitza).getVisibility());
        });
        ondo("Sarrera baliogabea: b='" + b + "', c='" + c + "'");
        if (izena != null) kaptura(izena);
    }

    /**
     * Bi metodoen portaera eta interfazearen egoerak probatzen ditu.
     * @throws Exception fitxategiak ezin badira gorde
     */
    @Test
    public void hipotenusaBaliozkotzeaGarbiketaEtaKapturak() throws Exception {
        try (ActivityScenario<MainActivity> eszenatokia =
                     ActivityScenario.launch(MainActivity.class)) {
            eszenatokia.onActivity(jarduera -> {
                assertNotNull(jarduera.findViewById(R.id.imgTriangelua));
                assertNotNull(jarduera.findViewById(R.id.edtKatetoB));
                assertNotNull(jarduera.findViewById(R.id.edtKatetoC));
                assertNotNull(jarduera.findViewById(R.id.btnGarbitu));
                assertNotNull(jarduera.findViewById(R.id.btnKalkulatu));
                assertEquals(View.GONE,
                        jarduera.findViewById(R.id.txtEmaitza).getVisibility());
            });
            kaptura("01_Hasierako_Pantaila.png");
            ondo("Hasierako pantaila: triangelua, bi kateto eta bi botoi");

            kasua(eszenatokia, "3", "4", 5.0, "02_Hipotenusa_5.png");
            kasua(eszenatokia, "5", "12", 13.0, "03_Hipotenusa_13.png");
            kasua(eszenatokia, "6", "8", 10.0, null);
            kasua(eszenatokia, "0,3", "0,4", 0.5, "04_Hamartarrak.png");
            kasua(eszenatokia, "8", "15", 17.0, null);
            kasua(eszenatokia, "1e308", "1e308", Math.hypot(1e308,1e308), null);

            errorea(eszenatokia, "", "", R.id.edtKatetoC, null);
            errorea(eszenatokia, "", "4", R.id.edtKatetoB, "05_Eremu_Hutsa.png");
            errorea(eszenatokia, "3", "", R.id.edtKatetoC, null);
            errorea(eszenatokia, "-3", "4", R.id.edtKatetoB, "06_Negatiboa.png");
            errorea(eszenatokia, "0", "4", R.id.edtKatetoB, "07_Zeroa.png");
            errorea(eszenatokia, "abc", "4", R.id.edtKatetoB, null);
            errorea(eszenatokia, "3", "NaN", R.id.edtKatetoC, null);
            errorea(eszenatokia, "1e308", "1e3080", R.id.edtKatetoC, null);
            errorea(eszenatokia, "1.7e308", "1.7e308", R.id.edtKatetoB, null);
            eszenatokia.onActivity(jarduera -> {
                jarduera.findViewById(R.id.btnGarbitu).performClick();
                EditText b = jarduera.findViewById(R.id.edtKatetoB);
                EditText c = jarduera.findViewById(R.id.edtKatetoC);
                assertEquals("", b.getText().toString());
                assertEquals("", c.getText().toString());
                assertNull(b.getError());
                assertNull(c.getError());
                assertTrue(b.hasFocus());
                assertEquals(View.GONE,
                        jarduera.findViewById(R.id.txtEmaitza).getVisibility());
            });
            ondo("GARBITU: bi eremuak, erroreak, emaitza eta fokua");
            kaptura("08_Garbitu_Ondoren.png");
        }
        gorde("09_Proben_Emaitzak.txt", "text/plain", null,
                txostena.toString().getBytes(StandardCharsets.UTF_8));
    }
}
