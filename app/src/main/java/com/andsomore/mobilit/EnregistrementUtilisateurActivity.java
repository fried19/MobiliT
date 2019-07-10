package com.andsomore.mobilit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class EnregistrementUtilisateurActivity extends AppCompatActivity {
    //Declaration des composants
    private RelativeLayout rlayout;
    private Animation animation;
    private EditText Nom,Prenom,Telephone,Email,Password,Repassword;
    private Spinner TypeUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enregistrement_utilisateur);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        InitViews();

        rlayout.setAnimation(animation);
    }

    //Initialisation des composants
    private void InitViews() {
        Nom=findViewById(R.id.etNom);
        Prenom=findViewById(R.id.etPrenom);
        Telephone=findViewById(R.id.etTelephone);
        Email=findViewById(R.id.etEmail);
        Password=findViewById(R.id.etPassword);
        Repassword=findViewById(R.id.etRePassword);
        TypeUtilisateur=findViewById(R.id.spTypeUtilisateur);

        rlayout     = findViewById(R.id.rlayout);
        animation   = AnimationUtils.loadAnimation(this,R.anim.uptodown);

    }

    //Methode de retour à l'activite precedante
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
