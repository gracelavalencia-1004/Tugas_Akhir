package com.example.dietkuy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    ProgressBar progressbar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String file = "file:android_asset/android.gif";
        webView.loadUrl(file);

        progressbar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.textView6);

        progressbar.setMax(100);
        progressbar.setScaleY(3f);
        progressAnimation();
    }

    public void progressAnimation() {
        ProgressbarAnimation animation = new ProgressbarAnimation(this, progressbar, textView, 0f, 100f);
        animation.setDuration(7000);
        progressbar.setAnimation(animation);
    }
}
