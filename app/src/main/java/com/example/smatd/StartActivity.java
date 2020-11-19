package com.example.smatd;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

public class StartActivity extends AppCompatActivity {

    String DEVICE_TOKEN = "";
    String staus_code = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDeviceToken();


        final MyPreferenceManager pref = new MyPreferenceManager(StartActivity.this);

        Log.d("MY_APP" , "Pref user status "+ pref.getLoginStatus());

        Log.d("MY_APP" , "Pref user status "+ pref.getPass().isEmpty());

        if(internetOn()){
            if(pref.getLoginStatus() && !pref.getPass().isEmpty()){

                final String input_rfid = pref.getRfid();
                final String input_mobile = pref.getMobile();

                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getApi()
                        .check(input_rfid, input_mobile);

                call.enqueue(new Callback<ResponseBody>() {
                    @SuppressLint("LogConditional")
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String resp = null;
                        try {

                            if (response.body() != null){
                                resp = response.body().string();
                            }else{
                                Log.d("MY_APP" , " kisu hoitase naki ");
                            }

                            try {

                                JSONObject jsonObject = new JSONObject(resp);
                                JSONObject jsonObject1 = jsonObject.getJSONObject("Status");
                                staus_code = jsonObject1.getString("Status_Code");

                                if(staus_code.equals("999")){

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StartActivity.this);
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
                                    Intent intent = new Intent(StartActivity.this, UserDashboard.class);
                                    intent.putExtra("rfid", input_rfid);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();

                                }else if(staus_code.equals("102")){
                                    pref.setData(input_rfid , input_mobile);
                                    pref.setLoginStatus();
                                    Intent intent = new Intent(StartActivity.this, UserDashboard.class);
                                    intent.putExtra("rfid", input_rfid);
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
                        Log.d("MY_APP" , "-------------json fail korse---------");
                        Log.d("MY_APP" , t.getMessage());
                    }

                });

                Log.d("MY_APP" , "status code : "+ staus_code );

            }else{
                Thread welcomeThread = new Thread() {
                    @SuppressLint("LogConditional")
                    @Override
                    public void run() {
                        try {
                            super.run();
                            sleep(2000);
                        } catch (Exception ignored) {
                        } finally {
                            Log.d("MY_APP" , String.format("Token : %s", DEVICE_TOKEN));
                            Intent intent = new Intent(StartActivity.this, ProceedActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                };
                welcomeThread.start();
            }
        }else{
            androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(StartActivity.this);
            alertDialog.setTitle("Warning !")
                    .setMessage("Your internet connection is off , Please turn on mobile data or wifi and restart the app to proceed.")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            internetOn();
                        }
                    })
                    .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            alertDialog.setCancelable(false);
            alertDialog.show();
        }





    }

    private void getDeviceToken() {
        FirebaseApp.initializeApp(StartActivity.this);
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

    private boolean internetOn(){
        boolean have_wifi = false ;
        boolean have_data = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for(NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI")){
                if(info.isConnected()){
                    have_wifi=true;
                }

            }
            if (info.getTypeName().equalsIgnoreCase("MOBILE")){

                if (info.isConnected()){
                    have_data = true;
                }


            }
        }
        return have_wifi || have_data ;
    }
}
