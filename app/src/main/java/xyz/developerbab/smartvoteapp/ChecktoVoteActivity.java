package xyz.developerbab.smartvoteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.IllegalFormatCodePointException;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.developerbab.smartvoteapp.Model.User;
import xyz.developerbab.smartvoteapp.Model.Vote;
import xyz.developerbab.smartvoteapp.singleton.RESTCl;

public class ChecktoVoteActivity extends AppCompatActivity {
    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "androidHive";
    private Cipher cipher;
    private TextView textView;
    private String user, province, district, season, candidate, candidate_name,names,phone;
    private ProgressDialog progressDialog;

    private static int SPLASH_TIME_OUT = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkto_vote);
        ckecktovote();

        Intent intent = getIntent();
        names=intent.getStringExtra("names");
        phone=intent.getStringExtra("phone");
        user = intent.getStringExtra("user");
        province = intent.getStringExtra("province");
        district = intent.getStringExtra("district");
        season = intent.getStringExtra("season");
        candidate = intent.getStringExtra("candidate");
        candidate_name = intent.getStringExtra("candidate_name");

        progressDialog = new ProgressDialog(ChecktoVoteActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");
    }


    // create API
    public void ckecktovote() {

        // Initializing both Android Keyguard Manager and Fingerprint Manager
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        }


        textView = (TextView) findViewById(R.id.errorText);


        // Check whether the device has a Fingerprint sensor.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!fingerprintManager.isHardwareDetected()) {

                textView.setText("Your Device does not have a Fingerprint Sensor");
            } else {
                // Checks whether fingerprint permission is set on manifest

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    textView.setText("Fingerprint authentication permission not enabled");
                } else {
                    // Check whether at least one fingerprint is registered
                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                        textView.setText("Register at least one fingerprint in Settings");
                    } else {
                        // Checks whether lock screen security is enabled or not
                        if (!keyguardManager.isKeyguardSecure()) {
                            textView.setText("Lock screen security not enabled in Settings");
                        } else {
                            generateKey();


                            try {
                                if (cipherInit()) {

                                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                    FingerprintHandler helper = new FingerprintHandler(this, new FingerprintHandler.CallbackInterface() {
                                        @Override
                                        public void onAuthenticationSucceed() {

                                            Toast.makeText(ChecktoVoteActivity.this, "Finger print is ok,Authorized " + "\n" + "Candidate names are " + candidate_name, Toast.LENGTH_LONG).show();
                                            voteplz();
                                        }

                                        @Override
                                        public void onAuthenticationFail() {
                                            Toast.makeText(ChecktoVoteActivity.this, "Sorry,We don't recognize your fingerprint", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    helper.startAuth(fingerprintManager, cryptoObject);
                                }
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (NoSuchPaddingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }


    }


    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }


        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() throws NoSuchAlgorithmException, NoSuchPaddingException {
        cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);


        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private void voteplz() {
        progressDialog.show();

        // create user body object
        Vote obj = new Vote();
        obj.setUser(user);
        obj.setProvince(province);
        obj.setDistrict(district);
        obj.setSeason(season);
        obj.setCandidate(candidate);

        Map<String, String> param = new HashMap<>();
        param.put("user", user);
        param.put("province", province);
        param.put("district", district);
        param.put("season", season);
        param.put("candidate", candidate);

        toraa(param);

    }

    private void toraa(final Map<String, String> user2) {
        Call<ResponseBody> request = RESTCl.getInstance().getApi().vote(user2);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Response
                if (response.code() == 200) {

                    try {
                        String jsondata = response.body().string();

                        JSONObject reader = new JSONObject(jsondata);
                        String message = reader.getString("message");
                        if (message.equals("voted")) {
                            Dialog dialog = new Dialog(ChecktoVoteActivity.this);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.layout_alreadyvoted);
                            dialog.show();

                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    startActivity(new Intent(ChecktoVoteActivity.this,LoginActivity.class));
                                    finish();

                                }
                            }, SPLASH_TIME_OUT);



                        } else if (message.equals("ok")) {
                            Dialog dialog = new Dialog(ChecktoVoteActivity.this);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.layout_chechmarkvoted);
                            dialog.show();



                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    Intent intent = new Intent(ChecktoVoteActivity.this, VotingMain2Activit.class);
                                    intent.putExtra("names", names);
                                    intent.putExtra("phone", phone);
                                    overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                                    finish();

                                }
                            }, SPLASH_TIME_OUT);


                        }


                    } catch (IOException e) {
                        e.printStackTrace();

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                    progressDialog.dismiss();

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ChecktoVoteActivity.this, "You have voted already", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //failure
                progressDialog.dismiss();
                Toast.makeText(ChecktoVoteActivity.this, "Internet connection" + t.getCause(), Toast.LENGTH_LONG).show();

            }
        });
    }


}
