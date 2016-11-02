package com.tongming.manga.mvp.view.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tongming.manga.R;
import com.tongming.manga.mvp.base.SwipeBackActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Tongming on 2016/9/1.
 */
public class PersonalityActivity extends SwipeBackActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_personality)
    EditText etPersonality;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personality;
    }

    @Override
    protected void initView() {
        initToolbar(toolbar);
        String personality = getIntent().getStringExtra("personality");
        if (!TextUtils.isEmpty(personality)) {
            etPersonality.setText(personality);
        }
        etPersonality.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 120) {
                    Toast.makeText(PersonalityActivity.this, "字数已满", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick(R.id.btn_commit)
    public void onClick() {
        if (etPersonality.getText().toString().length() > 120) {
            Toast.makeText(this, "字数不能超过120", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(etPersonality.getText().toString())) {
            Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
        } else {
            setResult(RESULT_OK, new Intent().putExtra("personality", etPersonality.getText().toString()));
            finish();
        }
    }
}
