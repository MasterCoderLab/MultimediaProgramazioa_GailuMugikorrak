package com.ikasle.masvistas;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * MasVistas aplikazioaren jarduera nagusia.
 *
 * <p>Klase honek main.xml fitxategian definitutako bistak kudeatzen ditu.
 * Erabiltzaileak zenbaki bat sar dezake, zenbakizko botoien bidez balioak
 * gehitu eta botoi grafikoa sakatzean sartutako balioa bider bi kalkulatu.</p>
 *
 * <p>Ariketa honetan findViewById(), listener-ak, tag atributua,
 * type cast-ak eta String/Float bihurketak lantzen dira.</p>
 */
public class MasVistasActivity extends AppCompatActivity {

    /**
     * Erabiltzaileak zenbakizko balioa sartzeko erabiltzen duen EditText-a.
     */
    private EditText entrada;

    /**
     * Kalkulatutako emaitza erakusteko erabiltzen den TextView-a.
     */
    private TextView salida;

    /**
     * Jarduera sortzean exekutatzen den metodoa.
     *
     * <p>main.xml layout-a kargatzen du, Edge-to-Edge konfigurazioa
     * aplikatzen du eta XML-ko entrada eta salida bistak Java
     * objektuekin lotzen ditu findViewById() erabiliz.</p>
     *
     * @param savedInstanceState jardueraren aurreko egoera gordetzen duen
     *                           Bundle objektua; lehen exekuzioan null izan daiteke
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Edge-to-Edge modua aktibatzen da.
        EdgeToEdge.enable(this);

        // main.xml jardueraren interfaze grafiko gisa kargatzen da.
        setContentView(R.layout.main);

        /*
         * Sistemaren egoera- eta nabigazio-barren tamaina kontuan hartzen da,
         * interfazearen edukia barra horien azpian gera ez dadin.
         */
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );

            return insets;
        });

        /*
         * XML-ko bistak Java objektuekin lotzen dira.
         * Ariketak type cast-a lantzen duenez, (EditText) eta (TextView)
         * bihurketak mantentzen dira.
         */
        entrada = (EditText) findViewById(R.id.entrada);
        salida = (TextView) findViewById(R.id.salida);
    }

    /**
     * Botoi grafikoa sakatzean exekutatzen den listener-a.
     *
     * <p>entrada eremuko testua String bihurtzen da, ondoren Float
     * zenbakira pasatzen da eta bider bi egiten da. Azken emaitza berriro
     * String bihurtzen da salida TextView-an erakutsi ahal izateko.</p>
     *
     * @param view klik-gertaera sortu duen bista
     */
    public void sePulsa(View view) {
        salida.setText(
                String.valueOf(
                        Float.parseFloat(entrada.getText().toString()) * 2.0
                )
        );
    }

    /**
     * Zenbakizko botoia sakatzean exekutatzen den listener-a.
     *
     * <p>view parametroaren bidez sakatutako botoia jasotzen da.
     * getTag() metodoak XML-ko android:tag atributuan gordetako
     * Object objektua itzultzen du.</p>
     *
     * <p>Tag-ean String bat gordetzen denez, Object-etik String-era
     * type cast-a egiten da eta balioa entrada eremuaren amaieran
     * gehitzen da.</p>
     *
     * @param view klik-gertaera sortu duen bista; haren tag atribututik
     *             gehitu beharreko balioa lortzen da
     */
    public void sePulsa0(View view) {
        entrada.setText(entrada.getText() + (String) view.getTag());
    }
}