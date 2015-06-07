package com.jude.know.module.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.jude.know.R;
import com.jude.know.app.BaseActivity;
import com.jude.know.model.bean.Question;
import com.jude.know.util.BaseViewHolder;
import com.jude.know.util.RecyclerArrayAdapter;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

/**
 * Created by zhuchenxi on 15/6/7.
 */
@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BaseActivity<MainPresenter> {
    private SuperRecyclerView mRecyclerView;
    public QuestionAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        setToolBar(false);
        mRecyclerView = $(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new QuestionAdapter(this));
        mRecyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().addQuestions(0);
            }
        });
        mRecyclerView.setOnMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int i, int i1, int i2) {
                getPresenter().addQuestions(mAdapter.getPage()+1);
            }
        });
    }

    public void addQuestion(Question[] data){
        mAdapter.addAll(data);
        mRecyclerView.hideMoreProgress();
    }

    public void refreshQuestion(Question[] data){
        mAdapter.clear();
        mAdapter.addAll(data);
    }

    private class QuestionAdapter extends RecyclerArrayAdapter<Question>{

        public QuestionAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new QuestionViewHolder(parent);
        }
    }
}
