package com.tongming.manga.mvp.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.mvp.base.SwipeBackActivity;
import com.tongming.manga.mvp.presenter.LogonPresenterImp;
import com.tongming.manga.util.RSA;

import java.io.InputStream;
import java.security.PublicKey;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Tongming on 2016/8/28.
 */
public class LogonActivity extends SwipeBackActivity implements ILogonView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pw)
    EditText etPw;
    @BindView(R.id.et_pw_again)
    EditText etPwAgain;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_code)
    Button btnCode;
    @BindView(R.id.btn_logon)
    Button btnLogon;
    private ProgressDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_logon;
    }

    @Override
    protected void initView() {
        initToolbar(toolbar);
        presenter = new LogonPresenterImp(this);
    }

    private boolean checkInput() {
        String phone = etPhone.getText().toString();
        String pw = etPw.getText().toString();
        String pwa = etPwAgain.getText().toString();
        String code = etCode.getText().toString();
        if (!checkPhone(phone)) {
            return false;
        }
        if (!TextUtils.isEmpty(pw) && !TextUtils.isEmpty(pwa)) {
            if (!pw.equals(pwa)) {
                Toast.makeText(this, "密码不正确", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkPhone(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            //用正则匹配是否为手机号
            String reg = "^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$";
            Matcher matcher = Pattern.compile(reg).matcher(phone);
            if (!matcher.find()) {
                Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getCode() {
        String phone = etPhone.getText().toString();
        if (!checkPhone(phone)) {
            return;
        }
        ((LogonPresenterImp) presenter).getCode(phone);
    }

    private void logon() {
        if (!checkInput()) {
            return;
        }
        String pwd = etPw.getText().toString();
        try {
            InputStream inputStream = getAssets().open("rsa_public_key.pem");
            PublicKey key = RSA.loadPublicKey(inputStream);
            byte[] bytes = RSA.encryptData(pwd.getBytes(), key);
            pwd = Base64.encodeToString(bytes, Base64.CRLF);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((LogonPresenterImp) presenter).logon(etPhone.getText().toString(), pwd, etCode.getText().toString());
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

    private void chronometer() {
        //验证码按钮倒计时
        btnCode.setClickable(false);
        btnCode.setBackgroundResource(R.drawable.btn_code_no_bg);
        new Thread(new Runnable() {
            int progress = 60;

            @Override
            public void run() {
                while (progress >= 0) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (progress == 0) {
                                btnCode.setClickable(true);
                                btnCode.setBackgroundResource(R.drawable.btn_code_bg);
                                btnCode.setText("重新获取");
                            } else {
                                if (btnCode != null) {
                                    btnCode.setText("重新获取(" + progress + ")");
                                }
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progress--;
                }
            }
        }).start();
    }

    @OnClick({R.id.btn_code, R.id.btn_logon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_code:
                getCode();
                //开启1分钟倒计时,按钮为不可按状态
                chronometer();
                break;
            case R.id.btn_logon:
                logon();
                break;
        }
    }

    @Override
    public void onGetCode(boolean result) {
        if (!result) {
            Toast.makeText(this, "获取验证码失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "发送验证码成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLogon(boolean result) {
        if (!result) {
            Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onFail(Throwable throwable) {
        Logger.e(throwable.getMessage());
    }
}
