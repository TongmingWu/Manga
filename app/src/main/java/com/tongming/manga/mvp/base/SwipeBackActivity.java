package com.tongming.manga.mvp.base;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.tongming.manga.util.CommonUtil;

/**
 * Author: Tongming
 * Date: 2016/10/28
 */

public abstract class SwipeBackActivity extends BaseActivity {

    public static final int ANIMATION_DURATION = 300;//默认动画时间
    public static final int DEFAULT_TOUCH_THRESHOLD = 60;//默认开始滑动的位置距离左边缘的距离

    private int screenWidth;
    private int touchSlop;
    private ArgbEvaluator evaluator;
    private ViewGroup decorView;
    private ViewGroup contentView;
    private ViewGroup userView;
    private ValueAnimator animator;
    private float rawY;
    private float rawX;
    private boolean isMoving;
    private VelocityTracker mVelTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenWidth = CommonUtil.getScreenWidth(this);
        touchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        evaluator = new ArgbEvaluator();
        decorView = (ViewGroup) getWindow().getDecorView();
        contentView = (ViewGroup) findViewById(android.R.id.content);
        userView = (ViewGroup) contentView.getChildAt(0);

        animator = new ValueAnimator();
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                decorView.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00ffffff")));
                int x = (int) animation.getAnimatedValue();
                if (x >= screenWidth) {
                    //超过屏幕范围销毁activity
                    finish();
                }

                handleView(x);
                handleBackgroundColor(x);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getVelocityTracker(event);
        if (animator.isRunning()) {
            return true;
        }
        int pointId = -1;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                rawX = event.getRawX();
                rawY = event.getRawY();
                pointId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isMoving) {
                    int dx = (int) Math.abs(event.getRawX() - rawX);
                    int dy = (int) Math.abs(event.getRawY() - rawY);
                    if (dx > touchSlop && dx > dy && rawX < DEFAULT_TOUCH_THRESHOLD) {
                        isMoving = true;
                    }
                } else {
                    handleView((int) event.getRawX());
//                    handleBackgroundColor(event.getRawX());
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                int distance = (int) (event.getRawX() - rawX);
                //计算速度
                mVelTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelTracker.getXVelocity(pointId);
                if (isMoving && Math.abs(userView.getTranslationX()) >= 0) {
                    if (xVelocity > 1000f || distance >= screenWidth / 4) {
                        //退出
                        animator.setIntValues((int) event.getRawX(), screenWidth);
                    } else {
                        animator.setIntValues((int) event.getRawX(), 0);
                    }
                    animator.start();
                    isMoving = false;
                }

                rawX = 0;
                rawY = 0;
                recycleVelocityTracker();
                break;
        }
        if (isMoving) {
            return true;
        } else {
            return super.dispatchTouchEvent(event);
        }
    }

    private void handleView(int x) {
        userView.setTranslationX(x);
    }

    /**
     * 控制背景颜色与透明度
     */
    private void handleBackgroundColor(float x) {
        int colorValue = (int) evaluator.evaluate(x / screenWidth,
                Color.parseColor("#dd000000"),
                Color.parseColor("#00000000"));
        contentView.setBackgroundColor(colorValue);
    }

    /**
     * 获取速度追踪器
     *
     * @return
     */
    private VelocityTracker getVelocityTracker(MotionEvent event) {
        if (mVelTracker == null) {
            mVelTracker = VelocityTracker.obtain();
        }
        mVelTracker.addMovement(event);
        return mVelTracker;
    }

    /**
     * 回收速度追踪器
     */
    private void recycleVelocityTracker() {
        if (mVelTracker != null) {
            mVelTracker.clear();
            mVelTracker.recycle();
            mVelTracker = null;
        }
    }
}
