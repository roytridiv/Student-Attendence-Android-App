package com.example.smatd;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {


    EditText user_rfid , user_ver_code , user_new_pass;

    TextView user_confirmation;
    String input_rfid, input_code, status, staus_code, msg , input_new_password , input_password_confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        final MyPreferenceManager pref = new MyPreferenceManager(ForgotPassword.this);

        user_rfid=findViewById(R.id.user_rfid);
        user_ver_code = findViewById(R.id.ver_code);
        user_new_pass= findViewById(R.id.new_password);

        user_confirmation=findViewById(R.id.conform_button);

        user_rfid.setText(pref.getRfid());


        user_confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (user_rfid.getText().toString().equals("") || user_ver_code.getText().toString().equals("")|| user_new_pass.getText().toString().equals("")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgotPassword.this);
                    alertDialogBuilder.setTitle("Error!");
                    alertDialogBuilder.setMessage("Please fill up the required fields ");
                    alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialogBuilder.setCancelable(false);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {

                    Log.d("MY_APP" , "...........dhukse............");

                    input_rfid = user_rfid.getText().toString();
                    input_code = user_ver_code.getText().toString();
                    input_new_password = user_new_pass.getText().toString();




                    Call<ResponseBody> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .forgotPassowrd(input_rfid, input_code , input_new_password);

                    final ProgressDialog progressDialog = new ProgressDialog(ForgotPassword.this);
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


//                                    if(staus_code.equals("999")){
//
//                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgotPassword.this);
//                                        alertDialogBuilder.setTitle("Login Error");
//                                        alertDialogBuilder.setMessage("No user found with this RFID");
//                                        alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialogInterface, int i) {
//                                            }
//                                        });
//                                        alertDialogBuilder.setCancelable(false);
//                                        AlertDialog alertDialog = alertDialogBuilder.create();
//                                        alertDialog.show();
//
//
//
//                                    }else if(staus_code.equals("101")){
//
//                                        pref.setData(input_rfid , input_mobile);
//                                        pref.setLoginStatus();
//
//
//
//                                        inputRfid.setVisibility(View.GONE);
//                                        inputNumber.setVisibility(View.GONE);
//                                        inputcheckButton.setVisibility(View.GONE);
//
//                                        inputPassword.setVisibility(View.VISIBLE);
//                                        inputConfirmPassword.setVisibility(View.VISIBLE);
//                                        inputPasswordButton.setVisibility(View.VISIBLE);
//
//
//
//                                    }else if(staus_code.equals("102")){
//                                        pref.setData(input_rfid , input_mobile);
//                                        pref.setLoginStatus();
//
//
//
//                                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
//                                        intent.putExtra("rfid", input_rfid);
//                                        intent.putExtra("mobile", input_mobile);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        startActivity(intent);
//                                        finish();
//
//                                    }


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

                    Log.d("MY_APP" , "sggsfgsdfg : "+ staus_code );


                }


            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgotPassword.this , LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
