package com.jude.know.app;

import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jude.know.R;

import nucleus.presenter.Presenter;
import nucleus.view.NucleusAppCompatActivity;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class BaseActivity<T extends Presenter> extends NucleusAppCompatActivity<T> {
    private Toolbar toolbar;
    protected void setToolBar(boolean returnAble){
        toolbar = $(R.id.toolbar);
        if (toolbar!=null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(returnAble);

        }
    }

    protected final <E extends View> E $(View view,@IdRes int id){
        return (E)view.findViewById(id);
    }
    protected final <E extends View> E $(@IdRes int id){
        return (E)findViewById(id);
    }

}
