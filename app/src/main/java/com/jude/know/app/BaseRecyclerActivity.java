package com.jude.know.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.jude.beam.nucleus.manager.Presenter;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.know.R;
import com.jude.utils.JUtils;

/**
 * Created by zhuchenxi on 15/6/8.
 */
public abstract class BaseRecyclerActivity<T extends Presenter,E> extends BaseActivity<T> {
    protected EasyRecyclerView recyclerView;
    protected DataAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        recyclerView = $(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapterWithProgress(adapter = new DataAdapter(this));
        adapter.setNoMore(R.layout.view_nomore);
    }

    protected int getLayoutRes(){
        return R.layout.activity_recyclerview;
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
        adapter.setMore(R.layout.view_moreprogress, new RecyclerArrayAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                BaseRecyclerActivity.this.onLoadMore();
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
    }

    public void refreshData(E[] data){
        adapter.clear();
        adapter.addAll(data);
    }

    public void stopLoad(){
        adapter.stopMore();
        JUtils.Log("stopLoads");
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
