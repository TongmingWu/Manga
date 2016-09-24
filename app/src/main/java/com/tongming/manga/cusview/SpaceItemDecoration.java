package com.tongming.manga.cusview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Tongming on 2016/8/11.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private boolean isGrid;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    public SpaceItemDecoration(int space, boolean isGrid) {
        this.isGrid = isGrid;
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        int count = parent.getAdapter().getItemCount();
        if (isGrid) {
            count += count % 3 == 1 ? 2 : (count % 3 == 0 ? 0 : 1);
            int row = count / 3;
            if ((position + 1) % 3 == 1) {
                //每一行的第一个
                outRect.left = space;
                outRect.right = space / 2;
            } else if ((position + 1) % 3 == 0) {
                //第三个
                outRect.left = space / 2;
                outRect.right = space;
            } else {
                outRect.left = space / 2;
                outRect.right = space / 2;
            }
            if (position / 3 == 0) {
                //第一行
                outRect.top = space;
                outRect.bottom = space / 2;
            } else if (position / 3 == row - 1) {
                //最后一行
                outRect.top = space / 2;
                outRect.bottom = space;
            } else {
                outRect.bottom = space / 2;
                outRect.top = space / 2;
            }
        } else {
            if (position != 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
    }
}
