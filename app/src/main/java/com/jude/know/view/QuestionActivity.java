package com.jude.know.view;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListPopupWindow;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.list.BeamListActivity;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.know.R;
import com.jude.know.bean.Question;
import com.jude.know.bean.User;
import com.jude.know.model.AccountModel;
import com.jude.know.presenter.QuestionPresenter;
import com.jude.know.util.PopupWindowsUtils;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.jude.utils.JUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by zhuchenxi on 15/6/7.
 */
@RequiresPresenter(QuestionPresenter.class)
public class QuestionActivity extends BeamListActivity<QuestionPresenter,Question> {
    private ImageView mUserView;
    private FloatingActionButton mFAB;
    private ListPopupWindow mUserWindows;
    private int SCREEN_HEIGHT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

//        取消蛋疼的动画
//        getListView().setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy != 0) showFAB(dy < 0);
//            }
//        });
        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(v -> getPresenter().startQuestion());
        mFAB.post(() -> initAnimator());
    }

    @Override
    public int getLayout() {
        return R.layout.activity_question;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new QuestionViewHolder(parent);
    }

    private void initAnimator(){
        SCREEN_HEIGHT = JUtils.getScreenHeightWithStatusBar();
        mFABAnimator = ValueAnimator.ofInt(0,SCREEN_HEIGHT - ((int)mFAB.getY()- (Build.VERSION.SDK_INT>=21?JUtils.getStatusBarHeight():0)));
        mFABAnimator.setDuration(200);
        mFABAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            mFAB.setY(SCREEN_HEIGHT - value);
        });
    }
    private boolean isShowed = true;
    private ValueAnimator  mFABAnimator;
    private void showFAB(boolean isShow){
        if (mFABAnimator == null || isShow==isShowed)return;
        if (isShow){
            mFABAnimator.start();
        }else{
            mFABAnimator.reverse();
        }
        isShowed = !isShowed;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mUserView = (ImageView) menu.findItem(R.id.user).getActionView();
        mUserView.setPadding(0, 0, JUtils.dip2px(8), 0);
        getPresenter().getUser().subscribe(this::setUser);


        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(JUtils.dip2px(40),JUtils.dip2px(32));
        mUserView.setLayoutParams(params);
        mUserView.setImageResource(R.drawable.ic_person_white);
        mUserWindows = PopupWindowsUtils.createTextListPopupWindows(this, new String[]{"修改头像", "退出登录"}, new PopupWindowsUtils.PopupListener() {
            @Override
            public void onListenerPop(ListPopupWindow listp) {

            }

            @Override
            public void onListItemClickBack(ListPopupWindow popupWindow, View parent, int position) {
                mUserWindows.dismiss();
                switch (position){
                    case 0:
                        RxPermissions.getInstance(QuestionActivity.this)
                                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .subscribe(granted -> {
                                    if (granted) {
                                        editFace();
                                    } else {
                                        JUtils.Toast("没有权限,臣妾办不到~");
                                    }
                                });

                        break;
                    case 1:
                        signOut();
                        break;
                }
            }
        });

        mUserWindows.setAnchorView(mUserView);
        mUserWindows.setWidth(JUtils.dip2px(108));
        mUserWindows.setVerticalOffset(JUtils.dip2px(8));
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            getListView().setRefreshing(true,true);
        }
    }

    private void editFace(){
        new MaterialDialog.Builder(this)
                .title("选择图片来源")
                .items(new String[]{"拍照","相册","网络"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        getPresenter().editFace(i);
                    }
                }).show();
    }

    public void setUser(User user){

        if (user==null){
            mUserView.setImageURI(JUtils.getUriFromRes(R.drawable.ic_person_white));
            mUserView.setOnClickListener(v->showLogin());
        }else{
            if (AccountModel.getInstance().getUser().getFace()!=null)
                Glide.with(this).load(user.getFace()).bitmapTransform(new CropCircleTransformation(this)).into(mUserView);
            mUserView.setOnClickListener(v -> mUserWindows.show());
        }
    }
    public void signOut(){
        new MaterialDialog.Builder(this)
                .title("退出登录")
                .content("您真的要退出登录吗？")
                .positiveText("退出")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        getPresenter().signOut();
                    }
                }).show();
    }


    public void showLogin(){
        AccountModel.getInstance().showLoginDialog(this);
    }

    public void showRegister(){
        AccountModel.getInstance().showRegisterDialog(this);
    }

}
