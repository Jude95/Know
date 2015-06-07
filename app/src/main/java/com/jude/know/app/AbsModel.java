package com.jude.know.app;

import android.content.Context;

import com.jude.know.util.Utils;

import java.util.ArrayList;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class AbsModel {

    public final static ArrayList<AbsModel> MODELS = new ArrayList<>();

    public final static void init(Context ctx){
        //有bug。此时子类都还没有初始化
        for (AbsModel m:MODELS) {
            m.onAppCreate(ctx);
        }
    }

    protected AbsModel(){
        MODELS.add(this);
    }

    protected void onAppCreate(Context ctx){}
}
