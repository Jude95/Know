package com.jude.know.app;


import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jude.know.R;

import nucleus.manager.Presenter;
import nucleus.view.NucleusAppCompatActivity;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class BaseActivity<T extends Presenter> extends NucleusAppCompatActivity<T> {
    private Toolbar toolbar;
    private MaterialDialog dialog;
    protected void setToolBar(boolean returnAble){
        toolbar = $(R.id.toolbar);
        if (toolbar!=null){
            setSupportActionBar(toolbar);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(returnAble);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setToolBar(false);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setToolBar(false);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        setToolBar(false);
    }

    public void showProgress(String title){
         dialog = new MaterialDialog.Builder(this)
                .title(title)
                .content("请稍候")
                .progress(true, 100)
                .cancelable(false)
                .show();
    }

    public void dismissProgress(){
        dialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected final <E extends View> E $(View view,@IdRes int id){
        return (E)view.findViewById(id);
    }
    protected final <E extends View> E $(@IdRes int id){
        return (E)findViewById(id);
    }

}
