package com.jude.know.presenter;

import android.os.Bundle;

import com.facebook.common.internal.Lists;
import com.jude.beam.nucleus.manager.Presenter;
import com.jude.know.model.QuestionModel;
import com.jude.know.model.bean.Question;
import com.jude.know.model.bean.QuestionResult;
import com.jude.know.util.DataCallback;
import com.jude.know.view.QuestionActivity;
import com.jude.utils.JUtils;

import java.util.ArrayList;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class QuestionPresenter extends Presenter<QuestionActivity> {
    int page = 0;
    private ArrayList<Question> arr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        refreshQuestion();
    }

    @Override
    protected void onCreateView(QuestionActivity view) {
        super.onCreateView(view);
        getView().refreshData(arr.toArray(new Question[0]));
    }

    public void refreshQuestion(){
        QuestionModel.getInstance().getQuestionsFromServer(0, new DataCallback<QuestionResult>() {
            @Override
            public void success(String info, QuestionResult data) {
                if (getView()==null)return;
                getView().refreshData(data.getQuestions());
                arr.clear();
                if (data.getQuestions()!=null)
                arr.addAll(Lists.newArrayList(data.getQuestions()));
                if (data.getTotalPage()-1 <= data.getCurPage())getView().stopLoad();
                page=0;
            }

            @Override
            public void error(String errorInfo) {
                JUtils.Toast(errorInfo);
            }
        });
    }

    public void addQuestions(){
        QuestionModel.getInstance().getQuestionsFromServer(page+1, new DataCallback<QuestionResult>() {
            @Override
            public void success(String info, QuestionResult data) {
                if (getView()==null)return;
                getView().addData(data.getQuestions());
                if (data.getQuestions()!=null)
                arr.addAll(Lists.newArrayList(data.getQuestions()));
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
