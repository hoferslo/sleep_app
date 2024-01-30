package com.example.sleep_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LiquidView extends View {

    private Paint liquidPaint;
    private int viewHeight;
    private float waveAmplitude;
    private float waveFrequency;
    private float phaseShift;

    public LiquidView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        liquidPaint = new Paint();
        liquidPaint.setColor(Color.BLUE);
        waveAmplitude = 30; // Amplitude of the wave
        waveFrequency = 0.02f; // Frequency of the wave
        phaseShift = 0; // Phase shift to control wave movement
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the liquid based on the current liquid level and wave animation
        int numPoints = getWidth();
        float[] wavePoints = new float[numPoints * 2];

        for (int i = 0; i < numPoints; i++) {
            float x = i;
            float y = viewHeight + waveAmplitude * (float) Math.sin(waveFrequency * x + phaseShift);
            wavePoints[i * 2] = x;
            wavePoints[i * 2 + 1] = y;
        }

        canvas.drawLines(wavePoints, liquidPaint);

        // Update the phase shift to create a moving wave
        phaseShift += 0.1;

        // Redraw the view
        invalidate();
    }

    public void setLiquidLevel(int level) {
        viewHeight = getHeight() - level;
        invalidate();
    }
}
