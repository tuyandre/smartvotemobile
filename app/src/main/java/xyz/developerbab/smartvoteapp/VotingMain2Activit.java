package xyz.developerbab.smartvoteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class VotingMain2Activit extends AppCompatActivity implements View.OnClickListener {


    private ImageView imgmenu;
    private DrawerLayout drawer;


    private static final int TIME_INTERVAL = 3000;
    private long mBackPressed;
    private TextView tvhellodrawer;

    private String name,nid,id,email,phone,level,biometric,status;


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_main2);

        Intent intent=getIntent();
        name=intent.getStringExtra("names");
        nid=intent.getStringExtra("nid");
        id=intent.getStringExtra("id");
        email=intent.getStringExtra("email");
        phone=intent.getStringExtra("phone");
        level=intent.getStringExtra("level");
        status=intent.getStringExtra("status");
        biometric=intent.getStringExtra("biometric");


         drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        imgmenu=findViewById(R.id.imgmenu);
        imgmenu.setOnClickListener(this);

        drawer = findViewById(R.id.drawer_layout);

        View header = navigationView.getHeaderView(0);

        tvhellodrawer=header.findViewById(R.id.tvhellodrawer);
        tvhellodrawer.setText("Welcome, "+name+"\n"+phone);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){


                    case R.id.nav_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(VotingMain2Activit.this);
                        builder.setCancelable(false);
                        builder.setMessage("Do you want to logout");
                        builder.setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent =new Intent(VotingMain2Activit.this, ChooseActivity.class);
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
        switch (v.getId()){
            case R.id.imgmenu:
                drawer.open();
                break;
        }
    }
}
