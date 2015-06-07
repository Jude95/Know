package com.jude.know.module.main;

import com.jude.know.model.DataCallback;
import com.jude.know.model.QuestionModel;
import com.jude.know.model.bean.QuestionResult;
import com.jude.know.util.Utils;

import nucleus.presenter.Presenter;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class MainPresenter extends Presenter<MainActivity> {

    @Override
    protected void onTakeView(MainActivity view) {
        addQuestions(0);
    }


    public void addQuestions(int page){
        QuestionModel.getInstance().getQuestionsFromServer(page, new DataCallback<QuestionResult>() {
            @Override
            public void success(String info, QuestionResult data) {
                if (page == 0)
                    getView().refreshQuestion(data.getQuestions());
                else
                    getView().addQuestion(data.getQuestions());
            }

            @Override
            public void error(String errorInfo) {
                Utils.Toast(errorInfo);
            }
        });
    }
}
