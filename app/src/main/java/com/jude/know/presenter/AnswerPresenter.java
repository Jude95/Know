package com.jude.know.presenter;

import android.os.Bundle;

import com.facebook.common.internal.Lists;
import com.jude.beam.nucleus.manager.Presenter;
import com.jude.know.model.QuestionModel;
import com.jude.know.model.bean.Answer;
import com.jude.know.model.bean.AnswerResult;
import com.jude.know.model.bean.Question;
import com.jude.know.model.callback.DataCallback;
import com.jude.know.view.AnswerActivity;
import com.jude.utils.JUtils;

import java.util.ArrayList;


/**
 * Created by zhuchenxi on 15/6/8.
 */
public class AnswerPresenter extends Presenter<AnswerActivity> {
    private Question question;
    int page = 0;
    private ArrayList<Answer> arr = new ArrayList<>();

    public Question getQuestion(){
        return question;
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        question = (Question) getView().getIntent().getSerializableExtra("question");
        refreshAnswer();
    }

    @Override
    protected void onCreateView(AnswerActivity view) {
        super.onCreateView(view);
        getView().setQuestion(question);
        getView().refreshData(arr.toArray(new Answer[0]));
    }

    public void refreshAnswer(){
        QuestionModel.getInstance().getAnswerFromServer(0, question.getId(), false, new DataCallback<AnswerResult>() {
            @Override
            public void success(String info, AnswerResult data) {
                if (getView()==null){
                    return;
                }
                getView().refreshData(data.getAnswers());
                arr.clear();
                if (data.getAnswers()!=null)
                arr.addAll(Lists.newArrayList(data.getAnswers()));
                if (data.getTotalPage()-1 <= data.getCurPage())getView().stopLoad();
                page=0;
            }

            @Override
            public void error(String errorInfo) {
                JUtils.Toast(errorInfo);
            }
        });
    }

    public void addAnswers(){
        JUtils.Log("addAnswers:"+page);
        QuestionModel.getInstance().getAnswerFromServer(page+1, question.getId(), false, new DataCallback<AnswerResult>() {
            @Override
            public void success(String info, AnswerResult data) {
                if (getView()==null){
                    return;
                }
                getView().addData(data.getAnswers());
                if (data.getAnswers()!=null)
                arr.addAll(Lists.newArrayList(data.getAnswers()));
                if (data.getTotalPage()-1 <= data.getCurPage())getView().stopLoad();
                page++;
            }

            @Override
            public void error(String errorInfo) {
                JUtils.Toast(errorInfo);
            }
        });
    }
}
