package com.example.sleep_app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

public class CustomImageView extends androidx.appcompat.widget.AppCompatImageView implements View.OnClickListener {

    private String popupText;

    public CustomImageView(Context context) {
        super(context);
        init();
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        popupText = a.getString(R.styleable.CustomImageView_popupText);
        a.recycle();
        init();
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(this);
    }

    // Method to set the text that will be shown in the popup
    public void setPopupText(String text) {
        this.popupText = text;
    }

    // Handle clicks on the ImageView
    @Override
    public void onClick(View v) {
        // Show a Toast with the popup text
        if (popupText != null) {
            Toast.makeText(getContext(), popupText, Toast.LENGTH_SHORT).show();
        }
    }
}
