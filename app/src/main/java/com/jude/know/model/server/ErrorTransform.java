package com.jude.know.model.server;

import android.app.Activity;

import com.jude.know.model.AccountModel;
import com.jude.utils.JActivityManager;
import com.jude.utils.JUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.HttpException;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Mr.Jude on 2015/8/25.
 * 对服务器请求的Observer的修改
 */
public class ErrorTransform<T> implements Observable.Transformer<T, T> {

    private Action1<Throwable> handler;
    public ErrorTransform(Action1<Throwable> handler) {
        this.handler = handler;
    }

    @Override
    public Observable<T> call(Observable<T> tObservable) {
        return tObservable.doOnError(handler)
                .onErrorResumeNext(Observable.empty());
    }


    public static class ServerErrorHandler implements Action1<Throwable> {
        private static final int W_TOAST = 1;
        private static final int W_AUTH = 2;

        public static final ServerErrorHandler NONE = new ServerErrorHandler(1<< W_TOAST);
        public static final ServerErrorHandler AUTH = new ServerErrorHandler(1<< W_AUTH);
        public static final ServerErrorHandler AUTH_TOAST = new ServerErrorHandler(1<< W_TOAST |1<< W_AUTH);


        private int kind;

        public ServerErrorHandler(int kind) {
            this.kind = kind;
        }

        private boolean has(int index){
            return (kind & 1<<index) >0;
        }

        @Override
        public void call(Throwable throwable) {
            JUtils.Log("Error:" + throwable.getClass().getName() + ":" + throwable.getMessage());
            String errorString;
            if (throwable instanceof HttpException) {
                HttpException err = (HttpException) throwable;
                if (err.code() >= 400 && err.code() < 500){
                    //添加统一的异常处理
                    if (err.code() == 401&&has(W_AUTH))authFailure();

                    try {
                        JSONObject jsonObject = new JSONObject(err.response().errorBody().string());
                        errorString = jsonObject.getString("error");
                    } catch (JSONException | IOException e) {
                        errorString = "未知错误:"+err.code()+err.message();
                    }
                }else if (err.code() >= 500){
                    errorString = "服务器错误";
                }else {
                    errorString = "请求错误:"+err.code();
                }
            }else {
                errorString = "网络错误";
            }
            if (has(W_TOAST)) JUtils.Toast(errorString);
        }

        private void authFailure(){
            Activity activity = JActivityManager.getInstance().currentActivity();
            if (activity!=null){
                AccountModel.getInstance().showLoginDialog(activity);
            }
        }
    }

}
