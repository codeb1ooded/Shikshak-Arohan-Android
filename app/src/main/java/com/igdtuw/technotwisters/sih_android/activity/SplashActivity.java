package com.igdtuw.technotwisters.sih_android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.constants.SharedPreferencesStrings;

/**
 * Created by Admin on 18-03-2017.
 */

public class SplashActivity extends AppCompatActivity implements SharedPreferencesStrings{

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        sharedPreferences = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    boolean signedIn = sharedPreferences.getBoolean(SP_USER_TOKEN_GRANTED, false);
                    Intent intent = new Intent();
                    //If already signed in than open the DashboardActivity else open the LoginActivity
                    if(signedIn){
                        // set the new task and clear flags
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setClass(SplashActivity.this, DashboardActivity.class);
                    }
                    else{
                        // set the new task and clear flags
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setClass(SplashActivity.this, MainActivity.class);
                    }
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}

