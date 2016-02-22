package com.jude.know.presenter;

import android.app.Activity;

import com.jude.beam.bijection.Presenter;
import com.jude.know.model.QuestionModel;
import com.jude.know.model.server.ErrorTransform;
import com.jude.know.util.ProgressDialogTransform;
import com.jude.know.view.WriteQuestionActivity;
import com.jude.utils.JUtils;


/**
 * Created by zhuchenxi on 15/6/8.
 */
public class WriteQuestionPresenter extends Presenter<WriteQuestionActivity> {

    public void publicQuestion(String title,String content){
        QuestionModel.getInstance().publicQuestion(title, content)
        .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
        .compose(new ProgressDialogTransform<>(getView(),"提交中"))
        .subscribe(i->{
            JUtils.Toast("提交成功");
            getView().setResult(Activity.RESULT_OK);
            getView().finish();
        });
    }

}
