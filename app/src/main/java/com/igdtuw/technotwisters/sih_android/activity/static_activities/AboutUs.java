package com.igdtuw.technotwisters.sih_android.activity.static_activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.igdtuw.technotwisters.sih_android.R;

/**
 * Created by Admin on 25-03-2017.
 */

public class AboutUs extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
  //      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        t1=(TextView)findViewById(R.id.text_1);
        Animation animation1 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.stretch);
        t1.startAnimation(animation1);

        t2=(TextView)findViewById(R.id.text_2);
        Animation animation2 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.stretch);
        t2.startAnimation(animation2);
        t3=(TextView)findViewById(R.id.text_3);
        Animation animation3 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.stretch);
        t3.startAnimation(animation3);
        t4=(TextView)findViewById(R.id.text_4);
        Animation animation4 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.stretch);
        t4.startAnimation(animation4);
        t5=(TextView)findViewById(R.id.text_5);
        Animation animation5 =
                AnimationUtils.loadAnimation(getApplicationContext(), R.anim.stretch);
        t5.startAnimation(animation5);
    }

}
