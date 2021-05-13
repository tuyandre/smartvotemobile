package xyz.developerbab.smartvoteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xyz.developerbab.smartvoteapp.Adapter.ViewupcomingAdapter;
import xyz.developerbab.smartvoteapp.Model.Candidate;
import xyz.developerbab.smartvoteapp.interfaces.RESTApiInterface;

public class VotingMain2Activit extends AppCompatActivity implements View.OnClickListener, ViewupcomingAdapter.OnItemClickListener {

    private WebView webviewstatistics;
    private ImageView imgmenu;
    private DrawerLayout drawer;

    private static final int TIME_INTERVAL = 3000;
    private long mBackPressed;
    private TextView tvhellodrawer;

    private String period,reason,name, nid, id, email, phone, level, biometric, status, jsonresponseupcoming, province, district, logo, amazina, ifoto, ishyaka, amerekezo, amerekezo2, status_voting, imbaraga, from, to;

    private ProgressBar pbupcoming;
    private ViewupcomingAdapter adapter;
    private ArrayList<Candidate> list_data;
    private AppBarConfiguration mAppBarConfiguration;
    private TextView tvperiod, tvfrom, tvto, tvreason, tvmessage;

    private RecyclerView recyclerviewupcoming;
    private ImageButton ibrefreshmain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_main2);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);


        tvmessage = findViewById(R.id.tvmessagevote);
        tvperiod = findViewById(R.id.tvperiod);
        tvfrom = findViewById(R.id.tvfrom);
        tvto = findViewById(R.id.tvto);
        tvreason = findViewById(R.id.tvreason);

        pbupcoming = findViewById(R.id.pbupcoming);

        Intent intent = getIntent();
        name = intent.getStringExtra("names");
        nid = intent.getStringExtra("nid");
        id = intent.getStringExtra("user");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        level = intent.getStringExtra("level");
        status = intent.getStringExtra("status");
        biometric = intent.getStringExtra("biometric");

        province = intent.getStringExtra("province");
        district = intent.getStringExtra("district");

        recyclerviewupcoming = findViewById(R.id.recyclerviewupcoming);

        recyclerviewupcoming.setHasFixedSize(true);
        recyclerviewupcoming.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        list_data = new ArrayList<>();


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        imgmenu = findViewById(R.id.imgmenu);
        imgmenu.setOnClickListener(this);


        getupcomingelection();

        drawer = findViewById(R.id.drawer_layout);

        View header = navigationView.getHeaderView(0);

        tvhellodrawer = header.findViewById(R.id.tvhellodrawer);
        tvhellodrawer.setText("Welcome, " + name + "\n" + phone);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {


                    case R.id.nav_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(VotingMain2Activit.this);
                        builder.setCancelable(false);
                        builder.setMessage("Do you want to logout");
                        builder.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(VotingMain2Activit.this, ChooseActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
                                startActivity(intent);
                                finish();
                                Toast.makeText(VotingMain2Activit.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();

                        break;
                    case R.id.nav_candidate:
                        drawer.close();
                        break;

                    default:
                        break;
                }

                return true;
            }
        });


        ibrefreshmain = findViewById(R.id.ibrefreshmain);
        ibrefreshmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VotingMain2Activit.this, "Refreshing ...", Toast.LENGTH_SHORT).show();
                getupcomingelection();
            }
        });

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
        switch (v.getId()) {
            case R.id.imgmenu:
                drawer.open();
                break;
        }
    }

    private void getupcomingelection() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://vote.developerbab.xyz/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RESTApiInterface api = retrofit.create(RESTApiInterface.class);

        Call<String> call = api.getallupcomingel();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body());

                        jsonresponseupcoming = response.body();

                        //   Toast.makeText(VotingMain2Activit.this, response.body(), Toast.LENGTH_SHORT).show();
                        displayupcoming();


                        pbupcoming.setVisibility(View.GONE);

                    } else{

                        pbupcoming.setVisibility(View.GONE);
                        Toast.makeText(VotingMain2Activit.this, "Internal server error occured" + response.body(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pbupcoming.setVisibility(View.GONE);
                Toast.makeText(VotingMain2Activit.this, t.getCause().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void displayupcoming() {
        try {

            JSONObject object = new JSONObject(jsonresponseupcoming);
            JSONObject ob = new JSONObject(jsonresponseupcoming);

            status_voting = ob.getString("message");
            tvmessage.setText(status_voting);

            JSONArray array = object.getJSONArray("candidates");

            JSONObject object1 = ob.getJSONObject("season");

            period = object1.getString("period");
            from = object1.getString("start_date");
            to = object1.getString("end_date");
            reason = object1.getString("reason");

            for (int i = 0; i < array.length(); i++) {


                tvperiod.setText(period);
                tvfrom.setText("From:  " + from);
                tvto.setText("To:  " + to);
                tvreason.setText(reason);


                JSONObject jsonObject = array.getJSONObject(i);

                JSONObject readerprovince = jsonObject.getJSONObject("province");
                JSONObject readerdistrict = jsonObject.getJSONObject("district");




                amazina = jsonObject.getString("name");
                String dob = jsonObject.getString("dob");
                ishyaka = jsonObject.getString("party");
                ifoto = jsonObject.getString("profile");
                logo = jsonObject.getString("logo");
                imbaraga = jsonObject.getString("strength");
                String seasonid = jsonObject.getString("season_id");
                String candid = jsonObject.getString("id");

                amerekezo = readerprovince.getString("name");
                amerekezo2 = readerdistrict.getString("name");
                String provid = readerprovince.getString("id");
                String disid = readerdistrict.getString("id");


                Candidate model = new Candidate();
                model.setCandidate_name(amazina);
                model.setDob(dob);
                model.setParty(ishyaka);
                model.setProfile(ifoto);
                model.setLogo(logo);
                model.setStrength(imbaraga);
                model.setProvince_name(amerekezo);
                model.setDistrict_name(amerekezo2);
                model.setProvince_id(provid);
                model.setDistrict_id(disid);
                model.setSeason_id(seasonid);
                model.setCandidate_id(candid);

                list_data.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new ViewupcomingAdapter(VotingMain2Activit.this, list_data);
        recyclerviewupcoming.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(VotingMain2Activit.this);

    }


    @Override
    public void onItemClick(int position) {
        //Toast.makeText(this, "taped on " + id + list_data.get(position).getCandidate_id() + list_data.get(position).getProvince_id() + list_data.get(position).getDistrict_id() + list_data.get(position).getSeason_id(), Toast.LENGTH_LONG).show();

        if (status_voting.equals("upcoming")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(VotingMain2Activit.this);
            builder.setTitle("Status of voting is " + status_voting);
            builder.setMessage("Sorry " + name + "\n" + " This election of " + tvperiod.getText().toString() + " is not yet active at the moment,it will start from  " + "\n" + from + " up to " + to);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(VotingMain2Activit.this, "Ok,I understand", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        } else if (status_voting.equals("active")){

            Intent intenti = new Intent(VotingMain2Activit.this, ActivityVoting.class);

            intenti.putExtra("candidate", list_data.get(position).getCandidate_id());
            intenti.putExtra("user", id);
            intenti.putExtra("candidate", list_data.get(position).getCandidate_id());
            intenti.putExtra("province", province);
            intenti.putExtra("district", district);
            intenti.putExtra("season", list_data.get(position).getSeason_id());
            intenti.putExtra("names",list_data.get(position).getCandidate_name());
            intenti.putExtra("profile", list_data.get(position).getProfile());
            intenti.putExtra("logo", list_data.get(position).getLogo());
            intenti.putExtra("party", list_data.get(position).getParty());
            intenti.putExtra("strength", list_data.get(position).getStrength());
            intenti.putExtra("provname", list_data.get(position).getProvince_name());
            intenti.putExtra("disname", list_data.get(position).getDistrict_name());
            intenti.putExtra("izinaa",name);
            intenti.putExtra("fonee",phone);

            startActivity(intenti);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
    }

    private void showwebview() {

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                WebSettings webSettings = webviewstatistics.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webviewstatistics.loadUrl("https://money.cnn.com/quote/quote.html?symb=FLWS");
            }
        }, 0, 3000);


    }
}
