package com.jude.know.view;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListPopupWindow;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jude.beam.nucleus.factory.RequiresPresenter;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.know.R;
import com.jude.know.app.BaseRecyclerActivity;
import com.jude.know.model.AccountModel;
import com.jude.know.model.bean.Question;
import com.jude.know.model.bean.User;
import com.jude.know.presenter.QuestionPresenter;
import com.jude.know.util.PopupWindowsUtils;
import com.jude.utils.JUtils;


/**
 * Created by zhuchenxi on 15/6/7.
 */
@RequiresPresenter(QuestionPresenter.class)
public class QuestionActivity extends BaseRecyclerActivity<QuestionPresenter,Question> {
    private SimpleDraweeView mUserView;
    private FloatingActionButton mFAB;
    private ListPopupWindow mUserWindows;
    private int SCREEN_HEIGHT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRefreshAble();
        setLoadMoreAble();
        setSwipeAble(false);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy!=0)showFAB(dy<0);
            }
        });
        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(QuestionActivity.this, WriteQuestionActivity.class), WRITE);
            }
        });
        mFAB.post(new Runnable() {
            @Override
            public void run() {
                initAnimator();
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_question;
    }

    @Override
    protected void onRefresh() {
        getPresenter().refreshQuestion();
    }

    @Override
    protected void onLoadMore() {
        getPresenter().addQuestions();
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent) {
        return new QuestionViewHolder(parent);
    }

    private void initAnimator(){
        SCREEN_HEIGHT = JUtils.getScreenHeightWithStatusBar();
        mFABAnimator = ValueAnimator.ofInt(0,SCREEN_HEIGHT - ((int)mFAB.getY()- (Build.VERSION.SDK_INT>=21?JUtils.getStatusBarHeight():0)));
        mFABAnimator.setDuration(200);
        mFABAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mFAB.setY(SCREEN_HEIGHT - value);
            }
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
        mUserView = (SimpleDraweeView) menu.findItem(R.id.user).getActionView();

        mUserView.getHierarchy().setRoundingParams(RoundingParams.asCircle());
        mUserView.setPadding(0, 0, JUtils.dip2px(8), 0);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(JUtils.dip2px(40),JUtils.dip2px(32));
        mUserView.setLayoutParams(params);
        mUserWindows = PopupWindowsUtils.createTextListPopupWindows(this, new String[]{"修改头像", "修改姓名", "退出登录"}, new PopupWindowsUtils.PopupListener() {
            @Override
            public void onListenerPop(ListPopupWindow listp) {

            }

            @Override
            public void onListItemClickBack(ListPopupWindow popupWindow, View parent, int position) {
                mUserWindows.dismiss();
                switch (position){
                    case 0:
                        editFace();
                        break;
                    case 2:
                        signOut();
                        break;
                }
            }
        });
        mUserView.getHierarchy().setPlaceholderImage(R.drawable.ic_person);

        mUserWindows.setAnchorView(mUserView);
        mUserWindows.setWidth(JUtils.dip2px(108));
        mUserWindows.setVerticalOffset(JUtils.dip2px(8));
        setUser(user);
        return true;
    }


    private static final int WRITE = 1000;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE && resultCode == RESULT_OK){
            getPresenter().refreshQuestion();
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
    private User user = null;
    public void setUser(User user){
        this.user = user;
        if (mUserView == null)return;
        if (user==null){
            mUserView.setImageURI(JUtils.getUriFromRes(R.drawable.ic_person));
            mUserView.setOnClickListener(v->showLogin());
        }else{
            if (AccountModel.getInstance().getUser().getFace()!=null)
                mUserView.setImageURI(Uri.parse(AccountModel.getInstance().getUser().getFace()));
            mUserView.setOnClickListener(v -> mUserWindows.show());
        }
    }
    private void signOut(){
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


    private void showLogin(){
        View LoginView = LayoutInflater.from(this).inflate(R.layout.view_login,null);
        TextInputLayout tilNumber = (TextInputLayout) LoginView.findViewById(R.id.tilNumber);
        TextInputLayout tilPassword = (TextInputLayout) LoginView.findViewById(R.id.tilPassword);
        new MaterialDialog.Builder(this)
                .title("请登录")
                .customView(LoginView,false)
                .positiveText("登录")
                .negativeText("注册")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        getPresenter().login(tilNumber.getEditText().getText().toString(), tilPassword.getEditText().getText().toString());
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        showRegister();
                    }
                })
                .show();
    }

    private void showRegister(){
        View LoginView = LayoutInflater.from(this).inflate(R.layout.view_login,null);
        TextInputLayout tilNumber = (TextInputLayout) LoginView.findViewById(R.id.tilNumber);
        TextInputLayout tilPassword = (TextInputLayout) LoginView.findViewById(R.id.tilPassword);
        new MaterialDialog.Builder(this)
                .title("请注册")
                .customView(LoginView, false)
                .positiveText("注册")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        getPresenter().register(tilNumber.getEditText().getText().toString(), tilPassword.getEditText().getText().toString());
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                })
                .show();
    }

}
