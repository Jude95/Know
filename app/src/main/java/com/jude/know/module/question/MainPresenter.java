package com.jude.know.module.question;

import com.jude.know.util.DataCallback;
import com.jude.know.model.QuestionModel;
import com.jude.know.model.bean.QuestionResult;
import com.jude.know.util.Utils;

import nucleus.manager.Presenter;

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
                if (getView()==null)return;
                if (page == 0)
                    getView().refreshData(data.getQuestions());
                else
                    getView().addData(data.getQuestions());
            }

            @Override
            public void error(String errorInfo) {
                Utils.Toast(errorInfo);
            }
        });
    }
}
