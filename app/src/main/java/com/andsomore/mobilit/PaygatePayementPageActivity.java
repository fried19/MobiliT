package com.andsomore.mobilit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;




public class PaygatePayementPageActivity extends AppCompatActivity {
   private WebView webView;
    private ProgressBar progressBar;
    private String PageURL, PageTitle ;
    private  ActionBar actionBar;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paygate_payement_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        InitViews();

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF0000")));
        showWebPage("https://www.google.com");


    }
    private void InitViews(){
        webView = findViewById(R.id.webView1);
        actionBar=getSupportActionBar();
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void showWebPage(String url) {

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                // TODO Auto-generated method stub
                PageURL = view.getUrl();
                actionBar.setSubtitle(PageURL);
                Toast.makeText(getApplicationContext(),"Chargement...",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPageFinished(WebView view, String url) {

                // TODO Auto-generated method stub

                PageURL = view.getUrl();
                PageTitle = view.getTitle();
                actionBar.setTitle(PageURL);
                actionBar.setSubtitle(PageTitle);
                Toast.makeText(getApplicationContext(),"Chargement terminé",Toast.LENGTH_SHORT).show();

            }
        });
     webView.getSettings().setJavaScriptEnabled(true);
     webView.loadUrl(url);

    }



}
