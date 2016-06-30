package com.jude.know.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.beam.bijection.RequiresPresenter;
import com.jude.beam.expansion.list.BeamListActivity;
import com.jude.beam.expansion.list.ListConfig;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.know.R;
import com.jude.know.bean.Answer;
import com.jude.know.bean.Question;
import com.jude.know.model.AccountModel;
import com.jude.know.presenter.AnswerPresenter;
import com.jude.know.util.RecentDateFormat;
import com.jude.utils.JTimeTransform;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * Created by zhuchenxi on 15/6/8.
 */
@RequiresPresenter(AnswerPresenter.class)
public class AnswerActivity extends BeamListActivity<AnswerPresenter,Answer> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        $(R.id.fab).setOnClickListener(v->{
            if (!AccountModel.getInstance().hasLogin())AccountModel.getInstance().showLoginDialog(this);
            else{
                Intent i = new Intent(this, WriteAnswerActivity.class);
                i.putExtra("data",getPresenter().getQuestion());
                startActivityForResult(i, 0);
            }
        });
    }

    @Override
    public BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return new AnswerViewHolder(parent);
    }

    @Override
    public ListConfig getConfig() {
        return super.getConfig().setContainerEmptyAble(false);
    }

    public RecyclerArrayAdapter.ItemView getHeader(Question question){
        return new AnswerHeader(question);
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
            Glide.with(AnswerActivity.this)
                    .load(question.getAuthorFace())
                    .error(R.drawable.ic_person_gray)
                    .placeholder(R.drawable.ic_person_gray)
                    .bitmapTransform(new CropCircleTransformation(AnswerActivity.this))
                    .into((ImageView) $(view, R.id.face));
            ((TextView)$(view,R.id.date)).setText(new JTimeTransform().parse("yyyy-MM-dd HH:mm:ss", question.getDate()).toString(new RecentDateFormat()));
            return view;
        }

        @Override
        public void onBindView(View headerView) {

        }
    }

    @Override
    public int getLayout() {
        return R.layout.activity_question;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            getListView().setRefreshing(true,true);
        }
    }

}
