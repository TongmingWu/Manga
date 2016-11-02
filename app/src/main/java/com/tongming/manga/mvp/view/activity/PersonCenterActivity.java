package com.tongming.manga.mvp.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.cusview.GlideGircleTransform;
import com.tongming.manga.mvp.base.SwipeBackActivity;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.bean.UserInfo;
import com.tongming.manga.mvp.presenter.PersonPresenterImp;
import com.tongming.manga.util.CommonUtil;
import com.tongming.manga.util.ImagePathUtil;

import java.io.File;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Tongming on 2016/8/30.
 */
public class PersonCenterActivity extends SwipeBackActivity implements IPersonView {
    private static final int REQUEST_PERSONALITY = 0x4545;
    private static final int REQUEST_PICTURE = 0x5432;
    private static final int CROP_SMALL_PICTURE = 0x3325;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_avatar)
    RelativeLayout rlAvatar;
    @BindView(R.id.rl_nickname)
    RelativeLayout rlNickname;
    @BindView(R.id.rl_sex)
    RelativeLayout rlSex;
    @BindView(R.id.rl_personality)
    RelativeLayout rlPersonality;
    @BindView(R.id.fab_save)
    FloatingActionButton fabSave;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_logon_date)
    TextView tvLogonDate;
    @BindView(R.id.tv_personality)
    TextView tvPersonality;
    @BindView(R.id.rl_logon_date)
    RelativeLayout rlLogonDate;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;
    private boolean changeAvatar;
    private String oldName;
    private String oldSex;
    private String oldPersonality;
    private String path;
    private ProgressDialog progressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person;
    }

    @Override
    protected void initView() {
        initToolbar(toolbar);
        User user = User.getInstance();
        UserInfo.UserBean bean = user.getInfo().getUser();
        if (!TextUtils.isEmpty(bean.getAvatar())) {
            Glide.with(this)
                    .load(bean.getAvatar())
                    .placeholder(R.drawable.default_avatar)
                    .transform(new GlideGircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivAvatar);
        }

        oldName = bean.getName();
        oldSex = bean.getSex();
        oldPersonality = bean.getPersonality();
        tvNickname.setText(oldName);
        tvSex.setText(oldSex);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String logonDate = format.format((long) bean.getLogon_date() * 1000);
        tvLogonDate.setText(logonDate);
        if (!TextUtils.isEmpty(oldPersonality)) {
            tvPersonality.setText(oldPersonality);
        } else {
            oldPersonality = getResources().getString(R.string.personality);
        }
    }


    @OnClick({R.id.rl_avatar, R.id.rl_nickname, R.id.rl_sex, R.id.rl_personality, R.id.fab_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_avatar:
                //跳转到本地图片库选择图片并裁剪
                Intent openAlbumIntent = new Intent(
                        Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setType("image/*");
                startActivityForResult(openAlbumIntent, REQUEST_PICTURE);
                break;
            case R.id.rl_nickname:
                //修改昵称
                View inflate = View.inflate(this, R.layout.dialog_nickname, null);
                final EditText etNickname = (EditText) inflate.findViewById(R.id.et_nickname);
                etNickname.setText(tvNickname.getText());
                etNickname.selectAll();
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setView(inflate)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvNickname.setText(etNickname.getText());
                                if (!oldName.equals(etNickname.getText().toString())) {
                                    tvNickname.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                                } else {
                                    tvNickname.setTextColor(getResources().getColor(R.color.gray, null));
                                }
                                showSaveButton();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                final Window window = dialog.getWindow();
                final WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.TOP;
                params.y = CommonUtil.getScreenHeight(this) / 4;
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog.show();
                //TODO 修复dialog自适应软键盘的问题
                break;
            case R.id.rl_sex:
                //修改性别
                final String[] sexArr = new String[]{"男", "女"};
                String sex = tvSex.getText().toString();
                int index = 0;
                for (int i = 0; i < sexArr.length; i++) {
                    if (sexArr[i].equals(sex)) {
                        index = i;
                        break;
                    }
                }
                new AlertDialog.Builder(this)
                        .setSingleChoiceItems(sexArr, index, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvSex.setText(sexArr[which]);
                                if (!oldSex.equals(sexArr[which])) {
                                    tvSex.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                                } else {
                                    tvSex.setTextColor(getResources().getColor(R.color.gray, null));
                                }
                                showSaveButton();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null).show();
                break;
            case R.id.rl_personality:
                //修改个性签名
                Intent intent = new Intent(this, PersonalityActivity.class);
                intent.putExtra("personality", tvPersonality.getText().toString());
                startActivityForResult(intent, REQUEST_PERSONALITY);
                break;
            case R.id.fab_save:
                //提交修改
                new AlertDialog.Builder(this)
                        .setMessage("保存用户信息")
                        .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                presenter = new PersonPresenterImp(PersonCenterActivity.this);
                                String nickname = tvNickname.getText().toString();
                                String sex = tvSex.getText().toString();
                                String personality = tvPersonality.getText().toString();
                                if (changeAvatar) {
//                                    ((PersonPresenterImp) presenter).updateUser(path, nickname, sex, personality);
                                    ((PersonPresenterImp) presenter).compressFile(path);
                                } else {
                                    ((PersonPresenterImp) presenter).updateUser(nickname, sex, personality);
                                }
                                if (progressDialog == null) {
                                    progressDialog = ProgressDialog.show(PersonCenterActivity.this, null, "更新中...");
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }
    }

    private void showSaveButton() {
        //弹出保存按钮
        User user = User.getInstance();
        if (fabSave.getVisibility() == View.GONE) {
            UserInfo.UserBean bean = user.getInfo().getUser();
            String personality = tvPersonality.getText().toString();
            if (!tvNickname.getText().toString().equals(bean.getName()) || !tvSex.getText().toString().equals(bean.getSex())
                    || (!personality.equals(bean.getPersonality()) && !personality.equals(this.getResources().getString(R.string.personality))) || changeAvatar) {
                fabSave.setVisibility(View.VISIBLE);
                ObjectAnimator.ofFloat(fabSave, "translationY", -150).start();
            } else {
                hideSaveButton();
            }
        }
    }

    private void hideSaveButton() {
        if (fabSave.getVisibility() == View.VISIBLE) {
            ObjectAnimator.ofFloat(fabSave, "translationY", 200).start();
            fabSave.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fabSave.setVisibility(View.GONE);
                }
            }, 1000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_PERSONALITY:
                String personality = data.getStringExtra("personality");
                tvPersonality.setText(personality);
                if (!personality.equals(oldPersonality)) {
                    tvPersonality.setTextColor(getResources().getColor(R.color.colorPrimary, null));
                } else {
                    tvPersonality.setTextColor(getResources().getColor(R.color.gray, null));
                }
                break;
            case REQUEST_PICTURE:
                Uri uri = data.getData();
                if (uri != null) {
                    Glide.with(this)
                            .load(uri)
                            .transform(new GlideGircleTransform(this))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(ivAvatar);
                    changeAvatar = true;
                    path = ImagePathUtil.getPathByUri4kitkat(this, uri);
                }
                break;
        }
        showSaveButton();
    }

    @Override
    public void onUpdateUser(UserInfo info) {
        //更新用户信息完成
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        User instance = User.getInstance();
        instance.saveUser(info);
        hideSaveButton();
        Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
        tvNickname.setTextColor(getResources().getColor(R.color.gray, null));
        tvSex.setTextColor(getResources().getColor(R.color.gray, null));
        tvPersonality.setTextColor(getResources().getColor(R.color.gray, null));
        changeAvatar = false;
    }

    @Override
    public void onCompress(File file) {
        ((PersonPresenterImp) presenter).updateUser(file, tvNickname.getText().toString(), tvSex.getText().toString(), tvPersonality.getText().toString());
    }

    @Override
    public void onFail(Throwable throwable) {
        Logger.d(throwable.getMessage());
    }
}
