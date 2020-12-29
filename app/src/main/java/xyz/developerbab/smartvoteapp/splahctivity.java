package xyz.developerbab.smartvoteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splahctivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splahctivity);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(splahctivity.this, ChooseActivity.class);
                startActivity(i);
                splahctivity.this.finish();

            }
        }, SPLASH_TIME_OUT);


    }
}
