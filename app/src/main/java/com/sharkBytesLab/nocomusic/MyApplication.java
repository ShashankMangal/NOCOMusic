package com.sharkBytesLab.nocomusic;

import android.app.Application;

import com.onesignal.OneSignal;

public class MyApplication extends Application
{
    private static final String ONESIGNAL_APP_ID = "6a4b8adf-71b5-4ce3-8002-b53a344424b5";

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }

}
