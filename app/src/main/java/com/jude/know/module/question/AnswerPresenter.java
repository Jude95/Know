package com.jude.know.module.question;

import com.jude.know.model.QuestionModel;
import com.jude.know.model.bean.Answer;
import com.jude.know.model.bean.AnswerResult;
import com.jude.know.model.bean.Question;
import com.jude.know.model.bean.QuestionResult;
import com.jude.know.util.DataCallback;
import com.jude.know.util.Utils;

import nucleus.presenter.Presenter;

/**
 * Created by zhuchenxi on 15/6/8.
 */
public class AnswerPresenter extends Presenter<AnswerActivity> {
    private Question question;

    @Override
    protected void onTakeView(AnswerActivity view) {
        question = (Question) view.getIntent().getSerializableExtra("question");
        view.setQuestion(question);
    }

    public void addAnswers(int page){
        QuestionModel.getInstance().getAnswerFromServer(page, question.getId(), false, new DataCallback<AnswerResult>() {
            @Override
            public void success(String info, AnswerResult data) {
                if (page == 0&&getView()!=null)
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
