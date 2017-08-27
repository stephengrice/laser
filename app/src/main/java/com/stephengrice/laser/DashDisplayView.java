package com.stephengrice.laser;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class DashDisplayView extends LinearLayout {
    public DashDisplayView(Context context) {
        super(context);
        init();
    }
    public DashDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DashDisplayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_dash_display, this);
    }
}
