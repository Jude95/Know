package com.jude.know.model;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jude.beam.expansion.BeamBaseActivity;
import com.jude.beam.model.AbsModel;
import com.jude.know.R;
import com.jude.know.bean.Info;
import com.jude.know.bean.User;
import com.jude.know.config.Dir;
import com.jude.know.model.server.DaggerServiceModelComponent;
import com.jude.know.model.server.ErrorTransform;
import com.jude.know.model.server.SchedulerTransform;
import com.jude.know.model.server.ServiceAPI;
import com.jude.know.util.ProgressDialogTransform;
import com.jude.utils.JFileManager;
import com.jude.utils.JUtils;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Mr.Jude on 2015/7/29.
 */
public class AccountModel extends AbsModel {

    private static final String AccountFileName = "Account";
    private BehaviorSubject<User> mAccountSubject = BehaviorSubject.create();

    @Inject
    ServiceAPI mServiceAPI;

    public static final AccountModel getInstance(){
        return getInstance(AccountModel.class);
    }

    @Override
    protected void onAppCreateOnBackThread(Context ctx) {
        super.onAppCreateOnBackThread(ctx);
        DaggerServiceModelComponent.builder().build().inject(this);

        //账号持久化
        mAccountSubject.subscribe(account -> {
            if (account == null)
                JFileManager.getInstance().getFolder(Dir.Object).deleteChild(AccountFileName);
            else
                JFileManager.getInstance().getFolder(Dir.Object).writeObjectToFile(account, AccountFileName);
        });
        //初始化账户
        Observable.just((User) JFileManager.getInstance().getFolder(Dir.Object).readObjectFromFile(AccountFileName))
                .doOnNext(mAccountSubject::onNext)
                .subscribe();
    }

    public BehaviorSubject<User> getAccountSubject(){
        return mAccountSubject;
    }

    public boolean hasLogin(){
        return mAccountSubject.getValue()!=null;
    }

    public Observable<User> login(String account, String password){
        return mServiceAPI.login(account,password)
                .compose(new SchedulerTransform<>())
                .doOnNext(mAccountSubject::onNext);
    }

    public void logout(){
        mAccountSubject.onNext(null);
    }

    public Observable<User> register(String name, String password){
        return mServiceAPI.register(name, password)
                .flatMap(infoObservable -> login(name, password))
                .compose(new SchedulerTransform<>());
    }

    public User getUser(){
        return mAccountSubject.getValue();
    }

    public String getToken(){
        return getUser()==null?"":getUser().getToken();
    }

    public Observable<Info> modifyFace(String path){
        return mServiceAPI.modifyFace(getToken(),path)
                .doOnNext(info -> {
                    User user = getUser();
                    user.setFace(path);
                    mAccountSubject.onNext(user);
                })
                .compose(new SchedulerTransform<>());
    }

    public void showLoginDialog(Context ctx){
        View LoginView = LayoutInflater.from(ctx).inflate(R.layout.view_login,null);
        TextInputLayout tilNumber = (TextInputLayout) LoginView.findViewById(R.id.tilNumber);
        TextInputLayout tilPassword = (TextInputLayout) LoginView.findViewById(R.id.tilPassword);
        new MaterialDialog.Builder(ctx)
                .title("请登录")
                .customView(LoginView, false)
                .positiveText("登录")
                .negativeText("注册")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        login(tilNumber.getEditText().getText().toString(), tilPassword.getEditText().getText().toString())
                                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                                .compose(new ProgressDialogTransform<>((BeamBaseActivity) ctx, "登陆中"))
                                .subscribe(user -> JUtils.Toast("登录成功"));
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        showRegisterDialog(ctx);
                    }
                })
                .show();
    }

    public void showRegisterDialog(Context ctx){
        View LoginView = LayoutInflater.from(ctx).inflate(R.layout.view_login,null);
        TextInputLayout tilNumber = (TextInputLayout) LoginView.findViewById(R.id.tilNumber);
        TextInputLayout tilPassword = (TextInputLayout) LoginView.findViewById(R.id.tilPassword);
        new MaterialDialog.Builder(ctx)
                .title("请注册")
                .customView(LoginView, false)
                .positiveText("注册")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        register(tilNumber.getEditText().getText().toString(), tilPassword.getEditText().getText().toString())
                                .compose(new ErrorTransform<>(ErrorTransform.ServerErrorHandler.AUTH_TOAST))
                                .compose(new ProgressDialogTransform<>((BeamBaseActivity) ctx, "注册中"))
                                .subscribe(user -> JUtils.Toast("登录成功"));
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                })
                .show();
    }
}

