package com.andsomore.mobilit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.validator.routines.EmailValidator;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    private ImageView busImageView;
    private TextView busTextView;
    private ProgressBar loadingProgressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CountDownTimer(10000, 1000) {

            @Override
            public void onFinish() {
                initViews();
                busTextView.setVisibility(GONE);
                loadingProgressBar.setVisibility(GONE);
                busImageView.setVisibility(GONE);

                startActivity(new Intent(MainActivity.this, LoginActivity.class));

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
