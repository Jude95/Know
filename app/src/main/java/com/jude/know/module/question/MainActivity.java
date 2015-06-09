package com.jude.know.module.question;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.jude.know.R;
import com.jude.know.app.BaseRecyclerActivity;
import com.jude.know.model.bean.Question;
import com.jude.know.util.BaseViewHolder;


import nucleus.factory.RequiresPresenter;

/**
 * Created by zhuchenxi on 15/6/7.
 */
@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BaseRecyclerActivity<MainPresenter,Question> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRefreshAble();
        setLoadMoreAble();
    }

    @Override
    protected void onRefresh() {
        getPresenter().addQuestions(0);
    }

    @Override
    protected void onLoadMore() {
        getPresenter().addQuestions(getAdapter().getPage());
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent) {
        return new QuestionViewHolder(parent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add){
            startActivityForResult(new Intent(this, WriteQuestionActivity.class), WRITE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static final int WRITE = 1000;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE && resultCode == RESULT_OK){
            getPresenter().addQuestions(0);
        }
    }
}
