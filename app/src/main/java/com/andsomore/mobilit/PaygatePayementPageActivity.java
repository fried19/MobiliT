package com.andsomore.mobilit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.andsomore.mobilit.Singleton.ApplicationContext;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import dmax.dialog.SpotsDialog;


public class PaygatePayementPageActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView webView;
    private String PageURL, PageTitle ;
    private Button btTerminer,btQuitter;
    private ActionBar actionBar;
    private AlertDialog alertDialog;
    private FirebaseFirestore db;
    private String identifiant;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paygate_payement_page);
        InitViews();
        actionBar=getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFF0000")));
        //Recupération du montant
        Intent intent=getIntent();
        int amount = intent.getIntExtra("amount",0);
        //Obtention d'un identifiant pour la transaction
        DocumentReference newReservationRef = db.collection("RESERVATION").document();
        identifiant=newReservationRef.getId();
        String apiKey= ApplicationContext.Token;
        String descprition="Achat de billet de transport";
        String requestUrl="https://paygateglobal.com/v1/page?token="+apiKey+"&amount="+1+"&description="+descprition+"&identifier="+identifiant;
        //https://paygateglobal.com/v1/page?token=3b540bf9-09a1-41cd-a854-bc73efbbd305&amount=150&description=Achat de billet de transport&identifier=3b540bf;
        showWebPage(requestUrl);

    }


    private void InitViews(){
        webView = findViewById(R.id.webView1);
        actionBar=getSupportActionBar();
        alertDialog=new SpotsDialog(this);
        btTerminer=findViewById(R.id.btTerminer);
        btQuitter=findViewById(R.id.btQuitter);
        db= FirebaseFirestore.getInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void showWebPage(String url) {

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                // TODO Auto-generated method stub
                PageURL = view.getUrl();
                actionBar.setSubtitle(PageURL);
                alertDialog.show();
                alertDialog.setMessage("Chargement...");


            }

            @Override
            public void onPageFinished(WebView view, String url) {

                // TODO Auto-generated method stub

                PageURL = view.getUrl();
                PageTitle = view.getTitle();
                actionBar.setTitle(PageURL);
                actionBar.setSubtitle(PageTitle);
                alertDialog.dismiss();
                if(url.contains("confirmation")||url.contains("accepted")){
                     btTerminer.setVisibility(View.VISIBLE);
                     btTerminer.setOnClickListener(PaygatePayementPageActivity.this);

                }else if(url.contains("declined")||url.contains("cancelled")){

                     btQuitter.setVisibility(View.VISIBLE);
                     btQuitter.setOnClickListener(PaygatePayementPageActivity.this);
                }


            }


        });
     webView.getSettings().setJavaScriptEnabled(true);
     webView.loadUrl(url);


    }


    @Override
    public void onClick(View view) {
        if(view==btTerminer){
            Intent intent=new Intent();
            intent.putExtra("idClient",identifiant);
            setResult(RESULT_OK,intent);
            finish();

        }

        if(view==btQuitter){
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
