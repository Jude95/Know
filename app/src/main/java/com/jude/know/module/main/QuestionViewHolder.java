package com.jude.know.module.main;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jude.know.R;
import com.jude.know.model.bean.Question;
import com.jude.know.util.BaseViewHolder;
import com.jude.know.util.RecentDateFormater;
import com.jude.know.util.TimeTransform;

/**
 * Created by zhuchenxi on 15/6/7.
 */
public class QuestionViewHolder extends BaseViewHolder<Question> {
    private TextView title;
    private TextView name;
    private SimpleDraweeView face;
    private TextView date;
    private TextView content;


    public QuestionViewHolder(ViewGroup parent) {
        super(parent, R.layout.main_item_question);
        title = $(R.id.title);
        name = $(R.id.name);
        face = $(R.id.face);
        date = $(R.id.date);
        content = $(R.id.content);
    }

    @Override
    public void setData(Question data) {
        title.setText(data.getTitle());
        name.setText(data.getAuthorName());
        face.setImageURI(Uri.parse(data.getAuthorFace()));
        //时间解析有bug
        date.setText(new TimeTransform().parse("yyyy-MM-dd HH:mm:ss",data.getDate()).toString(new RecentDateFormater()));
        content.setText(data.getContent());
    }
}
