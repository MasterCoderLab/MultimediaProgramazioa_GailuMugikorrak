package com.example.ariketa7_javainterfazea;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView(R.layout.activity_main);

        TextView testu = new TextView(this);
        testu.setText("Zorionak");

        setContentView(testu);
    }
}