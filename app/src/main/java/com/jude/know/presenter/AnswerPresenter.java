package com.jude.know.presenter;

import android.os.Bundle;

import com.jude.beam.expansion.list.BeamListActivityPresenter;
import com.jude.know.bean.Answer;
import com.jude.know.bean.AnswerResult;
import com.jude.know.bean.Question;
import com.jude.know.model.QuestionModel;
import com.jude.know.model.server.ErrorTransform;
import com.jude.know.view.AnswerActivity;


/**
 * Created by zhuchenxi on 15/6/8.
 */
public class AnswerPresenter extends BeamListActivityPresenter<AnswerActivity,Answer> {
    private Question question;
    public static final int COUNT = 20;

    public Question getQuestion(){
        return question;
    }

    @Override
    protected void onCreate(AnswerActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        question = getView().getIntent().getParcelableExtra("data");
        onRefresh();
    }

    @Override
    public void onRefresh() {
        QuestionModel.getInstance().getAnswerFromServer(0,COUNT,question.getId(),false)
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                .doOnNext(v->{
                    getAdapter().removeAllHeader();
                    getAdapter().addHeader(getView().getHeader(question));
                    getAdapter().notifyDataSetChanged();
                })
                .map(AnswerResult::getAnswers)
                .unsafeSubscribe(getRefreshSubscriber());
    }

    @Override
    public void onLoadMore() {
        QuestionModel.getInstance().getAnswerFromServer(getCurPage(),COUNT,question.getId(),false)
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                .map(AnswerResult::getAnswers)
                .unsafeSubscribe(getRefreshSubscriber());
    }
}
