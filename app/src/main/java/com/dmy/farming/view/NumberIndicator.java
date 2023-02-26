package com.dmy.farming.view;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.dmy.farming.R;

public class NumberIndicator extends TextView {

    public NumberIndicator(Context context) {
        super(context);
        setTextColor(Color.WHITE);
        setTextSize(14);
        setBackgroundResource(R.drawable.shape_bg);
        int padding = DensityUtils.dp2px(context, 5);
        setPadding(padding,padding,padding,padding);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //保证TextIndicator的宽高一致(正方形)
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
