package com.jude.know.model;

import android.content.Context;
import com.jude.beam.model.AbsModel;
import com.jude.http.RequestManager;
import com.jude.know.config.API;
import com.jude.know.model.callback.DataCallback;
import com.jude.library.imageprovider.Utils;
import com.jude.utils.JUtils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by zhuchenxi on 15/7/21.
 */
public class RemoteFileModel extends AbsModel {
    public static RemoteFileModel getInstance() {
        return getInstance(RemoteFileModel.class);
    }
    public static final String ADDRESS = "http://7xkr5d.com2.z0.glb.qiniucdn.com/";

    private UploadManager mUploadManager;
    public interface UploadImageListener{
        void onComplete(SizeImage path);
        void onError();
    }

    public class Token{
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }


    @Override
    protected void onAppCreate(Context ctx) {
        super.onAppCreate(ctx);
        mUploadManager = new UploadManager();
    }

    /**
     *
     * @param file 需上传文件
     * @return 上传文件访问地址
     */
    public SizeImage putImage(final File file,final UploadImageListener listener){
        String realName = "u"+AccountModel.getInstance().getUser().getId()+System.currentTimeMillis()+".jpg";
        String path = ADDRESS+realName;
        final SizeImage img = new SizeImage(path+"?imageView2/0/w/360",path+"?imageView2/0/w/1024",path);
        updateToken(new DataCallback<Token>() {
            @Override
            public void failure(String info) {
                Utils.Log(info);
                listener.onError();
            }

            @Override
            public void error(String errorInfo) {
                Utils.Log(errorInfo);
                listener.onError();
            }

            @Override
            public void success(String info, Token data) {
                mUploadManager.put(file, realName, data.getToken(), new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK())listener.onComplete(img);
                        else listener.onError();
                        JUtils.Log("QINIU:" + info.toString());
                    }
                }, null);
            }
        });
        return img;
    }

    public void updateToken(DataCallback<Token> callback){
        RequestManager.getInstance().post(API.URL.QiniuToken, null, callback);
    }

    public class SizeImage{
        private String smallImage;
        private String largeImage;
        private String originalImage;

        public String getOriginalImage() {
            return originalImage;
        }

        public void setOriginalImage(String originalImage) {
            this.originalImage = originalImage;
        }

        public String getSmallImage() {
            return smallImage;
        }

        public void setSmallImage(String smallImage) {
            this.smallImage = smallImage;
        }

        public String getLargeImage() {
            return largeImage;
        }

        public void setLargeImage(String largeImage) {
            this.largeImage = largeImage;
        }

        public SizeImage(String smallImage, String largeImage, String originalImage) {
            this.smallImage = smallImage;
            this.largeImage = largeImage;
            this.originalImage = originalImage;
        }
    }

}
