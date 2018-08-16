package com.example.actat.flowfreesolver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.button_reset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clickされた時の処理
                Log.v("RESET", "button_reset clicked");
            }
        });

        ((Button)findViewById(R.id.button_solve)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clickされた時の処理
                Log.v("SOLVE", "button_solve clicked");
            }
        });
    }
}
