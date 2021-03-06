package com.handsomexi.homework;

import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.google.gson.Gson;
import com.handsomexi.homework.bean.DaoMaster;
import com.handsomexi.homework.bean.DaoSession;
import com.handsomexi.homework.util.WebClient;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.opencv.android.OpenCVLoader;

public class Myapp extends MultiDexApplication {
    public static Myapp instances;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    public DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        //初始化Web
        WebClient.init();
        //初始化数据库
        setDatabase();
        //初始化OpenCV
        OpenCVLoader.initDebug();
        //初始化友盟
        UMConfigure.init(this,"5bc96517f1f55641580000c2","Umeng",UMConfigure.DEVICE_TYPE_PHONE,"215d4a0d631c7ba602feae7c97b3bbdb");
        MobclickAgent.setScenarioType(this,MobclickAgent.EScenarioType.E_UM_NORMAL);
        UMConfigure.setEncryptEnabled(true);
        PushAgent.getInstance(this).register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
                Log.e("PUSH","onSuccess:"+s);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("PUSH","onFailure"+s+":"+s1);

            }
        });
    }
    private void setDatabase() {
        mHelper = new DaoMaster.DevOpenHelper(this, "main", null);
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }
    public static Myapp getInstances(){
        return instances;
    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        return db;
    }


}
