package com.tongming.manga.mvp.view.activity;

import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;

import com.tongming.manga.R;
import com.tongming.manga.mvp.base.SwipeBackActivity;

import butterknife.BindView;

/**
 * Created by Tongming on 2016/8/13.
 */
public class WatchSettingActivity extends SwipeBackActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rg_page_mode)
    RadioGroup rgPageMode;
    @BindView(R.id.rb_vertical)
    AppCompatRadioButton rbVertical;
    @BindView(R.id.rb_horizontal)
    AppCompatRadioButton rbHorizontal;
    @BindView(R.id.sp_screen)
    AppCompatSpinner spScreen;
    private SharedPreferences sp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_watch_setting;
    }

    @Override
    protected void initView() {
        initToolbar(toolbar);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean isVertical = sp.getBoolean("isVertical", true);
        if (isVertical) {
            rbVertical.setChecked(true);
        } else {
            rbHorizontal.setChecked(true);
        }
        rgPageMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int buttonId = rgPageMode.getCheckedRadioButtonId();
                rbHorizontal.setClickable(false);
                setReadMode(buttonId);
            }
        });
        boolean isPortrait = sp.getBoolean("isPortrait", true);
        if (isPortrait) {
            spScreen.setSelection(0);
        } else {
            spScreen.setSelection(1);
        }
        spScreen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    sp.edit().putBoolean("isPortrait", true).apply();
                } else {
                    sp.edit().putBoolean("isPortrait", false).apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void setReadMode(int ButtonId) {
        switch (ButtonId) {
            case R.id.rb_vertical:
                sp.edit().putBoolean("isVertical", true).apply();
                break;
            case R.id.rb_horizontal:
                sp.edit().putBoolean("isVertical", false).apply();
                break;
        }
    }
}
