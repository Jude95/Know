package com.jude.know.util;

import com.jude.http.RequestListener;
import com.jude.know.config.API;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr.Jude on 2015/5/25.
 */
public abstract class StatusCallback implements RequestListener {
    @Override
    public void onRequest() {

    }

    @Override
    public void onSuccess(String s) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(s);
            int status = jsonObject.getInt(API.KEY.STATUS);
            String info = jsonObject.getString(API.KEY.INFO);
            if (status == API.CODE.SUCCEED){
                success(info);
            }else if (status == API.CODE.PERMISSION_DENIED){
                authorizationFailure();
            }else{
                error(info);
            }
        } catch (JSONException e) {
            error("数据解析错误");
        }
    }

    @Override
    public void onError(String s) {

    }

    public abstract void success(String info);
    public void authorizationFailure(){}
    public abstract void error(String errorInfo);
}
