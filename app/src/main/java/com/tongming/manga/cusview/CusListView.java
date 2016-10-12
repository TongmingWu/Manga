package com.tongming.manga.cusview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Author: Tongming
 * Date: 2016/10/10
 */

public class CusListView extends ListView {
    public CusListView(Context context) {
        super(context);
    }

    public CusListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CusListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CusListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE / 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
