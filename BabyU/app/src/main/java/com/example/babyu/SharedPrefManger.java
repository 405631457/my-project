package com.example.babyu;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManger {
    private static SharedPrefManger instance;
    private static Context ctx;

    private static final String SHARED_PREF_NAME="mysharedpref12";
    private static final String KEY_USERNAME="username";
    private static final String KEY_USER_EMAIL="useremail";
    private static final String KEY_USER_ID="userid";

    private SharedPrefManger(Context context) {
        ctx = context;

    }

    public static synchronized SharedPrefManger getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManger(context);
        }
        return instance;
    }

    public boolean userLogin(int id, String username, String email){

        SharedPreferences sharedPreferences=ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putInt(KEY_USER_ID,id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USER_EMAIL,email);

        editor.apply();

        return true;


    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences=ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USERNAME,null) != null){
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences=ctx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}


