package com.ikasle.asteroideak;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Aplikazioaren jarduera nagusia.
 *
 * Jarduera honek aplikazioaren pantaila nagusia bistaratzen du eta
 * "Acerca de..." botoiaren bidez AcercaDeActivity jarduera irekitzeko
 * aukera ematen du.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Jarduera sortzen denean exekutatzen den metodoa.
     *
     * Metodo honek activity_main layout-a kargatzen du eta sistemaren
     * barren tamaina kontuan hartzeko beharrezko doikuntzak egiten ditu.
     *
     * @param savedInstanceState Jardueraren aurreko egoera gordetzen duen
     *                           Bundle objektua. Egoerarik ez badago, null izan daiteke.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {

            Insets systemBars =
                    insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );

            return insets;
        });
    }

    /**
     * "Acerca de..." botoia sakatzean AcercaDeActivity jarduera irekitzen du.
     *
     * Intent esplizitu bat sortzen da uneko jardueratik
     * AcercaDeActivity jarduerara nabigatzeko.
     *
     * @param view Metodoa aktibatu duen View objektua, kasu honetan
     *             "Acerca de..." botoia.
     */
    public void lanzarAcercaDe(View view) {

        Intent i = new Intent(this, AcercaDeActivity.class);

        startActivity(i);
    }
}