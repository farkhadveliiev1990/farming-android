package com.dmy.farming.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

/**
 * Created by 陈晖 on 2018/4/2.
 */

public class MyObserScrollview extends ObservableScrollView {

    private boolean isbottom = false;
    private int scrollY;

    public MyObserScrollview(Context context) {
        super(context);
    }

    public MyObserScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.scrollY = t;

        View view = getChildAt(getChildCount() - 1);
        int d = view.getBottom();
        d -= (getHeight() + getScrollY());
        if (d == 0) {
            //you are at the end of the list in scrollview
            //do what you wanna do here
            isbottom = true;
        } else{
            isbottom = false;
            super.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public boolean isbottom() {
        return isbottom;
    }

    public int getScrollerY(){
        return scrollY;
    }
}
