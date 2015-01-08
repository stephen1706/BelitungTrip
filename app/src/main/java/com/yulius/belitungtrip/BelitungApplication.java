package com.yulius.belitungtrip;

import android.app.Application;

import com.yulius.belitungtrip.api.RequestManager;

public class BelitungApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        RequestManager.init(this);
    }
}
