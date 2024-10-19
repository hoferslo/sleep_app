package com.example.sleep_app.tools;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.example.sleep_app.R;

public class SelectableLinearLayout extends androidx.appcompat.widget.AppCompatImageView implements View.OnClickListener {

    private int selected;
    private int colorSelected;
    private int colorUnselected;
    private int colorDisabled;

    public SelectableLinearLayout(Context context) {
        super(context);
        init();
    }

    public SelectableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SelectableLinearLayout);
        selected = a.getInt(R.styleable.SelectableLinearLayout_selectableSelected, 2);
        colorSelected = a.getColor(R.styleable.SelectableLinearLayout_colorSelected, getResources().getColor(R.color.white));
        colorUnselected = a.getColor(R.styleable.SelectableLinearLayout_colorUnselected, getResources().getColor(R.color.gray_600));
        colorDisabled = a.getColor(R.styleable.SelectableLinearLayout_colorUnselected, getResources().getColor(R.color.sleepyBlue));
        a.recycle();
        init();
    }

    public SelectableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void handleSelection() {
        if (selected == 1) {
            setColor(colorSelected);
            setImageDrawable(null);
            clearColorFilter();
        } else if (selected == 0) {
            setColor(colorSelected);
            setImageResource(R.drawable.block_24px);
            setColorFilter(getResources().getColor(R.color.light_blue_900));
        } else {
            setColor(colorDisabled);
            setImageDrawable(null);
            clearColorFilter();
        }
    }

    public void setColor(int color) {
        this.getBackground().setTint((color));
    }

    private void init() {
        setOnClickListener(this);
        handleSelection();
    }

    // Method to set the text that will be shown in the popup
    public void setSelected(int i) {
        this.selected = i;
        handleSelection();
    }

    public int getSelected() {
        return this.selected;
    }


    // Handle clicks on the ImageView
    @Override
    public void onClick(View v) {
        // Show a Toast with the popup text
        selected += 1;
        if (selected > 2) {
            selected = 0;
        }
        setSelected(selected);
    }
}
