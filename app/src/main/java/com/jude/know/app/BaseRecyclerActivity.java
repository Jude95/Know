package com.jude.know.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import com.jude.know.R;
import com.jude.know.util.BaseViewHolder;
import com.jude.know.util.RecyclerArrayAdapter;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import nucleus.presenter.Presenter;

/**
 * Created by zhuchenxi on 15/6/8.
 */
public abstract class BaseRecyclerActivity<T extends Presenter,E> extends BaseActivity<T> {
    protected SuperRecyclerView recyclerView;
    protected DataAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        recyclerView = $(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new DataAdapter(this));
    }

    public void setRefreshAble(){
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BaseRecyclerActivity.this.onRefresh();
            }
        });
    }

    public void setLoadMoreAble(){
        recyclerView.setOnMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int i, int i1, int i2) {
                onLoadMore();
            }
        });
    }

    public DataAdapter getAdapter(){
        return adapter;
    }

    protected void onRefresh(){}
    protected void onLoadMore(){}
    protected abstract BaseViewHolder getViewHolder(ViewGroup parent);

    public void addData(E[] data){
        adapter.addAll(data);
        recyclerView.hideMoreProgress();
    }

    public void refreshData(E[] data){
        adapter.clear();
        adapter.addAll(data);
    }

    protected class DataAdapter extends RecyclerArrayAdapter<E> {

        public DataAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return getViewHolder(parent);
        }
    }
}
