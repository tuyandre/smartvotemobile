package xyz.developerbab.smartvoteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.developerbab.smartvoteapp.Model.User;
import xyz.developerbab.smartvoteapp.singleton.RESTCl;

public class LoginActivity extends AppCompatActivity {
    private TextView tv_signup;
    private Button btnLoginFarmer;
    private EditText etphone, etpassword;
    private String phone, password;
    private ImageView backlogin;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCanceledOnTouchOutside(false);

        etpassword = findViewById(R.id.etPasswordlogin);
        etphone = findViewById(R.id.etPhonelogin);

        backlogin = findViewById(R.id.backlogin);
        backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ChooseActivity.class));
            }
        });
        btnLoginFarmer = findViewById(R.id.btnLoginFarmer);
        btnLoginFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = etphone.getText().toString().trim();
                password = etpassword.getText().toString().trim();
                if (phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter phone and password", Toast.LENGTH_LONG).show();
                } else {
                    loginpop();
                }

            }
        });
        tv_signup = findViewById(R.id.tv_signup);
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ChooseActivity.class));
            }
        });
    }

    // create API
    public void loginpop() {

        progressDialog.show();

        // create user body object
        User obj = new User();
        obj.setPhone(phone);
        obj.setPassword(password);

        Map<String, String> param = new HashMap<>();
        param.put("email", phone);
        param.put("password", password);

        signin(param);
    }


    private void signin(final Map<String, String> user2) {
        Call<ResponseBody> request = RESTCl.getInstance().getApi().loginforvote(user2);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Response
                if (Integer.toString(response.code()).equals("200")) {


                    try {
                        String jsondata = response.body().string();

                        if (jsondata != null) {
                            JSONObject reader = new JSONObject(jsondata);

                            JSONObject data = reader.getJSONObject("user");
                            String id = data.getString("id");
                            String names = data.getString("name");
                            String fone = data.getString("telephone");
                            String emai = data.getString("email");
                            String status = data.getString("status");
                            String level = data.getString("level");
                            String biometric = data.getString("biometric");
                            String email = data.getString("email");
                            String nid = data.getString("nid");
                            String provinceid = data.getString("province_id");
                            String district_id = data.getString("district_id");

                            //     Toast.makeText(LoginActivity.this, "Successfully loged in " +
                            //         id + "\n" + names + "\n" + fone + "\n" + emai + "\n" + level+biometric+nid, Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(LoginActivity.this, VotingMain2Activit.class);
                            intent.putExtra("names", names);
                            intent.putExtra("phone", fone);
                            intent.putExtra("level", level);
                            intent.putExtra("status", status);
                            intent.putExtra("user", id);
                            intent.putExtra("email", email);
                            intent.putExtra("biometric", biometric);
                            intent.putExtra("nid", nid);
                            intent.putExtra("province",provinceid);
                            intent.putExtra("district",district_id);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                            finish();




                        } else {
                            Toast.makeText(LoginActivity.this, "Please check your internet connection,Error occured on server side", Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    etphone.getText().clear();
                    etpassword.getText().clear();

                    progressDialog.dismiss();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //failure
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "failed plz" + t.getCause(), Toast.LENGTH_LONG).show();

            }
        });
    }


}
