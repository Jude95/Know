package com.jude.know.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.jude.beam.bijection.Presenter;
import com.jude.know.bean.Question;
import com.jude.know.model.QuestionModel;
import com.jude.know.model.server.ErrorTransform;
import com.jude.know.util.ProgressDialogTransform;
import com.jude.know.view.WriteAnswerActivity;
import com.jude.utils.JUtils;


/**
 * Created by zhuchenxi on 15/6/9.
 */
public class WriteAnswerPresenter extends Presenter<WriteAnswerActivity> {
    public Question question;

    public void publicAnswer(String answer) {
        if (TextUtils.isEmpty(answer)){
            JUtils.Toast("请填写内容");
            return;
        }
        QuestionModel.getInstance().publicAnswer(question.getId(), answer)
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                .compose(new ProgressDialogTransform<>(getView(), "提交中"))
                .subscribe(i -> {
                    JUtils.Toast("提交成功");
                    getView().setResult(Activity.RESULT_OK);
                    getView().finish();
                });

    }

    @Override
    protected void onCreate(WriteAnswerActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        question =  getView().getIntent().getParcelableExtra("data");
    }
}
