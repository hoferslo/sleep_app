package com.example.sleep_app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

public class PopupTextImageView extends androidx.appcompat.widget.AppCompatImageView implements View.OnClickListener {

    private String popupText;

    public PopupTextImageView(Context context) {
        super(context);
        init();
    }

    public PopupTextImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PopupTextImageView);
        popupText = a.getString(R.styleable.PopupTextImageView_popupText);
        a.recycle();
        init();
    }

    public PopupTextImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public String getPopupText() {
        return this.popupText;
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
