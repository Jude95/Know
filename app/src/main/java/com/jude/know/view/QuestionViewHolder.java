package com.jude.know.view;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.know.R;
import com.jude.know.bean.Question;
import com.jude.know.util.RecentDateFormat;
import com.jude.utils.JTimeTransform;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class QuestionViewHolder extends BaseViewHolder<Question> {
    private TextView title;
    private TextView name;
    private ImageView face;
    private TextView date;
    private TextView content;
    private TextView answerCount;
    private Question question;
    public QuestionViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_question);
        title = $(R.id.title);
        name = $(R.id.name);
        face = $(R.id.face);
        date = $(R.id.date);
        content = $(R.id.content);
        answerCount = $(R.id.answer);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(parent.getContext(),AnswerActivity.class);
                i.putExtra("data",question);
                parent.getContext().startActivity(i);
            }
        });
    }

    @Override
    public void setData(Question data) {
        question = data;
        title.setText(data.getTitle());
        name.setText(data.getAuthorName());
        Glide.with(getContext())
                .load(data.getAuthorFace())
                .error(R.drawable.ic_person_gray)
                .placeholder(R.drawable.ic_person_gray)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(face);
        answerCount.setText(data.getAnswerCount()+"个回答");
        if(data.getRecent()!=null){
            date.setText("最近回答 "+new JTimeTransform().parse("yyyy-MM-dd HH:mm:ss",data.getRecent()).toString(new RecentDateFormat()));
        }else{
            date.setText(new JTimeTransform().parse("yyyy-MM-dd HH:mm:ss",data.getDate()).toString(new RecentDateFormat()));
        }
        if (TextUtils.isEmpty(data.getContent())){
            content.setVisibility(View.GONE);
        }else{
            content.setVisibility(View.VISIBLE);
            content.setText(data.getContent());
        }
    }
}
