package com.example.smatd;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

public class PrincipalOffice extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 109;
    CardView call , appointment ;
    private int clickedDay, clickedMonth, clickedYear, currentday, currentMonth, currentYear ;
    private String selected_date;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_office);


        call = findViewById(R.id.card_call);
        appointment = findViewById(R.id.appointment_card);


        currentday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        currentYear = Calendar.getInstance().get(Calendar.YEAR);



        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog();

            }
        });


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+8801680541229"));
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(PrincipalOffice.this,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(PrincipalOffice.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    // Permission has already been granted
                    startActivity(callIntent);
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PrincipalOffice.this, UserDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(PrincipalOffice.this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1;
        this.clickedDay = dayOfMonth;
        this.clickedMonth = month;
        this.clickedYear = year;

        String conv_day, conv_month;

        if(month < 10){
            conv_month = "0" + month;
        } else {
            conv_month = String.valueOf(month);
        }

        if(dayOfMonth < 10){
            conv_day = "0" + dayOfMonth;
        } else {
            conv_day = String.valueOf(dayOfMonth);
        }

//        if (month < 10 && dayOfMonth >= 10) {
//            selected_date = "" + year + "-0" + month + "-" + dayOfMonth;
//        } else if (dayOfMonth < 10 && month > 10) {
//            selected_date = "" + year + "-" + month + "-0" + dayOfMonth;
//        } else if (month < 10 && dayOfMonth < 10) {
//            selected_date = "" + year + "-0" + month + "-0" + dayOfMonth;
//        } else {
//            selected_date = "" + year + "-" + month + "-" + dayOfMonth;
//        }

        selected_date = "" + year + "-" + conv_month + "-" + conv_day;

        verifySelectedDate();

        if (flag) {

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(PrincipalOffice.this);
            alertDialogBuilder.setTitle("Appointment Date");
            alertDialogBuilder.setMessage("You requested appointment date -"+selected_date+ " ,your request has been sent to the authority . For confirmation you will be called soon");
            alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            alertDialogBuilder.setCancelable(false);
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(PrincipalOffice.this);
            alertDialog.setTitle("Message")
                    .setMessage("Please select an Future date to set the appointment. Past date selection is not allowed.")
                    .setPositiveButton("Select Date", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showDatePickerDialog();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            alertDialog.setCancelable(false);
            alertDialog.show();

        }

    }

    public void verifySelectedDate() {
        if (clickedYear > currentYear) {
            flag = true;
        } else if (clickedYear == currentYear) {
            if (clickedMonth > currentMonth) {
                flag = true;
            } else if (clickedMonth == currentMonth) {
                flag = clickedDay >= currentday;
            }
        } else {
            flag = false;
        }
    }
}
