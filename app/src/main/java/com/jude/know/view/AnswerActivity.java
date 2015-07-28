package com.jude.know.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jude.beam.nucleus.factory.RequiresPresenter;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.know.R;
import com.jude.know.app.BaseRecyclerActivity;
import com.jude.know.model.bean.Answer;
import com.jude.know.model.bean.Question;
import com.jude.know.presenter.AnswerPresenter;
import com.jude.know.util.RecentDateFormat;
import com.jude.utils.JTimeTransform;


/**
 * Created by zhuchenxi on 15/6/8.
 */
@RequiresPresenter(AnswerPresenter.class)
public class AnswerActivity extends BaseRecyclerActivity<AnswerPresenter,Answer> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRefreshAble();
        setLoadMoreAble();
    }

    @Override
    protected void onRefresh() {
        getPresenter().refreshAnswer();
    }

    @Override
    protected void onLoadMore() {
        getPresenter().addAnswers();
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent) {
        return new AnswerViewHolder(parent);
    }

    public void setQuestion(Question question){
        getAdapter().addHeader(new AnswerHeader(question));
    }

    class AnswerHeader implements RecyclerArrayAdapter.ItemView{
        private Question question;
        public AnswerHeader(Question question){
            this.question = question;
        }

        @Override
        public View onCreateView(ViewGroup parent) {
            View view = LayoutInflater.from(AnswerActivity.this).inflate(R.layout.item_answer_header,parent,false);
            ((TextView)$(view,R.id.title)).setText(question.getTitle());
            ((TextView)$(view,R.id.content)).setText(question.getContent());
            ((TextView)$(view,R.id.name)).setText(question.getAuthorName());
            ((SimpleDraweeView)$(view,R.id.face)).setImageURI(Uri.parse(question.getAuthorFace()));
            ((TextView)$(view,R.id.date)).setText(new JTimeTransform().parse("yyyy-MM-dd HH:mm:ss", question.getDate()).toString(new RecentDateFormat()));
            return view;
        }

        @Override
        public void onBindView(View headerView) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.answer,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.answer){
            Intent i = new Intent(this, WriteAnswerActivity.class);
            i.putExtra("question",getPresenter().getQuestion());
            startActivityForResult(i, WRITE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private static final int WRITE = 1000;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE && resultCode == RESULT_OK){
            getPresenter().refreshAnswer();
        }
    }

}
