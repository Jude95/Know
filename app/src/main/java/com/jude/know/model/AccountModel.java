package com.jude.know.model;

import android.content.Context;

import com.jude.beam.model.AbsModel;
import com.jude.http.RequestManager;
import com.jude.http.RequestMap;
import com.jude.know.app.APP;
import com.jude.know.config.API;
import com.jude.know.config.Dir;
import com.jude.know.model.bean.User;
import com.jude.know.model.callback.StatusCallback;
import com.jude.utils.JFileManager;

/**
 * Created by Mr.Jude on 2015/7/29.
 */
public class AccountModel extends AbsModel {
    private JFileManager.Folder mObjectFolder;
    private static final String AccountFileName = "Account";

    public static final AccountModel getInstance(){
        return getInstance(AccountModel.class);
    }

    @Override
    protected void onAppCreateOnBackThread(Context ctx) {
        super.onAppCreateOnBackThread(ctx);
        mObjectFolder = JFileManager.getInstance().getFolder(Dir.Object);
    }

    public User getUser(){
        return (User) mObjectFolder.readObjectFromFile(AccountFileName);
    }

    public void setUser(User user){
        mObjectFolder.writeObjectToFile(user,AccountFileName);
    }

    public void modifyFace(String path,StatusCallback callback){
        RequestMap params = new RequestMap();
        params.put("face",path);
        params.put("token", APP.getInstance().getToken());
        RequestManager.getInstance().post(API.URL.ModifyFace,params,callback);
    }
}

