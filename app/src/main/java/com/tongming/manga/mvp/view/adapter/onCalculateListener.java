package com.tongming.manga.mvp.view.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Tongming on 2016/9/6.
 */

public interface onCalculateListener {
    void onCalculateComplete(float scale, int position, RecyclerView.ViewHolder holder);
}
