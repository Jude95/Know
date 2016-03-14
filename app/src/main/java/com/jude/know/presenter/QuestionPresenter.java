package com.jude.know.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.jude.beam.expansion.list.BeamListActivityPresenter;
import com.jude.know.bean.Question;
import com.jude.know.bean.QuestionResult;
import com.jude.know.bean.User;
import com.jude.know.model.AccountModel;
import com.jude.know.model.ImageModel;
import com.jude.know.model.QuestionModel;
import com.jude.know.model.server.ErrorTransform;
import com.jude.know.model.server.SchedulerTransform;
import com.jude.know.util.ProgressDialogTransform;
import com.jude.know.view.QuestionActivity;
import com.jude.know.view.WriteQuestionActivity;
import com.jude.library.imageprovider.ImageProvider;
import com.jude.library.imageprovider.OnImageSelectListener;
import com.jude.utils.JUtils;

import java.io.File;

import rx.Observable;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class QuestionPresenter extends BeamListActivityPresenter<QuestionActivity,Question> {

    private ImageProvider provider;

    public static final int COUNT = 20;

    @Override
    protected void onCreate(QuestionActivity view, Bundle savedState) {
        super.onCreate(view, savedState);
        provider = new ImageProvider(getView());
        onRefresh();
    }

    public Observable<User> getUser(){
        return AccountModel.getInstance().getAccountSubject()
                .compose(new SchedulerTransform<>());
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        QuestionModel.getInstance().getQuestionsFromServer(0,COUNT)
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                .map(QuestionResult::getQuestions)
                .unsafeSubscribe(getRefreshSubscriber());
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        QuestionModel.getInstance().getQuestionsFromServer(getCurPage(),COUNT)
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                .map(QuestionResult::getQuestions)
                .unsafeSubscribe(getMoreSubscriber());
    }

    public void startQuestion(){
        if (!AccountModel.getInstance().hasLogin())getView().showLogin();
        else getView().startActivityForResult(new Intent(getView(), WriteQuestionActivity.class), 0);
    }

    public void signOut(){
        AccountModel.getInstance().logout();
    }


    OnImageSelectListener listener = new OnImageSelectListener() {

        @Override
        public void onImageSelect() {
            getView().getExpansion().showProgressDialog("加载中");
        }

        @Override
        public void onImageLoaded(Uri uri) {
            getView().getExpansion().dismissProgressDialog();
            provider.corpImage(uri, 300, 300, new OnImageSelectListener() {
                @Override
                public void onImageSelect() {
                }

                @Override
                public void onImageLoaded(Uri uri) {
                    uploadFace(new File(uri.getPath()));
                }

                @Override
                public void onError() {
                    getView().getExpansion().dismissProgressDialog();
                    JUtils.Toast("加载错误");
                }
            });

        }

        @Override
        public void onError() {
            getView().getExpansion().dismissProgressDialog();
            JUtils.Toast("加载错误");
        }
    };
    public void editFace(int style){
        switch (style){
            case 0:
                provider.getImageFromCamera(listener);
                break;
            case 1:
                provider.getImageFromAlbum(listener);
                break;
            case 2:
                provider.getImageFromNet(listener);
                break;
        }
    }

    private void uploadFace(File file){
        Observable.just(file)
                .flatMap(file1 -> ImageModel.getInstance().putImageSync(file1))
                .flatMap(path -> AccountModel.getInstance().modifyFace(path))
                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                .compose(new ProgressDialogTransform<>(getView(),"上传中"))
                .subscribe(o -> {
                    JUtils.Toast("上传成功");
                });
    }



    @Override
    protected void onResult(int requestCode, int resultCode, Intent data) {
        super.onResult(requestCode, resultCode, data);
        provider.onActivityResult(requestCode, resultCode, data);
    }
}
