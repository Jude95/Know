package com.jude.know.app;

import android.content.Context;

import com.jude.know.model.QuestionModel;
import com.jude.know.util.Utils;

import java.util.ArrayList;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class AbsModel {
    private final static AbsModel[] MODELS = {
            QuestionModel.getInstance(),
    };

    public final static void init(Context ctx){
        for (AbsModel m:MODELS) {
            m.onAppCreate(ctx);
        }
    }

    protected void onAppCreate(Context ctx){}
}
