package com.tongming.manga.mvp.view.fragment;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.util.CommonUtil;

/**
 * Created by Tongming on 2016/9/3.
 */
public class EditDialogFragment extends DialogFragment {

    private long availableSize;

    public interface EditDialogListener {
        void onFinishDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_cache, null);
        availableSize = CommonUtil.getAvailableSize() / 1024 / 1024;
        final EditText etCache = (EditText) view.findViewById(R.id.et_cache);
        final TextView tvTip = (TextView) view.findViewById(R.id.tv_cache_tip);
        etCache.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int size = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    size = Integer.parseInt(s.toString());
                }
                Logger.d("剩余存储=" + availableSize + "MB");
                if (size < 100) {
                    tvTip.setText("缓存大小不能小于100M");
                    tvTip.setTextColor(Color.RED);
                } else if (size > availableSize) {
                    tvTip.setText("缓存大小不能超过剩余存储");
                    tvTip.setTextColor(Color.RED);
                } else {
                    tvTip.setText("当前缓存上限为200M");
                    tvTip.setTextColor(getResources().getColor(R.color.gray, null));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etCache.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }
}
