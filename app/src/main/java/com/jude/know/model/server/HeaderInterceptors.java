package com.jude.know.model.server;


import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by zhuchenxi on 15/10/11.
 * 添加Header的过滤器，服务器通过header中的信息进行用户识别
 */
public class HeaderInterceptors implements Interceptor {
    public static String TOKEN = "";
    public static String UID = "";



    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!TextUtils.isEmpty(TOKEN)&&!TextUtils.isEmpty(UID))
            request = request.newBuilder()
                .addHeader("uid", UID)
                .addHeader("token", TOKEN)
                .build();
        return chain.proceed(request);
    }
}
