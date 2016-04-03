package com.fupinyou.shanci.shanciapp;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.orhanobut.logger.Logger;

/**
 * Created by fupinyou on 2016/3/31.
 */
public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
        Logger.init();
    }
}
