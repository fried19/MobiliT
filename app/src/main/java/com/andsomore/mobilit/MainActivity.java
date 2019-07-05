package com.andsomore.mobilit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    private ImageView busImageView;
    private ImageView busIconImageView;
    private TextView busTextView;
    private EditText Email,Password;
    private Button btShowPassword;
    private ProgressBar loadingProgressBar;
    private RelativeLayout rootView, afterAnimationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        initViews();
        new CountDownTimer(1000, 1000) {

            @Override
            public void onFinish() {
                busTextView.setVisibility(GONE);
                loadingProgressBar.setVisibility(GONE);
                busImageView.setVisibility(GONE);
                rootView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorSplashText));
                busIconImageView.setVisibility(VISIBLE);
                startAnimation();
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();

        //Thread pour afficher le mdp sur l'action onLongClick du boutton
        btShowPassword.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Thread thread = new Thread(new Runnable(){

                    @Override
                    public void run() {
                            while((btShowPassword.isPressed())){

                                Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            }
                        if (!btShowPassword.isPressed()){
                            Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                    }
                });
                thread.start();
                return true;
            }
        });

    }

    private void initViews() {
        busImageView = findViewById(R.id.busImageView);
        busTextView = findViewById(R.id.busTextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        busIconImageView=findViewById(R.id.busIconImageView);
        rootView = findViewById(R.id.rootView);
        Password=findViewById(R.id.passwordEditText);
        btShowPassword=findViewById(R.id.showPassword);
        afterAnimationView = findViewById(R.id.afterAnimationView);
    }
    private void startAnimation() {
        ViewPropertyAnimator viewPropertyAnimator = busIconImageView.animate();
        viewPropertyAnimator.x(35f);
        viewPropertyAnimator.y(75f);
        viewPropertyAnimator.setDuration(1000);
        viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                afterAnimationView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

}
