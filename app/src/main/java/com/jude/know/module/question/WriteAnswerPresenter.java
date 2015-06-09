package com.jude.know.module.question;

import android.app.Activity;

import com.jude.know.model.QuestionModel;
import com.jude.know.model.bean.Question;
import com.jude.know.util.StatusCallback;
import com.jude.know.util.Utils;

import nucleus.manager.Presenter;

/**
 * Created by zhuchenxi on 15/6/9.
 */
public class WriteAnswerPresenter extends Presenter<WriteAnswerActivity> {
    private Question question;
    @Override
    protected void onTakeView(WriteAnswerActivity view) {
        question = (Question) getView().getIntent().getSerializableExtra("question");
        view.setQuestion(question.getTitle());
    }

    public void publicAnswer(String answer){
        QuestionModel.getInstance().publicAnswer(question.getId(), answer, new StatusCallback() {
            @Override
            public void success(String info) {
                Utils.Toast("发布成功");
                getView().dismissProgress();
                getView().setResult(Activity.RESULT_OK);
                getView().finish();
            }

            @Override
            public void error(String errorInfo) {
                getView().dismissProgress();
                Utils.Toast(errorInfo);
            }
        });
    }
}
