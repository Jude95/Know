package com.jude.know.model;

import android.content.Context;

import com.android.http.RequestManager;
import com.android.http.RequestMap;
import com.jude.know.app.AbsModel;
import com.jude.know.config.API;
import com.jude.know.model.bean.QuestionResult;
import com.jude.know.util.DataCallback;
import com.jude.know.util.Utils;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class QuestionModel extends AbsModel {
    private static final QuestionModel model = new QuestionModel();
    public static QuestionModel getInstance(){
        return model;
    }
    @Override
    protected void onAppCreate(Context ctx) {
        Utils.Log("knowLog", "begin");
    }

    public void getQuestionsFromServer(int page,DataCallback<QuestionResult> callback){
        RequestManager.getInstance().post(API.URL.GetQuestionList, new RequestMap("page", page + ""), callback.add(new DataCallback<QuestionResult>() {
            @Override
            public void success(String info, QuestionResult data) {
                Utils.Log("total"+data.getTotalCount());
            }

            @Override
            public void error(String errorInfo) {

            }
        }));
    }
}
