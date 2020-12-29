package xyz.developerbab.smartvoteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.View;
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
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.developerbab.smartvoteapp.Model.Population;
import xyz.developerbab.smartvoteapp.singleton.RESTCl;

public class BiometricActivity extends AppCompatActivity {
    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "androidHive";
    private Cipher cipher;
    private TextView textView;


    private String name, phone, sex, dob, nid, province_id, district_id, id, profile, biometric, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        sex = intent.getStringExtra("sex");
        dob = intent.getStringExtra("dob");
        nid = intent.getStringExtra("nid");
        province_id = intent.getStringExtra("province_id");
        district_id = intent.getStringExtra("district_id");
        id = intent.getStringExtra("id");
        profile = intent.getStringExtra("profile");
        biometric = intent.getStringExtra("biometric");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");


        keymanager();
    }

    // create API
    public void keymanager() {

        // Initializing both Android Keyguard Manager and Fingerprint Manager
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        }


        textView = (TextView) findViewById(R.id.errorText);

     //   textView.setText(name + "\n" + phone + "\n" + sex + "\n" + dob + "\n" + nid + "\n" + province_id + "\n" + district_id + "\n" + biometric + "\n" + email + "\n" + password);


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

                                            registerforvoting();

                                            Toast.makeText(BiometricActivity.this, "Fingerprint matched with you " + name, Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onAuthenticationFail() {
                                            Toast.makeText(BiometricActivity.this, "Sorry failed,Please This not " + name + " 's Fingerprint", Toast.LENGTH_LONG).show();
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


    private void registerforvoting() {
        // create user body object
        Population obj = new Population();
        obj.setName(name);
        obj.setPhone(phone);
        obj.setSex(sex);
        obj.setDob(dob);
        obj.setNid(nid);
        obj.setProvince(province_id);
        obj.setDistrict(district_id);
        obj.setBiometric(biometric);
        obj.setEmail(email);

        Map<String, String> param = new HashMap<>();
        param.put("name", name);
        param.put("phone", phone);
        param.put("sex", sex);
        param.put("dob", dob);
        param.put("nid", nid);
        param.put("province", province_id);
        param.put("district", district_id);
        param.put("biometric", biometric);
        param.put("password", password);
        param.put("email", email);

        Call<ResponseBody> request = RESTCl.getInstance().getApi().registerforvote(param);

        request.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(BiometricActivity.this, "Registered successfully ", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(BiometricActivity.this,LoginActivity.class));
                    finish();

                } else {
                    Toast.makeText(BiometricActivity.this, "Failed to register you for this voting " + "\n" + "Response code = " + response.code(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(BiometricActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

}
