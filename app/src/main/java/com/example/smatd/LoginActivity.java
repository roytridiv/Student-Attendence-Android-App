package com.example.smatd;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText pass;
    TextView loginButton , forgot_pass;
    String test_password1 = "000";


    String input_password, status, staus_code  , rfid , mobile;

    String DEVICE_TOKEN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final MyPreferenceManager pref = new MyPreferenceManager(LoginActivity.this);




        Intent intent = getIntent();
        rfid = intent.getStringExtra("rfid");
        mobile = intent.getStringExtra("mobile");

        pass = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        forgot_pass=findViewById(R.id.new_pass);


        if(pref.getLoginStatus()){
            pass.setText(pref.getPass());
        }


        getDeviceToken();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("MY_APP", DEVICE_TOKEN);

                input_password = pass.getText().toString();

                if (pass.getText().toString().equals("")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                    alertDialogBuilder.setTitle("Login Error");
                    alertDialogBuilder.setMessage("Please fill up the required fields to login.");
                    alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialogBuilder.setCancelable(false);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Log.d("MY_APP", " input pass " + input_password);

                    Call<ResponseBody> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .login(rfid , mobile , input_password);

                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Processing request...");
                    progressDialog.show();


                    call.enqueue(new Callback<ResponseBody>() {
                        @SuppressLint("LogConditional")
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            String resp = null;
                            try {

                                if (response.body() != null) {
                                    resp = response.body().string();
                                    progressDialog.dismiss();
                                }

                                Log.d("MY_APP", "json data : " + resp);

                                try {

                                    JSONObject jsonObject = new JSONObject(resp);

                                    JSONObject jsonObject1 = jsonObject.getJSONObject("Status");


                                    staus_code = jsonObject1.getString("Status_Code");

                                    Log.d("MY_APP", " arraylength " + jsonObject1.toString());
                                    Log.d("MY_APP", " arraylength " + staus_code);

                                    if (staus_code.equals("995")) {
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                                        alertDialogBuilder.setTitle("Login Error");
                                        alertDialogBuilder.setMessage("Incorrect password , Enter correct password");
                                        alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                        alertDialogBuilder.setCancelable(false);
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    } else if (staus_code.equals("104")) {

                                        pref.setPass(input_password);

                                        Intent intent = new Intent(LoginActivity.this, UserDashboard.class);
                                        intent.putExtra("rfid", rfid);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressDialog.dismiss();
                        }


                    });
                }
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
    }



    private void getDeviceToken() {
        FirebaseApp.initializeApp(LoginActivity.this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    //Log.w(TAG, "getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                String token = task.getResult().getToken();

                // Log and toast
                //Log.d(TAG, token);
                DEVICE_TOKEN = token;
                //Toast.makeText(RegisterActivity.this, token, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
