package com.example.smatd;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class UserDashboard extends AppCompatActivity {

Button logout_but ;
    CardView attendenceReport ,  classRoutine , termResult , classTeacher , principalOffice , studyMateiral , studentInfo;

    String rfid ;
    long queueid;
    DownloadManager downloadManager;

    String TAG = "MY_APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        final MyPreferenceManager pref = new MyPreferenceManager(UserDashboard.this);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){

                    Toast.makeText(UserDashboard.this , "Download Complete" , Toast.LENGTH_SHORT).show();

                    Intent i = new Intent();
                    i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }

            }
        };

        try{
            registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }catch (Exception e){

        }


        Intent intent = getIntent();
        rfid = intent.getStringExtra("rfid");



        attendenceReport = findViewById(R.id.attendance_report);
        classRoutine = findViewById(R.id.class_routine);
        termResult = findViewById(R.id.term_result);
        classTeacher = findViewById(R.id.class_teacher);
        principalOffice = findViewById(R.id.principal_office);
        studyMateiral = findViewById(R.id.study_materials);
        studentInfo = findViewById(R.id.student);
        logout_but = findViewById(R.id.logout_button);

        attendenceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, AttendanceReport.class);
                intent.putExtra("rfid", rfid);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

        classRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, ClassSchedule.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

        logout_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserDashboard.this, RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        termResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(UserDashboard.this, TermResult.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();

//                downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//
//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse("link"));
//                 queueid = downloadManager.enqueue(request);

//                if(isStoragePermissionGranted()){
//
//                    boolean s = doInBackground(pref.getRfid());
//
//                    while(s == false){
//                        s = doInBackground(pref.getRfid());
//                        if(s ==true){
//                            //progressDialog.dismiss();
//                            break;
//                        }
//                    }
//
//                    if(s== true) //progressDialog.dismiss();
//                    Log.d("MY_APP" , ""+s);
//
//                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tridivroy.xyz/smatd/"+pref.getRfid()+".pdf"));
                startActivity(browserIntent);




            }
        });

        classTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this, ClassTeacherInfo.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

        principalOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this,PrincipalOffice.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

        studyMateiral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this,StudyMaterials.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });

        studentInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboard.this,StudentInfoActivity.class);
                intent.putExtra("rfid", rfid);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });


    }


    protected Boolean doInBackground(String k) {
        boolean flag = true;
        boolean downloading =true;
        try{
            DownloadManager mManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request mRqRequest = new DownloadManager.Request(
                    Uri.parse("https://tridivroy.xyz/smatd/"+k+".jpg"));

            queueid =mManager.enqueue(mRqRequest);
            DownloadManager.Query query = null;
            query = new DownloadManager.Query();
            Cursor c = null;
            if(query!=null) {
                query.setFilterByStatus(DownloadManager.STATUS_FAILED|DownloadManager.STATUS_PAUSED|DownloadManager.STATUS_SUCCESSFUL|DownloadManager.STATUS_RUNNING|DownloadManager.STATUS_PENDING);
            } else {
                return flag;
            }

            while (downloading) {

                c = mManager.query(query);
                if(c.moveToFirst()) {
                    Log.i ("FLAG","Downloading");
                    int status =c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

                    if (status==DownloadManager.STATUS_SUCCESSFUL) {
                        Log.i ("FLAG","done");
                        downloading = false;
                        flag=true;
                        break;
                    }
                    if (status==DownloadManager.STATUS_FAILED) {
                        Log.i ("FLAG","Fail");
                        downloading = false;
                        flag=false;
                        break;
                    }
                }
            }

//            //Creating Path
//           File apkStorage = new File(
//                    Environment.getExternalStorageDirectory() + "/"
//                            + "Clipcodes");
//
//            if (!apkStorage.exists()) {
//                //Create Folder From Path
//                apkStorage.mkdir();
//            }
//
//            //Path And Filename.type
//           File outputFile = new File(apkStorage,  k+".jpg");
//
//            if (!outputFile.exists()) {
//                outputFile.createNewFile();
//                Log.e("clipcodes", "File Created");
//            }
//
//            FileOutputStream fos = new FileOutputStream(outputFile);
//
//            InputStream is = c.get
//
//            byte[] buffer = new byte[1024];
//            int len1 = 0;
//            while ((len1 = is.read(buffer)) != -1) {
//                fos.write(buffer, 0, len1);
//            }
//
//            fos.close();
//            is.close();
//


            return flag;
        }catch (Exception e) {
            flag = false;
            return flag;
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.d(TAG,"Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserDashboard.this , ProceedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }



}
