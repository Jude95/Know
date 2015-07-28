package com.jude.know.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.jude.beam.nucleus.factory.RequiresPresenter;
import com.jude.know.R;
import com.jude.know.app.BaseActivity;
import com.jude.know.presenter.WriteQuestionPresenter;


/**
 * Created by zhuchenxi on 15/6/8.
 */
@RequiresPresenter(WriteQuestionPresenter.class)
public class WriteQuestionActivity extends BaseActivity<WriteQuestionPresenter> {
    private EditText title;
    private EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writequestion);
        title = $(R.id.title);
        content = $(R.id.content);
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
            getPresenter().publicQuestion(title.getText().toString(),content.getText().toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
