package com.yulius.belitungtourism;

import android.app.Application;

import com.yulius.belitungtourism.api.RequestManager;

public class BelitungApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        RequestManager.init(this);
    }
}
