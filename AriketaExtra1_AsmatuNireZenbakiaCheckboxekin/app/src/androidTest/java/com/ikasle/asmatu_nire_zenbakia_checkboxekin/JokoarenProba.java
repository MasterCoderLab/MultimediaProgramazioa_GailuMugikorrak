package com.ikasle.asmatu_nire_zenbakia_checkboxekin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.OutputStream;
import java.lang.reflect.Field;

/** Jokoaren egiaztapenak eta entrega-kapturak. */
@RunWith(AndroidJUnit4.class)
public class JokoarenProba {

    /** Jokoaren aukera eta emaitza nagusiak probatzen ditu. */
    @Test
    public void jokoaProbatuEtaKapturakSortu() throws Exception {
        try (ActivityScenario<MainActivity> egoera = ActivityScenario.launch(MainActivity.class)) {
            egoera.onActivity(jarduera -> {
                try {
                    Field f = MainActivity.class.getDeclaredField("zenbakiSekretua");
                    f.setAccessible(true);
                    int ausazkoa = f.getInt(jarduera);
                    assertTrue(ausazkoa >= 0 && ausazkoa <= 100);
                    f.setInt(jarduera, 50);
                } catch (ReflectiveOperationException e) {
                    throw new AssertionError(e);
                }
            });
            onView(withId(R.id.chkHamarSaiakera)).check(matches(isChecked()));
            onView(withId(R.id.chkLaguntza)).check(matches(not(isChecked())));
            argazkia("01_hasiera.png");

            zenbakiaIdatzi("25");
            egiaztatu();
            onView(withId(R.id.txtMezua)).check(matches(withText(R.string.ez_da_asmatu)));
            argazkia("02_laguntzarik_gabe.png");

            onView(withId(R.id.chkLaguntza)).perform(click());
            zenbakiaIdatzi("25");
            egiaztatu();
            onView(withId(R.id.txtMezua)).check(matches(withText(R.string.handiagoa)));
            argazkia("03_zenbaki_handiagoa.png");

            zenbakiaIdatzi("75");
            egiaztatu();
            onView(withId(R.id.txtMezua)).check(matches(withText(R.string.txikiagoa)));
            argazkia("04_zenbaki_txikiagoa.png");

            zenbakiaIdatzi("50");
            egiaztatu();
            onView(withId(R.id.txtMezua)).check(matches(withText(
                    "Zorionak! 50 zenbakia asmatu duzu. Saiakerak: 4.")));
            onView(withId(R.id.btnEgiaztatu)).check(matches(not(isEnabled())));
            argazkia("05_asmatuta_lau_saiakeratan.png");
        }

        try (ActivityScenario<MainActivity> egoera = ActivityScenario.launch(MainActivity.class)) {
            zenbakiSekretuaEzarri(egoera, 50);
            egiaztatu();
            argazkia("06_zenbaki_hutsa.png");

            zenbakiaIdatzi("101");
            egiaztatu();
            argazkia("07_zenbaki_baliogabea.png");
            onView(withId(R.id.btnEgiaztatu)).check(matches(isEnabled()));
        }

        try (ActivityScenario<MainActivity> egoera = ActivityScenario.launch(MainActivity.class)) {
            zenbakiSekretuaEzarri(egoera, 50);
            for (int i = 0; i < 10; i++) {
                zenbakiaIdatzi("25");
                egiaztatu();
            }
            onView(withId(R.id.txtMezua)).check(matches(withText(
                    "10 saiakera egin dituzu eta muga gainditu da. Zenbakia 50 zen.")));
            onView(withId(R.id.btnEgiaztatu)).check(matches(not(isEnabled())));
            argazkia("08_hamar_saiakera.png");
        }

        try (ActivityScenario<MainActivity> egoera = ActivityScenario.launch(MainActivity.class)) {
            zenbakiSekretuaEzarri(egoera, 50);
            onView(withId(R.id.chkHamarSaiakera)).perform(click());
            for (int i = 0; i < 11; i++) {
                zenbakiaIdatzi("25");
                egiaztatu();
            }
            onView(withId(R.id.btnEgiaztatu)).check(matches(isEnabled()));
            onView(withId(R.id.txtMezua)).check(matches(withText(R.string.ez_da_asmatu)));
            argazkia("09_mugarik_gabe.png");

            onView(withId(R.id.chkLaguntza)).perform(click());
            zenbakiaIdatzi("75");
            egiaztatu();
            onView(withId(R.id.txtMezua)).check(matches(withText(R.string.txikiagoa)));
            argazkia("10_laguntza_aktibatuta.png");
        }
    }

    /**
     * Probako zenbakia idazten du.
     *
     * @param testua sartu beharreko zenbakia
     */
    private void zenbakiaIdatzi(String testua) {
        onView(withId(R.id.edtZenbakia)).perform(replaceText(testua), closeSoftKeyboard());
    }

    /** Egiaztatu botoia sakatzen du. */
    private void egiaztatu() {
        onView(withId(R.id.btnEgiaztatu)).perform(click());
    }

    /**
     * Zenbaki sekretu ezagun bat ezartzen du, proba errepikagarria izateko.
     *
     * @param egoera proban dagoen jarduera
     * @param balioa probako zenbaki sekretua
     */
    private void zenbakiSekretuaEzarri(ActivityScenario<MainActivity> egoera, int balioa) {
        egoera.onActivity(jarduera -> {
            try {
                Field f = MainActivity.class.getDeclaredField("zenbakiSekretua");
                f.setAccessible(true);
                f.setInt(jarduera, balioa);
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
        });
    }

    /**
     * Androiden deskarga partekatuetan PNG irudi bat gordetzen du.
     *
     * @param izena irudiaren fitxategi-izena
     */
    private void argazkia(String izena) throws Exception {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        Bitmap irudia = InstrumentationRegistry.getInstrumentation()
                .getUiAutomation().takeScreenshot();
        assertNotNull(irudia);
        Context testuingurua = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ContentValues balioak = new ContentValues();
        balioak.put(MediaStore.Downloads.DISPLAY_NAME, izena);
        balioak.put(MediaStore.Downloads.MIME_TYPE, "image/png");
        balioak.put(MediaStore.Downloads.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + "/AndroidDAM_Extra1");
        Uri helbidea = testuingurua.getContentResolver().insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI, balioak);
        assertNotNull(helbidea);
        try (OutputStream irteera = testuingurua.getContentResolver().openOutputStream(helbidea)) {
            assertNotNull(irteera);
            assertTrue(irudia.compress(Bitmap.CompressFormat.PNG, 100, irteera));
        }
        irudia.recycle();
    }
}
