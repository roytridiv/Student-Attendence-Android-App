package com.example.smatd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

class MyPreferenceManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "REGISTER_UI";
    private static final String DEFAULT_STR_RESPONSE = "DN";
    private static final boolean DEFAULT_BOOL_RESPONSE = false;
    private static final boolean test = false;

    private static final String RFID = "RFID";
    private static final String MOBILE = "MOBILE";
    private static final String PASSWORD = "PASSWORD";

    private static final String LOGIN_STATUS = "ISLOGGEDIN";

    @SuppressLint("CommitPrefEdits")
    MyPreferenceManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editor = pref.edit();
    }

    //=======================Setters=======================

    void setData(String rfid, String mobile){
        editor.putString(RFID,rfid);
        editor.putString(MOBILE,mobile);
        editor.apply();
    }

    void setPass(String pass){
        editor.putString(PASSWORD,pass);
        editor.apply();
    }

    void setLoginStatus(){
        editor.putBoolean(LOGIN_STATUS, true);
        editor.apply();
    }

    //=======================Getters=======================

    boolean getLoginStatus(){
        return pref.getBoolean(LOGIN_STATUS,DEFAULT_BOOL_RESPONSE);
    }

    String getPass(){return  pref.getString(PASSWORD , DEFAULT_STR_RESPONSE);}

    String getRfid() {
        return pref.getString(RFID, DEFAULT_STR_RESPONSE);
    }

    String getMobile() {
        return pref.getString(MOBILE, DEFAULT_STR_RESPONSE);
    }

}


