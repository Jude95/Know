package com.jude.know.presenter;

import android.app.Activity;

import com.jude.beam.nucleus.manager.Presenter;
import com.jude.know.model.QuestionModel;
import com.jude.know.util.StatusCallback;
import com.jude.know.view.WriteQuestionActivity;
import com.jude.utils.JUtils;


/**
 * Created by zhuchenxi on 15/6/8.
 */
public class WriteQuestionPresenter extends Presenter<WriteQuestionActivity> {

    public void publicQuestion(String title,String content){
        QuestionModel.getInstance().publicQuestion(title, content, new StatusCallback() {
            @Override
            public void success(String info) {
                JUtils.Toast("发布成功");
                getView().dismissProgress();
                getView().setResult(Activity.RESULT_OK);
                getView().finish();
            }

            @Override
            public void error(String errorInfo) {
                getView().dismissProgress();
                JUtils.Toast(errorInfo);
            }
        });
    }
}
