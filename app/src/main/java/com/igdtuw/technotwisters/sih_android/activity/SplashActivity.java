package com.igdtuw.technotwisters.sih_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.igdtuw.technotwisters.sih_android.R;
import com.igdtuw.technotwisters.sih_android.OtherFiles.SharedPreferencesUtils;

/**
 * Created by Admin on 18-03-2017.
 */

public class SplashActivity extends AppCompatActivity {

    SharedPreferencesUtils spUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        spUtils = new SharedPreferencesUtils(SplashActivity.this);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent();
                    //If already signed in than open the DashboardActivity else open the LoginActivity
                    if(spUtils.isLoggedIn()){
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
}

