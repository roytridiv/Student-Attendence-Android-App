package com.example.smatd;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegistrationActivity extends AppCompatActivity {

    TextView haveAccount , checkin , set_pass;
    EditText id , number , password , con_pass ;

    String DEVICE_TOKEN = "abcdefg";
   // Button checkin ;




    String test_token1 = "a123b";
    String test_number1 = "01680541229";
    String test_token2 = "abc";
    String test_number2 = "123456";
    String test_password= "";
    String corfirm_password = "";

    TextInputLayout inputRfid , inputNumber , inputPassword , inputConfirmPassword ;
    LinearLayout inputcheckButton , inputPasswordButton ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        haveAccount = findViewById(R.id.have_account);
        checkin = findViewById(R.id.regBut);
        set_pass = findViewById(R.id.set_password_button);

        //

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

        //fetch device token//

        getDeviceToken();

        /* if id and phone no. exits
           the user will be asked for password to login
           else
           he will ne asked to give a new password for registration
         */
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(id.getText().toString().equals("") || number.getText().toString().equals("")){
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
                }else {
                    final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Processing request...");
                    progressDialog.show();

                    Call<ResponseBody> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .check(id.getText().toString(), password.getText().toString());

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            String resp = null;
                            try {
                                resp = response.body().string();
                                try {
                                    JSONObject jsonObject = new JSONObject(resp);


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

                        Call<ResponseBody> sendToken = RetrofitClient
                                .getInstance()
                                .getApi()
                                .token(DEVICE_TOKEN);

                    });
                }





                if(id.getText().toString().equals(test_token1) == true && number.getText().toString().equals(test_number1) == true){
                    if(test_password.equals("")){

                        inputRfid.setVisibility(View.GONE);
                        inputNumber.setVisibility(View.GONE);
                        inputcheckButton.setVisibility(View.GONE);

                        inputPassword.setVisibility(View.VISIBLE);
                        inputConfirmPassword.setVisibility(View.VISIBLE);
                        inputPasswordButton.setVisibility(View.VISIBLE);

                    }else{

                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                    }
                }else if(id.getText().toString().equals(test_token2) == true && number.getText().toString().equals(test_number2) == true) {
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else {
                    Log.d("MY_APP" , "else e dhukse");
                }

            }
        });

        set_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, UserDashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                Toast.makeText(RegistrationActivity.this , "You have registered to our system successfully " , Toast.LENGTH_LONG);
            }
        });









        haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });
    }

    private void getDeviceToken(){
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
