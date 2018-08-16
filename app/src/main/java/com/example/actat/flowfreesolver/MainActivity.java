package com.example.actat.flowfreesolver;

import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Drawable button_ring[] = new Drawable[16];
    Drawable button_rect[] = new Drawable[16];
    Button button[] = new Button[25];

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

        button_init();
    }

    private void button_init() {
        button_ring[0] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_red, null);
        button_ring[1] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_green, null);
        button_ring[2] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_blue, null);
        button_ring[3] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_yellow, null);
        button_ring[4] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_orange, null);
        button_ring[5] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_cyan, null);
        button_ring[6] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_magenta, null);
        button_ring[7] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_pink, null);
        button_ring[8] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_purple, null);
        button_ring[9] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_tan, null);
        button_ring[10] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_maroon, null);
        button_ring[11] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_brightgreen, null);
        button_ring[12] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_darkblue, null);
        button_ring[13] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_white, null);
        button_ring[14] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_darkcyan, null);
        button_ring[15] = ResourcesCompat.getDrawable(getResources(), R.drawable.ring_gray, null);

        button_rect[0] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_red, null);
        button_rect[1] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_green, null);
        button_rect[2] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_blue, null);
        button_rect[3] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_yellow, null);
        button_rect[4] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_orange, null);
        button_rect[5] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_cyan, null);
        button_rect[6] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_magenta, null);
        button_rect[7] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_pink, null);
        button_rect[8] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_purple, null);
        button_rect[9] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_tan, null);
        button_rect[10] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_maroon, null);
        button_rect[11] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_brightgreen, null);
        button_rect[12] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_darkblue, null);
        button_rect[13] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_white, null);
        button_rect[14] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_darkcyan, null);
        button_rect[15] = ResourcesCompat.getDrawable(getResources(), R.drawable.rect_gray, null);

        button[0] = (Button)findViewById(R.id.button1);
        button[1] = (Button)findViewById(R.id.button2);
        button[2] = (Button)findViewById(R.id.button3);
        button[3] = (Button)findViewById(R.id.button4);
        button[4] = (Button)findViewById(R.id.button5);
        button[5] = (Button)findViewById(R.id.button6);
        button[6] = (Button)findViewById(R.id.button7);
        button[7] = (Button)findViewById(R.id.button8);
        button[8] = (Button)findViewById(R.id.button9);
        button[9] = (Button)findViewById(R.id.button10);
        button[10] = (Button)findViewById(R.id.button11);
        button[11] = (Button)findViewById(R.id.button12);
        button[12] = (Button)findViewById(R.id.button13);
        button[13] = (Button)findViewById(R.id.button14);
        button[14] = (Button)findViewById(R.id.button15);
        button[15] = (Button)findViewById(R.id.button16);
        button[16] = (Button)findViewById(R.id.button17);
        button[17] = (Button)findViewById(R.id.button18);
        button[18] = (Button)findViewById(R.id.button19);
        button[19] = (Button)findViewById(R.id.button20);
        button[20] = (Button)findViewById(R.id.button21);
        button[21] = (Button)findViewById(R.id.button22);
        button[22] = (Button)findViewById(R.id.button23);
        button[23] = (Button)findViewById(R.id.button24);
        button[24] = (Button)findViewById(R.id.button25);
    }
}
