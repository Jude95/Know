package com.jude.know.module.question;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.jude.know.R;
import com.jude.know.app.BaseActivity;

import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

/**
 * Created by zhuchenxi on 15/6/9.
 */
@RequiresPresenter(WriteAnswerPresenter.class)
public class WriteAnswerActivity extends BaseActivity<WriteAnswerPresenter> {
    private TextView question;
    private EditText answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writeanswer);
        question = $(R.id.title);
        answer = $(R.id.answer);
    }

    public void setQuestion(String title){
        question.setText(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.send){
            showProgress("发布中");
            getPresenter().publicAnswer(answer.getText().toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
