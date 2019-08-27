package com.andsomore.mobilit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import static android.view.View.GONE;

public class WelcomeActivity extends AppCompatActivity {
    private ImageView busImageView;
    private TextView busTextView;
    private ProgressBar loadingProgressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new CountDownTimer(1000, 1000) {

            @Override
            public void onFinish() {
                initViews();
                busTextView.setVisibility(GONE);
                loadingProgressBar.setVisibility(GONE);
                busImageView.setVisibility(GONE);

                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();

            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();
    }

    private void initViews() {
        busImageView = findViewById(R.id.busImageView);
        busTextView = findViewById(R.id.busTextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);


    }
}
