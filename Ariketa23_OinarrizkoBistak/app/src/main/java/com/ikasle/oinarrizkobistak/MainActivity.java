
package com.ikasle.oinarrizkobistak;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Android aplikazioko oinarrizko bisten funtzionaltasuna kudeatzen duen klasea.
 *
 * TextView, Button, CheckBox, RadioGroup, EditText, Spinner eta SeekBar
 * elementuen gertaerak kudeatzen ditu.
 *
 * Erabiltzailearen ekintzak Logcat-en erregistratzen dira,
 * interfaze grafikoaren funtzionamendua egiaztatzeko.
 */
public class MainActivity extends AppCompatActivity {

    // Logcat-en aplikazioaren mezuak identifikatzeko etiketa.
    private static final String ETIKETA = "Info";

    /**
     * Jarduera sortzean interfazea eta gertaerak prestatzen ditu.
     *
     * Layout-a kargatu, sistemaren barrak egokitu eta
     * bista bakoitzaren funtzionaltasuna hasieratzen du.
     *
     * @param savedInstanceState Aurretik gordetako jardueraren egoera.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.bistak);

        // Sistemaren barren arabera pantailaren tarteak egokitzen dira.
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {

                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    v.setPadding(bars.left, bars.top,
                            bars.right, bars.bottom);

                    return insets;
                });

        // Interfazeko osagai bakoitzaren gertaerak prestatzen dira.
        prestatuTextView();
        prestatuBotoia();
        prestatuCheckBox();
        prestatuRadioGroup();
        prestatuEditText();
        prestatuSpinner();
        prestatuSeekBar();
    }

    /**
     * TextView elementuaren testua Java kodetik aldatzen du.
     *
     * setText() metodoaren bidez XML fitxategian definitutako
     * hasierako testua ordezkatzen da.
     */
    private void prestatuTextView() {

        TextView testua = findViewById(R.id.textView1);

        // Baliabideetan definitutako testu berria erakusten da.
        testua.setText(R.string.testu_berria);
    }

    /**
     * Button elementuaren klik-gertaera konfiguratzen du.
     *
     * Erabiltzaileak botoia sakatzen duenean,
     * mezua Logcat-en erregistratzen da.
     */
    private void prestatuBotoia() {

        Button botoia = findViewById(R.id.button1);

        // Botoia sakatzean entzuleak dagokion ekintza exekutatzen du.
        botoia.setOnClickListener(v ->
                Log.e(ETIKETA, "Boton click"));
    }

    /**
     * CheckBox elementuaren hautaketa-gertaera konfiguratzen du.
     *
     * Kontrol-laukia hautatuta dagoen egiaztatzen du
     * eta bere egoera Logcat-en erakusten du.
     */
    private void prestatuCheckBox() {

        CheckBox kontrolLaukia = findViewById(R.id.checkBox1);

        kontrolLaukia.setOnClickListener(v -> {

            // isChecked() metodoak hautaketa-egoera itzultzen du.
            if (kontrolLaukia.isChecked()) {

                Log.e(ETIKETA, "CheckBox hautatuta");

            } else {

                Log.e(ETIKETA, "CheckBox ez hautatuta");
            }
        });
    }

    /**
     * RadioGroup elementuaren hautaketa-gertaera konfiguratzen du.
     *
     * Talde bereko RadioButton bakarra egon daiteke hautatuta.
     * Hautatutako botoiaren identifikatzailea egiaztatzen du.
     */
    private void prestatuRadioGroup() {

        RadioGroup taldea = findViewById(R.id.radioGroup1);

        taldea.setOnCheckedChangeListener((group, checkedId) -> {

            // Hautatutako RadioButton-a bere IDaren bidez identifikatzen da.
            if (checkedId == R.id.radio0) {

                Log.e(ETIKETA, "RadioButton0 aukeratuta");

            } else if (checkedId == R.id.radio1) {

                Log.e(ETIKETA, "RadioButton1 aukeratuta");
            }
        });
    }

    /**
     * EditText elementuaren teklatu-gertaerak konfiguratzen ditu.
     *
     * ENTER tekla edo teklatu birtualeko Done botoia sakatzean,
     * erabiltzaileak idatzitako testua Logcat-en erakusten da.
     */
    private void prestatuEditText() {

        EditText testuEremua = findViewById(R.id.editText1);

        // Teklatu fisikoaren ENTER tekla kontrolatzen da.
        testuEremua.setOnKeyListener((v, keyCode, event) -> {

            if (enterTeklaAskatuDa(keyCode, event)) {

                // EditText-eko edukia String bihurtzen da.
                String edukia = testuEremua.getText().toString();

                Log.e(ETIKETA, edukia);

                return true;
            }

            // Beste teklak Android sistemak kudeatzen ditu.
            return false;
        });

        // Teklatu birtualeko Done ekintza kontrolatzen da.
        testuEremua.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                String edukia = testuEremua.getText().toString();

                Log.e(ETIKETA, edukia);

                return true;
            }

            return false;
        });
    }

    /**
     * ENTER tekla askatu den egiaztatzen du.
     *
     * Teklaren kodea eta gertaeraren egoera aztertzen ditu,
     * testua behin bakarrik erregistratzeko.
     *
     * @param teklaKodea Sakatutako teklari dagokion kodea.
     * @param gertaera Teklatuaren gertaerari buruzko informazioa.
     * @return true ENTER tekla askatu bada; bestela, false.
     */
    private boolean enterTeklaAskatuDa(int teklaKodea, KeyEvent gertaera) {

        return teklaKodea == KeyEvent.KEYCODE_ENTER
                && gertaera != null
                && gertaera.getAction() == KeyEvent.ACTION_UP;
    }

    /**
     * Spinner elementuaren aukerak eta gertaerak konfiguratzen ditu.
     *
     * strings.xml fitxategiko array-a erabiltzen du
     * aukerak zerrenda zabalgarriko elementu bihurtzeko.
     */
    private void prestatuSpinner() {

        Spinner spinner = findViewById(R.id.spinner1);

        // XML fitxategiko array-a irakurtzeko egokitzailea sortzen da.
        ArrayAdapter<CharSequence> egokitzailea =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.spinner_aukerak,
                        android.R.layout.simple_spinner_item);

        // Zerrenda zabalgarriaren itxura definitzen da.
        egokitzailea.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        // Egokitzailea Spinner elementuari lotzen zaio.
        spinner.setAdapter(egokitzailea);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    /**
                     * Spinner-eko aukera bat hautatzean exekutatzen da.
                     *
                     * Hautatutako elementuaren edukia berreskuratzen du
                     * eta Logcat-en erregistratzen du.
                     *
                     * @param parent Aukerak dituen AdapterView elementua.
                     * @param view Hautatutako elementuari dagokion bista.
                     * @param position Aukeraren posizioa zerrendan.
                     * @param id Hautatutako elementuaren identifikatzailea.
                     */
                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        // Hautatutako aukeraren testua lortzen da.
                        String aukera = parent.getItemAtPosition(position)
                                .toString();

                        Log.e(ETIKETA, "Se ha seleccionado " + aukera);
                    }

                    /**
                     * Spinner-ean aukerarik ez dagoenean exekutatzen da.
                     *
                     * @param parent Aukerak dituen AdapterView elementua.
                     */
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                        // Ez da ekintzarik egin behar.
                    }
                });
    }

    /**
     * SeekBar elementuaren balio-tartea eta gertaerak konfiguratzen ditu.
     *
     * Barrak 0 eta 10 arteko balioak hartzen ditu.
     * Erabiltzaileak barra askatzean, azken balioa erregistratzen da.
     */
    private void prestatuSeekBar() {

        SeekBar barra = findViewById(R.id.seekBar1);

        // Aukera-tartearen gehieneko balioa ezartzen da.
        barra.setMax(10);

        barra.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    /**
                     * SeekBar-aren balioa aldatzen denean exekutatzen da.
                     *
                     * @param seekBar Aldatzen ari den barra.
                     * @param progress Uneko balioa.
                     * @param fromUser Aldaketa erabiltzaileak egin duen adierazten du.
                     */
                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser) {

                        // Ez da ekintzarik egin behar.
                    }

                    /**
                     * Erabiltzailea barra mugitzen hasten denean exekutatzen da.
                     *
                     * @param seekBar Mugitzen hasi den barra.
                     */
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                        // Ez da ekintzarik egin behar.
                    }

                    /**
                     * Erabiltzaileak barra askatzen duenean exekutatzen da.
                     *
                     * Une horretan aukeratutako balioa berreskuratzen du
                     * eta Logcat-en erregistratzen du.
                     *
                     * @param seekBar Erabiltzaileak askatutako barra.
                     */
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        // getProgress() metodoak aukeratutako balioa itzultzen du.
                        int balioa = seekBar.getProgress();

                        Log.e(ETIKETA, String.valueOf(balioa));
                    }
                });
    }
}
