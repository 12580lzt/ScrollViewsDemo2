package com.scrollviewdemolzt.scrollviewsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyScrollView mScrollView = (MyScrollView) findViewById(R.id.scrollView);
        ScrollView mOutScrollView = (ScrollView) findViewById(R.id.out_scrollView);

        mOutScrollView.scrollTo(0,180);

        //ScrollView默认显示第-个view
        View childView = mScrollView.getChildAt(0);
        if (childView!=null){
            ViewGroup.LayoutParams layoutParams = childView.getLayoutParams();
            int measuredHeight =layoutParams.height;
            mScrollView.scrollTo(0,measuredHeight);
        }
    }
}
