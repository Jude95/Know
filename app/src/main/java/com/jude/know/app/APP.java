package com.jude.know.app;

import android.app.Application;

import com.android.http.RequestManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.jude.know.util.Utils;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class APP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RequestManager.getInstance().init(this);
        RequestManager.getInstance().setDebugMode(true,"knowNet");
        Fresco.initialize(this);
        Utils.initialize(this, "knowLog", "0");
        AbsModel.init(this);
    }

    public String getToken(){
        return Utils.getPreference().getString("token","");
    }

    public void setToken(String token){
        Utils.getPreference().edit().putString("token", token).commit();
    }
}
