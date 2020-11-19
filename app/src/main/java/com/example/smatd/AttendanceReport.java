package com.example.smatd;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceReport extends AppCompatActivity {

    String rfid ;

    TextView last_month , this_month , remarks ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);

        final MyPreferenceManager pref = new MyPreferenceManager(AttendanceReport.this);

        Intent intent = getIntent();
        rfid = intent.getStringExtra("rfid");

        last_month = findViewById(R.id.last_month2);
        this_month = findViewById(R.id.current_month2);
        remarks = findViewById(R.id.remarks2);

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .attendanceReport(pref.getRfid());

        final ProgressDialog progressDialog = new ProgressDialog(AttendanceReport.this);
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

                        last_month.setText(jsonObject1.getString("PREVIOUS"));
                        this_month.setText(jsonObject1.getString("CURRENT"));

                        if(Integer.parseInt(jsonObject1.getString("CURRENT")) > 20 ){
                            remarks.setText("Excellent");
                            remarks.setTextColor(Color.GREEN);
                        }
                        else if(Integer.parseInt(jsonObject1.getString("CURRENT")) > 12 ){
                            remarks.setText("Good");
                            remarks.setTextColor(Color.BLUE);
                        }
                        else if(Integer.parseInt(jsonObject1.getString("CURRENT")) > 6 ){
                            remarks.setText("Average");
                            remarks.setTextColor(Color.YELLOW);
                        }
                        else{
                            remarks.setText("Bad !");
                            remarks.setTextColor(Color.RED);
                        }

//                        Toast.makeText(AttendanceReport.class,"", "", Toast.LENGTH_LONG).show();



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
        Intent intent = new Intent(AttendanceReport.this, UserDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
