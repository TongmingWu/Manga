package com.tongming.manga.mvp.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tongming.manga.R;
import com.tongming.manga.mvp.base.BaseActivity;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.bean.UserInfo;
import com.tongming.manga.mvp.presenter.LoginPresenterImp;
import com.tongming.manga.util.Base64Utils;
import com.tongming.manga.util.RSA;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Tongming on 2016/8/28.
 */
public class LoginActivity extends BaseActivity implements ILoginView {
    public static final int LOGIN_RESULT_CODE = 0x45;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pw)
    EditText etPw;
    @BindView(R.id.tv_logon)
    TextView tvLogon;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_forget)
    TextView tvForget;
    private ProgressDialog dialog;
    private SharedPreferences sp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        initToolbar(toolbar);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        String phone = sp.getString("phone", null);
        if (phone != null) {
            etPhone.setText(phone);
        }
        presenter = new LoginPresenterImp(this);
    }

    private void login() {
        if (!checkInput()) {
            return;
        }
        String password = etPw.getText().toString();
        byte[] bytes = RSA.encryptData(password.getBytes());
        if (bytes != null) {
            password = Base64Utils.encode(bytes);
        }
        String phone = etPhone.getText().toString();
        sp.edit().putString("phone", phone).apply();
        ((LoginPresenterImp) presenter).login(phone, password);
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            Toast.makeText(this, "帐号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etPw.getText().toString())) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick({R.id.tv_logon, R.id.tv_login, R.id.tv_forget})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_logon:
                startActivity(new Intent(this, LogonActivity.class));
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_forget:
                break;
        }
    }

    @Override
    public void onLogin(UserInfo info) {
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        //登录成功后得到user,将user对象保存到本地
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        sp.edit().putBoolean("isLogin", true).apply();
        //首页-->注册-->登录-->首页
        //首页-->登录-->首页
        ((LoginPresenterImp) presenter).saveUser(User.getInstance());
        Intent data = new Intent(this, HomeActivity.class);
        data.putExtra("info", info);
        startActivity(data);
    }

    @Override
    public void onSaveUser(boolean result) {
        finish();
    }

    @Override
    public void showDialog() {
        if (dialog == null) {
            dialog = ProgressDialog.show(this, null, "加载中...");
        }
    }

    @Override
    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onFail(Throwable throwable) {

    }
}
