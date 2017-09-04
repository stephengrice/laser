package com.stephengrice.laser;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DashDisplayView extends LinearLayout {

    private Context mContext;
    private AttributeSet mAttrs;

    private TextView mTitle, mAmount;
    private Button mButton;

    private String mTitleText;
    private int mAmountValue;

    public DashDisplayView(Context context) {
        super(context);
        mContext = context;
        init();
    }
    public DashDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        init();
    }

    public DashDisplayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mAttrs = attrs;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_dash_display, this);

        mTitle = (TextView) getRootView().findViewById(R.id.view_title);
        mAmount = (TextView) getRootView().findViewById(R.id.view_amount);
        mButton = (Button) getRootView().findViewById(R.id.view_button);

        if (mAttrs != null) {
            TypedArray a = mContext.getTheme().obtainStyledAttributes(
                    mAttrs,
                    R.styleable.DashDisplayView,
                    0, 0);

            try {
                mTitleText = a.getString(R.styleable.DashDisplayView_title);
                mAmountValue = a.getInteger(R.styleable.DashDisplayView_amount, 0);

                mTitle.setText(mTitleText);
                mAmount.setText(Integer.toString(mAmountValue));
            } finally {
                a.recycle();
            }

        }


    }

    public Button getButton() {
        return mButton;
    }
    public TextView getAmountTextView() {
        return mAmount;
    }

    public void setTitle(String title) {
        mTitleText = title;
        mTitle.setText(mTitleText);
    }

    public void setAmount(int amount) {
        mAmountValue = amount;
        mAmount.setText(Integer.toString(mAmountValue));
    }

    public String getTitle() {
        return mTitleText;
    }

    public int getAmount() {
        return mAmountValue;
    }

}
