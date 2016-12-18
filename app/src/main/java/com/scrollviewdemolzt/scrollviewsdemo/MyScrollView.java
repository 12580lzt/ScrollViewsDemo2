package com.scrollviewdemolzt.scrollviewsdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by lzt on 2016/12/17.
 */

public class MyScrollView extends FrameLayout {
    private View mItemView;
    private int mItemHeight;
    private float mdownY;

    private static int STATE_NON = 0;//正常状态
    private static int STATE_TO_TOP = 1;//下拉到顶部
    private static int STATE_TO_BOTTOM = 2;//上拉到底部
    private int mCurrentState;//当前状态

    public MyScrollView(Context context) {
        super(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        mItemView = getChildAt(0);
        LayoutParams layoutParams = (LayoutParams) mItemView.getLayoutParams();
        mItemHeight = layoutParams.height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() > 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int childWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            int childHeight = MeasureSpec.makeMeasureSpec(mItemHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidth, childHeight);
            //5倍childView 的高度
            setMeasuredDimension(width, childHeight * 5);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() > 0) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                int l = 0;
                int t = i * childView.getMeasuredHeight();
                int r = childView.getMeasuredWidth();
                int b = i * childView.getMeasuredHeight() + childView.getMeasuredHeight();
                childView.layout(l, t, r, b);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mdownY = ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = ev.getY();
                float dy = moveY - mdownY;
                if (dy > 0) {
                    //下拉
                    if (mCurrentState==STATE_TO_TOP){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } else {
                    //上拉
                    if (mCurrentState==STATE_TO_BOTTOM){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mdownY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY();
                int dy = (int) ((moveY - mdownY) + 0.5f);

                int scrollY = getScrollY();

                if (dy > 0) {
                    //下拉
                    if (scrollY - dy < 0) {
                        //下拉到顶
                        scrollTo(0, 0);
                        mCurrentState = STATE_TO_TOP;

                    } else {
                        scrollBy(0, -dy);
                        mCurrentState = STATE_NON;
                    }
                } else {
                    //上拉
                    if (scrollY + getMeasuredHeight() - dy > getChildCount() * mItemHeight) {
                        //上拉到底
                        scrollTo(0, getChildCount() * mItemHeight - getMeasuredHeight());
                        mCurrentState = STATE_TO_BOTTOM;
                    } else {
                        scrollBy(0, -dy);
                        mCurrentState = STATE_NON;
                    }
                }
                mdownY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                break;

        }

        return true;
    }

}
