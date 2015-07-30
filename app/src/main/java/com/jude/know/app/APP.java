package com.jude.know.app;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jude.beam.Beam;
import com.jude.http.RequestManager;
import com.jude.know.BuildConfig;
import com.jude.know.config.Dir;
import com.jude.utils.JFileManager;
import com.jude.utils.JUtils;

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
        RequestManager.getInstance().setDebugMode(true, "knowNet");
        Fresco.initialize(this);
        JUtils.initialize(this);
        JUtils.setDebug(BuildConfig.DEBUG, "knowDefault");

        JFileManager.getInstance().init(this, Dir.values());
        Beam.init(this);
    }

}
