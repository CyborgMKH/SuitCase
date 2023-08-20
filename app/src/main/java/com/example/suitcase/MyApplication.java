package com.example.suitcase;

import android.app.Application;
import com.gu.toolargetool.TooLargeTool;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize TooLargeTool in your Application class
        TooLargeTool.startLogging(this);
    }
}
