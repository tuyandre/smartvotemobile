package xyz.developerbab.smartvoteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.developerbab.smartvoteapp.Model.Population;
import xyz.developerbab.smartvoteapp.singleton.RESTApiClient;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvnames, tvphone, tvsex, tvdob, tvnid;
    private String nidentered, name, phone, sex, dob, nid, checkcontinue, province_id, district_id, id, profile, biometric, email, password;
    private EditText etnidcheck, etemail, etpassword;
    private Button btncontinuecheck, btnnexttobio;
    private ProgressDialog progressDialog;
    private LinearLayout lay_message, lay_showdata;
    private CircleImageView imgprofile;
    private ImageView backmain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        backmain=findViewById(R.id.backmain);
        backmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ChooseActivity.class));
            }
        });
        imgprofile = findViewById(R.id.imgprofile);

        lay_message = findViewById(R.id.lay_message);
        lay_showdata = findViewById(R.id.lay_showdata);

        etemail = findViewById(R.id.etemail);
        etpassword = findViewById(R.id.etpassword);
        btnnexttobio = findViewById(R.id.btnnexttobio);
        btnnexttobio.setOnClickListener(this);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        etnidcheck = findViewById(R.id.etnidcheck);
        btncontinuecheck = findViewById(R.id.btncontinuecheck);
        btncontinuecheck.setOnClickListener(this);
        tvnames = findViewById(R.id.tvname);
        tvphone = findViewById(R.id.tvphone);
        tvsex = findViewById(R.id.tvsex);
        tvdob = findViewById(R.id.tvdob);
        tvnid = findViewById(R.id.tvnid);

        checkcontinue = "0";

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btncontinuecheck:
                imgprofile.setVisibility(View.GONE);
                lay_showdata.setVisibility(View.GONE);
                lay_message.setVisibility(View.GONE);
                nidentered = etnidcheck.getText().toString().trim();
                if (nidentered.isEmpty()) {
                    Toast.makeText(this, "Please enter your National Identity number first", Toast.LENGTH_LONG).show();
                } else {
                    checkusernid();
                }
                break;
            case R.id.btnnexttobio:
                email = etemail.getText().toString().trim();
                password = etpassword.getText().toString().trim();
                if (checkcontinue.equals("0")) {
                    Toast.makeText(this, "No record is found", Toast.LENGTH_SHORT).show();
                } else if (checkcontinue.equals("1")) {
                    if (email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(this, "Please enter your password, required", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MainActivity.this, BiometricActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("phone", phone);
                        intent.putExtra("sex", sex);
                        intent.putExtra("dob", dob);
                        intent.putExtra("nid", nid);
                        intent.putExtra("province_id", province_id);
                        intent.putExtra("district_id", district_id);
                        intent.putExtra("id", id);
                        intent.putExtra("profile", profile);
                        intent.putExtra("biometric", biometric);
                        intent.putExtra("email", email);
                        intent.putExtra("password", password);
                        startActivity(intent);

                    }
                }
                break;
        }
    }


    private void checkusernid() {
        progressDialog.show();

        // create user body object
        Population obj = new Population();
        obj.setNid(nidentered);

        Map<String, String> param = new HashMap<>();
        param.put("nid", nidentered);


        Call<ResponseBody> request = RESTApiClient.getInstance().getApi().checknid(param);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    progressDialog.dismiss();


                    try {
                        String jsondata = response.body().string();

                        if (jsondata != null) {
                            JSONObject reader = new JSONObject(jsondata);

                            JSONObject data = reader.getJSONObject("data");
                            name = data.getString("name");
                            phone = data.getString("phone");
                            sex = data.getString("sex");
                            dob = data.getString("dob");
                            nid = data.getString("nid");
                            province_id = data.getString("province_id");
                            district_id = data.getString("district_id");
                            id = data.getString("id");
                            biometric = data.getString("biometric");
                            profile = data.getString("profile");

                            checkcontinue = "1";

                            tvnames.setText("Names              " + name);
                            tvphone.setText("Telephone          " + phone);
                            tvsex.setText("Sex                  " + sex);
                            tvdob.setText("Date OF birth         " + dob);
                            tvnid.setText("National ID number     " + nid);

                            lay_showdata.setVisibility(View.VISIBLE);

                            btnnexttobio.setVisibility(View.VISIBLE);
                            imgprofile.setVisibility(View.VISIBLE);

                            imgprofile.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.profile));


                        } else {
                            Toast.makeText(MainActivity.this, "No data is found,sorry", Toast.LENGTH_LONG).show();

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    //   Toast.makeText(MainActivity.this, "Good,You have an account, first we need to check your fingerprint", Toast.LENGTH_LONG).show();

                    //go check the biometric
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Sorry,you are not registered in NIDA, first be registered" + "\n" + "Response code = " + response.code() + "\n" + response.errorBody(), Toast.LENGTH_LONG).show();
                    lay_message.setVisibility(View.VISIBLE);
                    btnnexttobio.setVisibility(View.GONE);
                    imgprofile.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


}
