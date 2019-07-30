package com.example.smatd;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentInfoActivity extends AppCompatActivity {

    String rfid , staus_code ;

    TextView name , id, roll , father , mother , age , blood_group , address, mobile;

    //String fetch_name , fetch_id ,fetch_roll, fetch_father , fetch_mother , fetch_age , fetch_blood_group , fetch_address , fetch_mobile ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        final MyPreferenceManager pref = new MyPreferenceManager(StudentInfoActivity.this);

        Intent intent = getIntent();
        rfid = intent.getStringExtra("rfid");

            Log.d("MY_APP" ,"student_info jsndvjsnvjsnlvjnslfjvn : "+ rfid);

        name = findViewById(R.id.student_name);
        id = findViewById(R.id.rf_id);
        roll = findViewById(R.id.roll);
        father=findViewById(R.id.father_name);
        mother = findViewById(R.id.mother_name);
        age = findViewById(R.id.age);
        blood_group = findViewById(R.id.blood_group);
        address = findViewById(R.id.address);
        mobile = findViewById(R.id.phone_number);



        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .studentInfo(pref.getRfid());

        final ProgressDialog progressDialog = new ProgressDialog(StudentInfoActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing request...");
        progressDialog.show();


        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint({"LogConditional", "SetTextI18n"})
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

                        JSONObject jsonObject = new JSONObject(resp);

                        JSONObject jsonObject1 = jsonObject.getJSONObject("Data");

                        Log.d("MY_APP" , "Data  : "+ jsonObject1);

                        name.setText(jsonObject1.getString("NAME"));
                        id.setText("RFID number : " + jsonObject1.getString("RFID"));
                        roll.setText("Roll number : " + jsonObject1.getString("ROLL"));
                        father.setText("Father's Name : " + jsonObject1.getString("FATHER"));
                        mother.setText("Mother's Name : " + jsonObject1.getString("MOTHER"));
                        age.setText("Age : " + jsonObject1.getString("AGE"));
                        address.setText("Address : " +  jsonObject1.getString("ADDRESS"));
                        blood_group.setText("Blood Group : " + jsonObject1.getString("BLOODGROUP"));
                        mobile.setText("Phone Number : " + jsonObject1.getString("MOBILE"));

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





    }






    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StudentInfoActivity.this, UserDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
