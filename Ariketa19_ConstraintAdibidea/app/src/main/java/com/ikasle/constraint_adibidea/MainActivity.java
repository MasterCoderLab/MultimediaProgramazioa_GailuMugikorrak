package com.ikasle.constraint_adibidea;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Constraint_Adibidea aplikazioaren jarduera nagusia.
 *
 * <p>Ariketa honen helburua ConstraintLayout erabiliz hainbat
 * ikuspegi pantailan kokatzea da: ImageView, CheckBox,
 * TextView eta bi Button.</p>
 *
 * <p>Bista bakoitzaren posizioa constraint-en bidez definitzen da,
 * pantailaren tamainarekiko kokapen koherentea mantentzeko.</p>
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Jarduera sortzen denean exekutatzen den metodoa.
     *
     * <p>activity_main.xml layout-a kargatzen du eta Edge-to-Edge
     * konfigurazioa aplikatzen du.</p>
     *
     * @param savedInstanceState jardueraren aurreko egoera gordetzen duen
     *                           Bundle objektua; lehen exekuzioan null izan daiteke
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Edge-to-Edge modua aktibatzen da.
        EdgeToEdge.enable(this);

        // activity_main.xml interfazea kargatzen da.
        setContentView(R.layout.activity_main);

        /*
         * Sistemaren egoera- eta nabigazio-barrak kontuan hartzeko
         * padding-a aplikatzen da.
         */
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (view, insets) -> {

                    Insets systemBars =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );
    }
}