package com.jude.know.util;


import com.android.http.RequestManager;
import com.google.gson.Gson;
import com.jude.know.config.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;

/**
 * Created by zhuchenxi on 15/5/11.
 */
public abstract class DataCallback<T> implements RequestManager.RequestListener {
    private DataCallback<T> link;
    public DataCallback<T> add(DataCallback<T> other){
        other.setLink(this);
        return other;
    }

    private void setLink(DataCallback<T> link){
        this.link = link;
    }

    @Override
    public void onRequest() {
        if (link !=null)
        link.onRequest();
    }

    @Override
    public void onSuccess(String s) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(s);
            int status = jsonObject.getInt(API.KEY.STATUS);
            String info = jsonObject.getString(API.KEY.INFO);
            JSONObject dataArr = jsonObject.getJSONObject(API.KEY.DATA);
            T data = new Gson().fromJson(dataArr.toString(), ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
            if (status == API.CODE.SUCCEED){
                success(info,data);
            }else if (status == API.CODE.PERMISSION_DENIED){
                authorizationFailure();
            }else{
                error(info);
            }
        } catch (JSONException e) {
            error("数据解析错误");
        }
        if (link !=null)
        link.onSuccess(s);
    }

    @Override
    public void onError(String s) {
        error("网络错误");
        if (link !=null)
        link.onError(s);
    }

    public abstract void success(String info , T data);
    public void authorizationFailure(){}
    public abstract void error(String errorInfo);

}
