package com.chaemil.hgms.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.chaemil.hgms.R;

/**
 * Created by chaemil on 27.10.14.
 */
public class FixedAspectLayout extends FrameLayout {

    private float aspect = 1.0f;

    // .. alternative constructors omitted

    public FixedAspectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.FixedAspectLayout);
        aspect = a.getFloat(R.styleable.FixedAspectLayout_aspectRatio, 1.0f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        if (w == 0) {
            h = 0;
        } else if (h / w < aspect) {
            w = (int)(h / aspect);
        } else {
            h = (int)(w * aspect);
        }

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(w,
                        MeasureSpec.getMode(widthMeasureSpec)),
                MeasureSpec.makeMeasureSpec(h,
                        MeasureSpec.getMode(heightMeasureSpec)));
    }
}