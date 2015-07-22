package com.chaemil.hgms.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by chaemil on 22.7.15.
 */
public class ThumbImageView extends ImageView {

    public ThumbImageView(Context context) {
        super(context);
    }

    public ThumbImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ThumbImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = (int) (width / 1.777);
        setMeasuredDimension(width, height);
    }
}
