package com.jude.know.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.common.internal.Lists;
import com.jude.beam.nucleus.manager.Presenter;
import com.jude.know.model.AccountModel;
import com.jude.know.model.QuestionModel;
import com.jude.know.model.RemoteFileModel;
import com.jude.know.model.bean.Question;
import com.jude.know.model.bean.QuestionResult;
import com.jude.know.model.callback.DataCallback;
import com.jude.know.model.callback.StatusCallback;
import com.jude.know.view.QuestionActivity;
import com.jude.library.imageprovider.ImageProvider;
import com.jude.library.imageprovider.OnImageSelectListener;
import com.jude.utils.JUtils;
import com.qiniu.android.storage.UploadManager;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class QuestionPresenter extends Presenter<QuestionActivity> {
    int page = 0;
    private ArrayList<Question> arr = new ArrayList<>();
    private ImageProvider provider;
    private UploadManager mUploadManager;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        refreshQuestion();
        provider = new ImageProvider(getView());
        mUploadManager = new UploadManager();
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
                if (getView() == null) return;
                getView().refreshData(data.getQuestions());
                arr.clear();
                if (data.getQuestions() != null)
                    arr.addAll(Lists.newArrayList(data.getQuestions()));
                if (data.getTotalPage() - 1 <= data.getCurPage()) getView().stopLoad();
                page = 0;
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

    public void editFace(int style){
        OnImageSelectListener listener = new OnImageSelectListener() {
            @Override
            public void onImageSelect() {
                getView().showProgress("加载中");
            }

            @Override
            public void onImageLoaded(Uri uri) {
                getView().dismissProgress();
                //开始裁剪
                provider.corpImage(uri, 300, 300, new OnImageSelectListener() {
                    @Override
                    public void onImageSelect() {
                        getView().showProgress("上传中");
                    }

                    @Override
                    public void onImageLoaded(Uri uri) {
                        RemoteFileModel.getInstance().putImage(new File(uri.getPath()), new RemoteFileModel.UploadImageListener() {
                            @Override
                            public void onComplete(RemoteFileModel.SizeImage path) {
                                AccountModel.getInstance().modifyFace(path.getOriginalImage(), new StatusCallback() {
                                    @Override
                                    public void success(String info) {
                                        JUtils.Toast("上传成功");
                                        getView().dismissProgress();
                                    }
                                });
                            }

                            @Override
                            public void onError() {
                                JUtils.Toast("上传失败");
                                getView().dismissProgress();
                            }
                        });

                    }

                    @Override
                    public void onError() {

                    }
                });
            }

            @Override
            public void onError() {
                getView().dismissProgress();
            }
        };
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



    @Override
    protected void onResult(int requestCode, int resultCode, Intent data) {
        super.onResult(requestCode, resultCode, data);
        provider.onActivityResult(requestCode, resultCode, data);
    }
}
