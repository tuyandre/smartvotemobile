package xyz.developerbab.smartvoteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView salutation;
    private LinearLayout laylogin,layregister;
    private ImageView imgtime;

    private static final int TIME_INTERVAL = 3000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        salutation=findViewById(R.id.salutation);
        imgtime=findViewById(R.id.imgtime);
        laylogin=findViewById(R.id.lay_farmer);
        laylogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
startActivity(new Intent(ChooseActivity.this,LoginActivity.class));
            }
        });

        layregister=findViewById(R.id.lay_client);
        layregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseActivity.this,MainActivity.class));
            }
        });

        salutation();

    }


    private void salutation(){
        // salitation
        Calendar calendar = Calendar.getInstance();
        int dayTime = calendar.get(Calendar.HOUR_OF_DAY);

        if (dayTime >= 04 && dayTime < 12) {
            // morning
            imgtime.setImageDrawable(getResources().getDrawable(R.drawable.sun));
            salutation.setText("Morning");
            salutation.setTextColor(getResources().getColor(R.color.colorbluedark));

        } else if (dayTime >= 12 && dayTime < 17) {
            // afternoon
            imgtime.setImageDrawable(getResources().getDrawable(R.drawable.sun));
            salutation.setText("Afternoon");
            salutation.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        } else if (dayTime >= 17 && dayTime < 20) {
            // evening
            imgtime.setImageDrawable(getResources().getDrawable(R.drawable.mon));
            salutation.setText("Evening");
            salutation.setTextColor(getResources().getColor(R.color.colorblue));
        } else if (dayTime >= 20 && dayTime < 23) {
            // night
            imgtime.setImageDrawable(getResources().getDrawable(R.drawable.mon));
            salutation.setText("Night");
            salutation.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

    }
    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;

        } else {
            Toast.makeText(getBaseContext(), "press back again to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public void onClick(View v) {

    }


}
