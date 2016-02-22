package com.jude.know.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.google.gson.Gson;
import com.jude.beam.model.AbsModel;
import com.jude.know.bean.Token;
import com.jude.know.config.Dir;
import com.jude.know.model.server.DaggerServiceModelComponent;
import com.jude.know.model.server.SchedulerTransform;
import com.jude.utils.JFileManager;
import com.jude.utils.JUtils;
import com.qiniu.android.storage.UploadManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by zhuchenxi on 15/7/21.
 */
public class ImageModel extends AbsModel {
    public static String UID = "";
    public static final int IMAGE_WIDTH_MAX=480;
    public static final int IMAGE_HEIGHT_MIN=800;
    public static final int COMPRESS_LEVEL = 60;

    public static final int IMAGE_SIZE_SMALL = 200;
    public static final int IMAGE_SIZE_LARGE = 1280;

    @Inject
    OkHttpClient mOkHttpClient;

    public static ImageModel getInstance() {
        return getInstance(ImageModel.class);
    }
    public static final String ADDRESS = "http://7xn814.com1.z0.glb.clouddn.com/";
    public static final String QINIU = "qiniucdn.com";
    private UploadManager mUploadManager;


    public static boolean isQiniuAddress(String address){
        return address.contains(QINIU)||address.contains("clouddn");
    }

    @Override
    protected void onAppCreate(Context ctx) {
        super.onAppCreate(ctx);
        DaggerServiceModelComponent.builder().build().inject(this);
        mUploadManager = new UploadManager();
    }

    public static String getSmallImage(String image){
        if (image==null)return null;
        if (isQiniuAddress(image)) image+="?imageView2/0/w/"+IMAGE_SIZE_SMALL;
        return image;
    }

    public static String getLargeImage(String image){
        if (image==null)return null;
        if (isQiniuAddress(image)) image+="?imageView2/0/w/"+IMAGE_SIZE_LARGE;
        return image;
    }

    public static String getSizeImage(String image,int width){
        if (image==null)return null;
        if (isQiniuAddress(image)) image+="?imageView2/0/w/"+width;
        return image;
    }

    private static String createName(File file){
        String realName = "u"+UID+System.currentTimeMillis()+file.hashCode()+".jpg";
        return realName;
    }

    private Observable<Token> getQiniuToken(){
        return Observable.create(new Observable.OnSubscribe<Token>() {
            @Override
            public void call(Subscriber<? super Token> subscriber) {
                Request request = new Request.Builder()
                        .url("http://123.56.230.6/2.0/qiniu.php")
                        .addHeader("token","abcdefg")
                        .build();
                Response response = null;
                try {
                    response = mOkHttpClient.newCall(request).execute();
                    Token token = new Gson().fromJson(response.body().string(),Token.class);
                    subscriber.onNext(token);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        }).compose(new SchedulerTransform<>());
    }


    /**
     * 同步上传
     * @param file 需上传文件
     * @return 上传文件访问地址
     */
    public Observable<String> putImageSync(final File file){
        String name = createName(file);
        return getQiniuToken()
                .flatMap(token -> Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        File f = compressImage(file);
                        String url = ADDRESS + name;

                        mUploadManager.put(f, name, token.getToken(), (key, info, response) -> {
                            if (!info.isOK()) {
                                subscriber.onError(new Throwable("key:" + key + "  info:" + info + "  response:" + response));
                            } else {
                                subscriber.onNext(url);
                            }
                            subscriber.onCompleted();
                        }, null);
                    }
                }))
                .doOnNext(s -> JUtils.Log("已上传：" + s))
                .compose(new SchedulerTransform<>());
    }


    public Observable<String> putImageSync(final File[] file){
        final int[] count = {0};
        if (file.length==0)return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onCompleted();
            }
        });
        return getQiniuToken()
                .flatMap(token -> Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        for (File temp : file) {
                            String name = createName(temp);
                            File f = compressImage(temp);
                            String url = ADDRESS + name;

                            mUploadManager.put(f, name, token.getToken(), (key, info, response) -> {
                                if (!info.isOK()) {
                                    subscriber.onError(new Throwable("key:" + key + "  info:" + info + "  response:" + response));
                                } else {
                                    subscriber.onNext(url);
                                }
                                count[0]++;
                                if (count[0] == file.length)subscriber.onCompleted();
                            }, null);
                        }
                    }
                }))
                .doOnNext(s -> JUtils.Log("已上传：" + s))
                .compose(new SchedulerTransform<>());
    }


    private File compressImage(File file){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        int reqHeight=IMAGE_HEIGHT_MIN;
        int reqWidth=IMAGE_WIDTH_MAX;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap= BitmapFactory.decodeFile(file.getPath(), options);
        File tempfile =  createTempImage();
        FileOutputStream baos;
        try {
            baos = new FileOutputStream(tempfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_LEVEL, baos);
            baos.close();
        } catch (IOException e) {
            return null;
        }
        return tempfile;
    }

    private File createTempImage(){
        String state = Environment.getExternalStorageState();
        String name = Math.random()*10000+System.nanoTime()+".jpg";
        if(state.equals(Environment.MEDIA_MOUNTED)){
            // 已挂载
            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File tmpFile = new File(pic, name);
            return tmpFile;
        }else{
            File cacheDir = JFileManager.getInstance().getFolder(Dir.Image).getFile();
            File tmpFile = new File(cacheDir, name);
            return tmpFile;
        }
    }

}
