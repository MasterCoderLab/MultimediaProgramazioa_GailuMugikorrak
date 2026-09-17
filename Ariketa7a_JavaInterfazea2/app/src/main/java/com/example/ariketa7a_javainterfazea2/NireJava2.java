package com.example.ariketa7a_javainterfazea2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NireJava2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // XML-ko LinearLayout-a kargatu
        LinearLayout layout = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.javanirea2, null);

        // Sistemaren barrak kontuan hartu
        ViewCompat.setOnApplyWindowInsetsListener(layout, (v, insets) -> {
            Insets systemBars =
                    insets.getInsets(WindowInsetsCompat.Type.systemBars());

            v.setPadding(
                    dp(16),
                    systemBars.top + dp(16),
                    dp(16),
                    systemBars.bottom + dp(16)
            );

            return insets;
        });

        // Izenburua
        TextView izenburua = new TextView(this);
        izenburua.setText("Auzoko Jaiak");
        izenburua.setTextSize(24);
        izenburua.setTypeface(null, Typeface.BOLD);
        izenburua.setPadding(0, 0, 0, dp(12));
        layout.addView(izenburua);

        // Auzoaren izena
        EditText auzoa = new EditText(this);
        auzoa.setHint("Auzoaren izena");
        layout.addView(auzoa);

        // Helbidea
        EditText helbidea = new EditText(this);
        helbidea.setHint("Helbidea");
        layout.addView(helbidea);

        // E-maila
        EditText emaila = new EditText(this);
        emaila.setHint("E-maila");
        emaila.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        );
        layout.addView(emaila);

        // Pasahitza
        EditText pasahitza = new EditText(this);
        pasahitza.setHint("Pasahitza sartu");
        pasahitza.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD
        );
        layout.addView(pasahitza);

        // Tarte txiki bat botoien aurretik
        pasahitza.setPadding(
                pasahitza.getPaddingLeft(),
                pasahitza.getPaddingTop(),
                pasahitza.getPaddingRight(),
                dp(12)
        );

        // BIDALTZEKO botoia
        Button bidali = new Button(this);
        bidali.setText("BIDALTZEKO");
        layout.addView(bidali);

        // IRTETEKO botoia
        Button irten = new Button(this);
        irten.setText("IRTETEKO");
        layout.addView(irten);

        // Layout-a pantailan erakutsi
        setContentView(layout);
    }

    // dp balioa pixel bihurtzeko
    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}