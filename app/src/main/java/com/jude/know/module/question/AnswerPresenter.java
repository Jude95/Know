package com.jude.know.module.question;

import android.os.Bundle;

import com.jude.know.model.QuestionModel;
import com.jude.know.model.bean.AnswerResult;
import com.jude.know.model.bean.Question;
import com.jude.know.util.DataCallback;
import com.jude.know.util.Utils;

import nucleus.manager.Presenter;

/**
 * Created by zhuchenxi on 15/6/8.
 */
public class AnswerPresenter extends Presenter<AnswerActivity> {
    private Question question;

    public Question getQuestion(){
        return question;
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        question = (Question) getView().getIntent().getSerializableExtra("question");
        getView().setQuestion(question);
        addAnswers(0);
    }

    public void addAnswers(int page){
        QuestionModel.getInstance().getAnswerFromServer(page, question.getId(), false, new DataCallback<AnswerResult>() {
            @Override
            public void success(String info, AnswerResult data) {
                if (getView()==null){
                    Utils.Log("抽风了");
                    return;
                }
                if (page == 0)
                    getView().refreshData(data.getAnswers());
                else
                    getView().addData(data.getAnswers());
            }

            @Override
            public void error(String errorInfo) {
                Utils.Toast(errorInfo);
            }
        });
    }
}
