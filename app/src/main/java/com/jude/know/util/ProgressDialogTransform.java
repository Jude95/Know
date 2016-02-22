package com.jude.know.util;

import com.jude.beam.expansion.BeamBaseActivity;

import rx.Observable;

/**
 * Created by Mr.Jude on 2015/8/25.
 * 对服务器请求的Observer的修改
 */
public class ProgressDialogTransform<T> implements Observable.Transformer<T, T> {
    private BeamBaseActivity activity;
    private String title;

    public ProgressDialogTransform(BeamBaseActivity activity, String title) {
        this.activity = activity;
        this.title = title;
    }

    @Override
    public Observable<T> call(Observable<T> tObservable) {
        return tObservable.doOnSubscribe(()->activity.getExpansion().showProgressDialog(title))
                .finallyDo(()->activity.getExpansion().dismissProgressDialog());

    }
}
