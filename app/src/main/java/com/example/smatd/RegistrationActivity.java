package com.example.smatd;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    TextView haveAccount, checkin, set_pass;
    EditText id, number, password, con_pass;

    String DEVICE_TOKEN = "abcdefg";
    // Button checking


    String input_rfid, input_mobile, status, staus_code, msg , input_password , input_password_confirm;

    TextInputLayout inputRfid, inputNumber, inputPassword, inputConfirmPassword;
    LinearLayout inputcheckButton, inputPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        final MyPreferenceManager pref = new MyPreferenceManager(RegistrationActivity.this);



        haveAccount = findViewById(R.id.have_account);
        checkin = findViewById(R.id.regBut);
        set_pass = findViewById(R.id.set_password_button);



        inputRfid = findViewById(R.id.input_rfid_id);
        inputNumber = findViewById(R.id.input_mobile_id);
        inputPassword = findViewById(R.id.input_password_id);
        inputConfirmPassword = findViewById(R.id.input_confirm_password_id);

        inputcheckButton = findViewById(R.id.input_check_button);
        inputPasswordButton = findViewById(R.id.input_password_button);


        id = findViewById(R.id.rfid_id);
        number = findViewById(R.id.phone);
        password = findViewById(R.id.new_password);
        con_pass = findViewById(R.id.confirm_password);


        if(pref.getRfid() != null && pref.getMobile() !=  null){
            id.setText(pref.getRfid());
            number.setText(pref.getMobile());
            pref.removeLoginStatus();
        }

        if(pref.getLoginStatus()){
            id.setText(pref.getRfid());
            number.setText(pref.getMobile());
            pref.removeLoginStatus();
        }



        /* if id and phone no. exits
           the user will be asked for password to login
           else
           he will ne asked to give a new password for registration
         */


        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id.getText().toString().equals("") || number.getText().toString().equals("")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationActivity.this);
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

                    Log.d("MY_APP" , "...........User input dise............");

                    input_rfid = id.getText().toString();
                    input_mobile = number.getText().toString();



                    Call<ResponseBody> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .check(input_rfid, input_mobile);

                    final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Processing request...");
                    progressDialog.show();


                    call.enqueue(new Callback<ResponseBody>() {
                        @SuppressLint("LogConditional")
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            String resp = null;
                            try {

                                if (response.body() != null){
                                    resp = response.body().string();
                                    progressDialog.dismiss();
                                   // Log.d("MY_APP" , "json data : "+ resp);
                                }else{
                                    Log.d("MY_APP" , " kisu hoitase naki ");
                                }


                                Log.d("MY_APP" , "json data : "+ resp);



                                try {
                                    //JSONArray jsonArray = new JSONArray(resp);
                                    JSONObject jsonObject = new JSONObject(resp);

                                    JSONObject jsonObject1 = jsonObject.getJSONObject("Status");
                                    //JSONObject jsonObject2 = jsonObject1.getJSONObject("Status_Code");

                                   staus_code = jsonObject1.getString("Status_Code");

                                    Log.d("MY_APP" , " arraylength " + staus_code );




                                    if(staus_code.equals("999")){

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationActivity.this);
                                        alertDialogBuilder.setTitle("Login Error");
                                        alertDialogBuilder.setMessage("No user found with this RFID");
                                        alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                        alertDialogBuilder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        alertDialogBuilder.setCancelable(false);
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();



                                    }else if(staus_code.equals("101")){

                                        pref.setData(input_rfid , input_mobile);
                                        pref.setLoginStatus();



                                        inputRfid.setVisibility(View.GONE);
                                        inputNumber.setVisibility(View.GONE);
                                        inputcheckButton.setVisibility(View.GONE);

                                        inputPassword.setVisibility(View.VISIBLE);
                                        inputConfirmPassword.setVisibility(View.VISIBLE);
                                        inputPasswordButton.setVisibility(View.VISIBLE);



                                    }else if(staus_code.equals("102")){
                                        pref.setData(input_rfid , input_mobile);
                                        pref.setLoginStatus();



                                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                        intent.putExtra("rfid", input_rfid);
                                        intent.putExtra("mobile", input_mobile);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();

                                    }


                                    Log.d("MY_APP" , " arraylength " + jsonObject1.toString() );
                                   Log.d("MY_APP" , " arraylength " + staus_code );



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("MY_APP" , "-------------json fail korse---------");
                            Log.d("MY_APP" , t.getMessage());
                            progressDialog.dismiss();
                        }

                    });

                    Log.d("MY_APP" , "status code : "+ staus_code );

                }

            }
        });



        set_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_password = password.getText().toString().trim();
                input_password_confirm = con_pass.getText().toString().trim();

                Log.d("MY_APP" , input_rfid  + input_mobile );

                if(input_password.equals(input_password_confirm)){

                    Call<ResponseBody> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .updatePass(input_rfid , input_mobile  , input_password +" ");

                    final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Processing request...");
                    progressDialog.show();


                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            String resp = null;
                            try {

                                if (response.body() != null){
                                    resp = response.body().string();
                                    progressDialog.dismiss();
                                    // Log.d("MY_APP" , "json data : "+ resp);
                                }else{
                                    Log.d("MY_APP" , " kisu hoitase naki ");
                                }


                                Log.d("MY_APP" , "json data : "+ resp);



                                try {
                                    //JSONArray jsonArray = new JSONArray(resp);
                                    JSONObject jsonObject = new JSONObject(resp);

                                    JSONObject jsonObject1 = jsonObject.getJSONObject("Status");
                                    //JSONObject jsonObject2 = jsonObject1.getJSONObject("Status_Code");

                                    staus_code = jsonObject1.getString("Status_Code");


                                    if(staus_code.equals("103")){

                                        Intent intent = new Intent(RegistrationActivity.this, UserDashboard.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();

                                        Toast.makeText(RegistrationActivity.this, "You have registered to our system successfully ", Toast.LENGTH_LONG).show();

                                    }


                                    Log.d("MY_APP" , " arraylength " + jsonObject1.toString() );
                                    Log.d("MY_APP" , " arraylength " + staus_code );



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("MY_APP" , "-------------json fail korse---------");
                            progressDialog.dismiss();
                        }

                    });



                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RegistrationActivity.this);
                    alertDialogBuilder.setTitle("Password Mismatch");
                    alertDialogBuilder.setMessage("Please fill up the required fields with similar password.");
                    alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialogBuilder.setCancelable(false);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }


            }
        });


    }

    private void getDeviceToken() {
        FirebaseApp.initializeApp(RegistrationActivity.this);
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
