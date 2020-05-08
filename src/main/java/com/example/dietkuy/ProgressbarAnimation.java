package com.example.dietkuy;

import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressbarAnimation extends Animation {

    private Context context;
    private ProgressBar progressbar;
    private TextView textView;
    private float from;
    private float to;

    public ProgressbarAnimation(Context context, ProgressBar progressbar, TextView textView, float from, float to) {
        this.context = context;
        this.progressbar = progressbar;
        this.textView = textView;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        float value = from + (to-from) * interpolatedTime;
        progressbar.setProgress((int) value);
        textView.setText((int) value + "%");

        if (value == to) {
            context.startActivity(new Intent(context, MainActivity.class));
        }
    }
}
