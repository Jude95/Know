package com.jude.know.model;

import android.content.Context;

import com.jude.beam.model.AbsModel;
import com.jude.http.RequestManager;
import com.jude.http.RequestMap;
import com.jude.know.config.API;
import com.jude.know.config.Dir;
import com.jude.know.model.bean.User;
import com.jude.know.model.callback.DataCallback;
import com.jude.know.model.callback.StatusCallback;
import com.jude.utils.JFileManager;
import com.jude.utils.JUtils;

import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by Mr.Jude on 2015/7/29.
 */
public class AccountModel extends AbsModel {
    private JFileManager.Folder mObjectFolder;
    private static final String AccountFileName = "Account";
    private BehaviorSubject<User> mUserSubject;

    public static final AccountModel getInstance(){
        return getInstance(AccountModel.class);
    }

    @Override
    protected void onAppCreateOnBackThread(Context ctx) {
        super.onAppCreateOnBackThread(ctx);
        mObjectFolder = JFileManager.getInstance().getFolder(Dir.Object);
        mUserSubject = BehaviorSubject.create();
    }

    public void registerUserSubscriber(Action1<User> subscriber){
        mUserSubject.subscribe(subscriber);
    }

    public void setUser(User user){
        if (user!=null){
            mObjectFolder.writeObjectToFile(user,AccountFileName);
            setToken(user.getToken());
            mUserSubject.onNext(user);
        }else{
            mObjectFolder.deleteChild(AccountFileName);
            setToken("");
            mUserSubject.onNext(null);
        }
    }

    public User getUser(){
        return (User) mObjectFolder.readObjectFromFile(AccountFileName);
    }

    public String getToken(){
        return JUtils.getSharedPreference().getString("token","");
    }

    private  void setToken(String token){
        JUtils.getSharedPreference().edit().putString("token", token).commit();
    }

    public void modifyFace(String path,StatusCallback callback){
        RequestMap params = new RequestMap();
        params.put("face",path);
        params.put("token", getToken());
        RequestManager.getInstance().post(API.URL.ModifyFace, params, callback.add(new StatusCallback() {
            @Override
            public void success(String info) {
                User user = getUser();
                user.setFace(path);
                setUser(user);
            }
        }));
    }

    public void login(String name,String password,DataCallback<User> callback){
        RequestMap params = new RequestMap();
        params.put("name",name);
        params.put("password", password);
        RequestManager.getInstance().post(API.URL.Login,params,callback);
    }

    public void register(String name,String password,StatusCallback callback){
        RequestMap params = new RequestMap();
        params.put("name",name);
        params.put("password", password);
        RequestManager.getInstance().post(API.URL.Register,params,callback);
    }
}

