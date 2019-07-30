package com.example.smatd;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ClassSchedule extends AppCompatActivity {

    Spinner  day ;
    CardView load;
    String my_day;

    TextView sub1 , sub2 , sub3 , sub4 , sub5 , time1 , time2, time3 , time4, time5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);



        day= findViewById(R.id.spinner1);
        load = findViewById(R.id.load);
        sub1 = findViewById(R.id.sub1);
        sub2 = findViewById(R.id.sub2);
        sub3  = findViewById(R.id.sub3);
        sub4 = findViewById(R.id.sub4);
        sub5 = findViewById(R.id.sub5);
        time1 = findViewById(R.id.time1);
        time2 = findViewById(R.id.time2);
        time3 = findViewById(R.id.time3);
        time4 = findViewById(R.id.time4);
        time5 = findViewById(R.id.time5);

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              my_day = day.getSelectedItem().toString();
                if(my_day.equals("Sunday")){

                    sub1.setText("Maths");
                    time1.setText("9:00am");
                    sub2.setText("Bangla");
                    time2.setText("9:40am");
                    sub3.setText("Science");
                    time3.setText("10:20am");
                    sub4.setText("English");
                    time4.setText("11:20am");
                    sub5.setText("Social Science");
                    time5.setText("12:10am");



                }else if(my_day.equals("Monday")){
                    sub1.setText("English");
                    time1.setText("9:00am");
                    sub2.setText("Maths");
                    time2.setText("9:40am");
                    sub3.setText("Social Science");
                    time3.setText("10:20am");
                    sub4.setText("Bangla");
                    time4.setText("11:20am");
                    sub5.setText("Science");
                    time5.setText("12:10am");


                }else if(my_day.equals("Tuesday")){

                    sub1.setText("Bangla");
                    time1.setText("9:00am");
                    sub2.setText("Science ");
                    time2.setText("9:40am");
                    sub3.setText("Social Science");
                    time3.setText("10:20am");
                    sub4.setText("English");
                    time4.setText("11:20am");
                    sub5.setText("Maths");
                    time5.setText("12:10am");
                }else if(my_day.equals("Wednesday")){

                    sub1.setText("Science");
                    time1.setText("9:00am");
                    sub2.setText("Bangla");
                    time2.setText("9:40am");
                    sub3.setText("Social Science");
                    time3.setText("10:20am");
                    sub4.setText("Maths");
                    time4.setText("11:20am");
                    sub5.setText("English");
                    time5.setText("12:10am");

                }else if(my_day.equals("Thursday")){
                    sub1.setText("Social Science");
                    time1.setText("9:00am");
                    sub2.setText("Maths");
                    time2.setText("9:40am");
                    sub3.setText("Science");
                    time3.setText("10:20am");
                    sub4.setText("Bangla");
                    time4.setText("11:20am");
                    sub5.setText("English");
                    time5.setText("12:10am");
                }
            }
        });






    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ClassSchedule.this, UserDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
