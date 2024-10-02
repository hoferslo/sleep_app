package com.example.sleep_app.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.example.sleep_app.R;

public class ToggleableLinearLayout extends androidx.appcompat.widget.AppCompatImageView implements View.OnClickListener {

    private boolean selected;
    private int colorSelected;
    private int colorUnselected;

    public ToggleableLinearLayout(Context context) {
        super(context);
        init();
    }

    public ToggleableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SelectableLinearLayout);
        selected = a.getBoolean(R.styleable.ToggleableLinearLayout_toggleableSelected, false);
        colorSelected = a.getColor(R.styleable.SelectableLinearLayout_colorSelected, getResources().getColor(R.color.white));
        colorUnselected = a.getColor(R.styleable.SelectableLinearLayout_colorUnselected, getResources().getColor(R.color.sleepyBlue));
        a.recycle();
        init();
    }

    public ToggleableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void handleSelection() {
        if (selected) {
            setColor(colorSelected);
        } else {
            setColor(colorUnselected);
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
    public void setSelected(boolean b) {
        this.selected = b;
        handleSelection();
    }

    public boolean isSelected() {
        return this.selected;
    }

    // Handle clicks on the ImageView
    @Override
    public void onClick(View v) {
        // Show a Toast with the popup text
        setSelected(!selected);
    }
}
