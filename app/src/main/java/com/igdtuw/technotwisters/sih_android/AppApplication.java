package com.igdtuw.technotwisters.sih_android;


import android.app.Application;

import com.github.ajalt.reprint.core.Reprint;

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Reprint.initialize(this);
    }
}
