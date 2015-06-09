package com.jude.know.app;

import android.app.Application;

import com.android.http.RequestManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jude.know.util.Utils;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class APP extends Application {
    private static APP instance;
    public static APP getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        RequestManager.getInstance().init(this);
        RequestManager.getInstance().setDebugMode(true,"knowNet");
        Fresco.initialize(this);
        Utils.initialize(this, "knowLog", "0");
        AbsModel.init(this);
    }

    public String getToken(){
        return Utils.getPreference().getString("token","6043706ee3285eac33dcfaee9e32e576aed75948");
    }

    public void setToken(String token){
        Utils.getPreference().edit().putString("token", token).commit();
    }
}
