package com.fupinyou.shanci.shanciapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

/**
 * Created by fupinyou on 2016/2/10.
 */
public class MyService extends Service {
    public static final String TAG = "MyService";
    public static final String TAG1="MyAndroidApp";
    private MyBinder mBinder = new MyBinder();
    private MainActivity mainActivity = new MainActivity();
    private DataBaseManager dataBaseManager = mainActivity.mDataBaseManager;
    public String string="";
    private int n=10;
    public static int index=1;
    private ScreenStatusReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        mReceiver = new ScreenStatusReceiver();
        registerReceiver(mReceiver, filter);
        Log.d(TAG,"onCreate() executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        Log.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Bundle bundle=intent.getExtras();
        String getString=bundle.getString("gap");
        n=Integer.parseInt(getString);
        //Log.v(TAG,String.valueOf(n));
        Log.v(TAG,getString);
        Log.v(TAG,"onBind executed");
        return mBinder;
    }

    public class MyBinder extends Binder {
        public void startToast() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String str;
                        int sign,remainder;
                        int m;
                        Log.d("TAG", "startToast() executed");
                        for (int i = 0; i < 30;i++ ) {
                            if(!ScreenStatusReceiver.wasScreenOn) {
                                Thread.sleep(10000);
                                i--;
                                continue;
                            }
                        sign = i / 5;      //每5个单词一组
                        remainder = i % 5+1;
                        m = sign / 3;      //每组循环3次
                        index = remainder + m * 5;  //计算索引，用以从稀疏数组中获取该索引对应的值
                            if(!MainActivity.aBoolean) {
                                str = dataBaseManager.sparseArray.get(index);
                                string = str;
                                Logger.v("mmmmmmmmmmmmmm", str);
                            }
                            else {
                                str=MainActivity.sparseArray.get(index);
                                string=str;
                                //Log.v("sssssssssssss",str);
                                System.out.print(str+"我我我我我我我我我我我我旺旺");
                            }
                        Thread.sleep(n);
                        handler.sendEmptyMessage(0);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        public int getIndex(){
            return index;
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //Looper.prepare();
            Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
            //Log.i(TAG1,string);
            //Looper.loop();
        }
    };
}

