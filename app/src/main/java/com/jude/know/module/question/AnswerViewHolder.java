package com.jude.know.module.question;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jude.know.R;
import com.jude.know.model.bean.Answer;
import com.jude.know.util.BaseViewHolder;
import com.jude.know.util.RecentDateFormater;
import com.jude.know.util.TimeTransform;

/**
 * Created by zhuchenxi on 15/6/8.
 */
public class AnswerViewHolder extends BaseViewHolder<Answer>{
    private TextView name;
    private SimpleDraweeView face;
    private TextView date;
    private TextView content;

    public AnswerViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_answer);
        name = $(R.id.name);
        face = $(R.id.face);
        date = $(R.id.date);
        content = $(R.id.content);
    }

    @Override
    public void setData(Answer data) {
        name.setText(data.getAuthorName());
        face.setImageURI(Uri.parse(data.getAuthorFace()));
        date.setText(new TimeTransform().parse("yyyy-MM-dd HH:mm:ss",data.getDate()).toString(new RecentDateFormater()));
        content.setText(data.getContent());
    }
}
