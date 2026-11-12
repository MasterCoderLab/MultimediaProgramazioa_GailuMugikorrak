package com.ikasle.asmatu_nire_zenbakia_radiobuttonekin;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/** Extra1b-ren bi aukeren eta sarrera-baliozkotzearen probak. */
@RunWith(AndroidJUnit4.class)
public class Extra1BRadioButtonTest {

    /** RadioGroup-ek aukera bakarra mantentzen duela frogatzen du. */
    @Test
    public void radioButtonAukeraBakarra() throws IOException {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(jarduera -> {
                RadioButton hamar = jarduera.findViewById(R.id.rbHamarSaiakera);
                RadioButton laguntza = jarduera.findViewById(R.id.rbLaguntza);
                assertTrue(hamar.isChecked());
                assertFalse(laguntza.isChecked());
            });
            InstrumentationRegistry.getInstrumentation().waitForIdleSync();
            argazkiaGorde("Extra1B_01_HamarSaiakera.png");

            scenario.onActivity(jarduera -> {
                RadioButton hamar = jarduera.findViewById(R.id.rbHamarSaiakera);
                RadioButton laguntza = jarduera.findViewById(R.id.rbLaguntza);
                laguntza.performClick();
                assertFalse(hamar.isChecked());
                assertTrue(laguntza.isChecked());
            });
            InstrumentationRegistry.getInstrumentation().waitForIdleSync();
            argazkiaGorde("Extra1B_02_Laguntza.png");

            scenario.onActivity(jarduera -> {
                RadioButton hamar = jarduera.findViewById(R.id.rbHamarSaiakera);
                RadioButton laguntza = jarduera.findViewById(R.id.rbLaguntza);
                hamar.performClick();
                assertTrue(hamar.isChecked());
                assertFalse(laguntza.isChecked());
            });
        }
    }

    /** Hutsik eta tartetik kanpo dagoen sarrera baztertzen dela frogatzen du. */
    @Test
    public void zenbakiBaliozkotzea() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            scenario.onActivity(jarduera -> {
                EditText zenbakia = jarduera.findViewById(R.id.edtZenbakia);
                zenbakia.setText("");
                jarduera.findViewById(R.id.btnEgiaztatu).performClick();
                assertNotNull(zenbakia.getError());

                zenbakia.setText("101");
                jarduera.findViewById(R.id.btnEgiaztatu).performClick();
                assertNotNull(zenbakia.getError());
            });
        }
    }

    /** Emuladoreko Download karpetan PNG ebidentzia gordetzen du (API 29+). */
    private void argazkiaGorde(String izena) throws IOException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return;
        Bitmap bitmap = InstrumentationRegistry.getInstrumentation().getUiAutomation().takeScreenshot();
        assertNotNull(bitmap);
        ContentValues datuak = new ContentValues();
        datuak.put(MediaStore.MediaColumns.DISPLAY_NAME, izena);
        datuak.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        datuak.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/");
        Uri uri = InstrumentationRegistry.getInstrumentation().getTargetContext()
                .getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, datuak);
        assertNotNull(uri);
        try (OutputStream irteera = InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getContentResolver().openOutputStream(uri)) {
            assertNotNull(irteera);
            assertTrue(bitmap.compress(Bitmap.CompressFormat.PNG, 100, irteera));
        } finally {
            bitmap.recycle();
        }
    }
}
