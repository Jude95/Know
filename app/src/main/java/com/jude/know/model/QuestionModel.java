package com.jude.know.model;

import android.content.Context;

import com.jude.beam.model.AbsModel;
import com.jude.know.bean.AnswerResult;
import com.jude.know.bean.Info;
import com.jude.know.bean.QuestionResult;
import com.jude.know.model.server.DaggerServiceModelComponent;
import com.jude.know.model.server.SchedulerTransform;
import com.jude.know.model.server.ServiceAPI;
import com.jude.utils.JUtils;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class QuestionModel extends AbsModel {

    public static QuestionModel getInstance(){
        return getInstance(QuestionModel.class);
    }

    @Inject
    ServiceAPI mServiceAPI;

    @Override
    protected void onAppCreate(Context ctx) {
        DaggerServiceModelComponent.builder().build().inject(this);
        JUtils.Log("onAppCreate:"+(mServiceAPI==null));
    }

    public Observable<QuestionResult> getQuestionsFromServer(int page,int count){
        return mServiceAPI.getQuestionList(page,count).compose(new SchedulerTransform<>());
    }

    public Observable<Info> publicQuestion(String title,String content){
        return mServiceAPI.question(title,content,AccountModel.getInstance().getToken()).compose(new SchedulerTransform<>());
    }

    public Observable<AnswerResult> getAnswerFromServer(int page,int count,int questionId,boolean desc){
        return mServiceAPI.getAnswerList(page,questionId,count,desc).compose(new SchedulerTransform<>());
    }

    public Observable<Info> publicAnswer(int questionId,String answer){
        return mServiceAPI.answer(questionId,answer,AccountModel.getInstance().getToken()).compose(new SchedulerTransform<>());
    }
}
