package xyz.developerbab.smartvoteapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityVoting extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView imgifoto;
    private Button btntora;
    private TextView tvamazina, tvishyaka, tvamerekezo, tvimbaraga;
    private String profile, names, party, province, district, disname, provname, strength, season, user, candidate, logo,fone,izina;
    private ImageView imglogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);


        imglogo = findViewById(R.id.imglogo);

        Intent intent = getIntent();
        izina=intent.getStringExtra("izinaa");
        fone=intent.getStringExtra("fonee");
        profile = intent.getStringExtra("profile");
        names = intent.getStringExtra("names");
        party = intent.getStringExtra("party");
        province = intent.getStringExtra("province");
        district = intent.getStringExtra("district");
        strength = intent.getStringExtra("strength");
        provname = intent.getStringExtra("provname");
        disname = intent.getStringExtra("disname");
        season = intent.getStringExtra("season");
        candidate = intent.getStringExtra("candidate");
        user = intent.getStringExtra("user");
        logo = intent.getStringExtra("logo");


        imgifoto = findViewById(R.id.imgifoto);
        btntora = findViewById(R.id.btntora);
        btntora.setOnClickListener(this);

        tvamazina = findViewById(R.id.tvamazina);
        tvishyaka = findViewById(R.id.tvishyaka);
        tvamerekezo = findViewById(R.id.tvamerekezo);
        tvimbaraga = findViewById(R.id.tvimbaraga);


        tvamazina.setText(names);
        tvishyaka.setText(party);
        tvamerekezo.setText(provname + " - " + disname);
        tvimbaraga.setText(strength);


        if (profile.equals("null")) {

            Picasso.with(ActivityVoting.this)
                    .load("http://vote.developerbab.xyz/backend/candidates/default.jpg")
                    .resize(60, 60)
                    .noFade()
                    .centerCrop()
                    .error(R.drawable.vote2)
                    .into(imgifoto);

        } else {

            Picasso.with(ActivityVoting.this)
                    .load("http://vote.developerbab.xyz/backend/candidates/" + profile)
                    .resize(60, 60)
                    .noFade()
                    .centerCrop()
                    .error(R.drawable.vote1)
                    .into(imgifoto);

        }

        // Toast.makeText(this, "data are"+"\n"+user+"\n"+province+"\n"+district+"\n"+season+"\n"+candidate, Toast.LENGTH_LONG).show();

        showlogo();
    }

    private void showlogo() {

        if (logo.equals("null")) {

            Picasso.with(ActivityVoting.this)
                    .load("http://vote.developerbab.xyz/backend/logos/default.jpg")
                    .error(R.drawable.vote2)
                    .into(imglogo);

        } else {

            Picasso.with(ActivityVoting.this)
                    .load("http://vote.developerbab.xyz/backend/logos/" + profile)
                    .noFade()
                    .error(R.drawable.vote2)
                    .into(imglogo);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btntora:
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityVoting.this);
                builder.setTitle("Are you sure,you want to vote?");
                builder.setPositiveButton("VOTE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ActivityVoting.this, ChecktoVoteActivity.class);
                        intent.putExtra("user",user);
                        intent.putExtra("province",province);
                        intent.putExtra("district",district);
                        intent.putExtra("season",season);
                        intent.putExtra("candidate",candidate);
                        intent.putExtra("candidate_name",names);
                        intent.putExtra("names",izina);
                        intent.putExtra("phone",fone);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        ActivityVoting.this.finish();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
                break;
        }
    }
}
