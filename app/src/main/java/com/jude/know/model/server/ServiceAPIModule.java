package com.jude.know.model.server;


import com.jude.utils.JUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by Mr.Jude on 2015/11/18.
 */
@Module
public class ServiceAPIModule {
    @Singleton
    @Provides
    ServiceAPI provideServiceAPI(OkHttpClient okHttpClient) {
        JUtils.Log("provideServiceAPI");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServiceAPI.SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(ServiceAPI.class);
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new HeaderInterceptors())
                .build();
        return client;
    }
}
