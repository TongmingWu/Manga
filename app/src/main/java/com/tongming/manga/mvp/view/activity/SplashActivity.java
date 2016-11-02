package com.tongming.manga.mvp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Tongming on 2016/8/26.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
