package com.andsomore.mobilit;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.andsomore.mobilit.dao.TraitementUtilisateur;
import com.andsomore.mobilit.entite.Utilisateur;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.validator.routines.EmailValidator;

import dmax.dialog.SpotsDialog;

public class EnregistrementUtilisateurActivity extends AppCompatActivity implements View.OnClickListener {
    //Declaration des composants
    private RelativeLayout rlayout;
    private Animation animation;
    private EditText etNom,etPrenom,etTelephone,etEmail,etPassword,etRepassword;
    private Spinner spTypeUtilisateur;
    private Button btInscription;
    private AlertDialog alertDialog;



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
        btInscription.setOnClickListener(this);

    }

    //Initialisation des composants
    private void InitViews() {
        etNom=findViewById(R.id.etNom);
        etPrenom=findViewById(R.id.etPrenom);
        etTelephone=findViewById(R.id.etTelephone);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etRepassword=findViewById(R.id.etRePassword);
        spTypeUtilisateur=findViewById(R.id.spTypeUtilisateur);
        btInscription=findViewById(R.id.btInscription);
        rlayout     = findViewById(R.id.rlayout);
        animation   = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        alertDialog = new SpotsDialog(this);

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

    @Override
    public void onClick(View view) {
        InitViews();
        String Nom=etNom.getText().toString();
        String Prenom=etPrenom.getText().toString();
        String Telephone=(etTelephone.getText().toString());
        String Email=etEmail.getText().toString();
        String Pswd=etPassword.getText().toString();
        String TypeCompte=spTypeUtilisateur.getSelectedItem().toString();
        String Repswd=etRepassword.getText().toString();
        if(view==btInscription){
            if(isEmpty()){
                afficherMessageErreur();
            }else if(!isValidEmail(Email)){
                etEmail.requestFocus();
                etEmail.setError("Veuillez saisir un mail sous la forme xyz@gmail.com");
            }else if(!compareBothPassword(Pswd,Repswd)){
                etRepassword.requestFocus();
                etRepassword.setError("Le mot de passe ne correspond pas");

            }else {
                Utilisateur utilisateur=new Utilisateur(Nom,Prenom,Telephone,Email,Pswd,TypeCompte);
                TraitementUtilisateur traitementUtilisateur=new TraitementUtilisateur();
                alertDialog.show();
                alertDialog.setMessage("Enrégistrement en cours...");

                traitementUtilisateur.creerCompte(utilisateur, ok -> {
                    if(ok){
                        startActivity( new Intent(this, MainActivity.class));
                        View main=findViewById(R.id.main);
                        Snackbar.make(main,"Vous êtes maintenant inscrit sur notre plateforme!! ",Snackbar.LENGTH_SHORT).show();
                    }else {
                        alertDialog.dismiss();
                        Toast.makeText(this,"Erreur!!Veuillez revérifier les données saisies",Toast.LENGTH_LONG).show();
                        etNom.requestFocus();

                    }

                });


                }
            }

        }

    public boolean isEmpty(){

        if((TextUtils.isEmpty(etEmail.getText().toString()))
                ||(TextUtils.isEmpty(etPassword.getText().toString()))
                ||(TextUtils.isEmpty(etRepassword.getText().toString()))
                ||(TextUtils.isEmpty(etNom.getText().toString()))
                ||(TextUtils.isEmpty(etPrenom.getText().toString()))
                ||(TextUtils.isEmpty(etTelephone.getText().toString()))
        )
        {
            return true;
        }else
            return false;

    }

    public boolean isValidEmail(String email){
        boolean valid = EmailValidator.getInstance(false).isValid(email);
        if(valid==true){
            return true;
        }else
            return false;

    }

    public boolean compareBothPassword(String pswd,String Repswd){
        if(pswd.equals(Repswd)){
            return true;
        }else return false;
    }

    public void afficherMessageErreur(){
        if((TextUtils.isEmpty(etEmail.getText().toString()))
                &&(TextUtils.isEmpty(etPassword.getText().toString()))
                &&(TextUtils.isEmpty(etRepassword.getText().toString()))
                &&(TextUtils.isEmpty(etNom.getText().toString()))
                &&(TextUtils.isEmpty(etPrenom.getText().toString()))
                &&(TextUtils.isEmpty(etTelephone.getText().toString()))
        ){
            etNom.requestFocus();
            etNom.setError("Veuillez saisir le nom");
            etPrenom.setError("Veuillez saisir le prenom");
            etTelephone.setError("Veuillez siaisr le numero téléphonique");
            etEmail.setError("Veuiller saisir le mail");
            etPassword.setError("Veuillez saisir le mot de passe");
            etRepassword.setError("Veuillez confirmer le mot de passe");

        }else if((TextUtils.isEmpty(etNom.getText().toString()))){
            etNom.requestFocus();
            etNom.setError("Veuillez saisir le nom");
        }else if((TextUtils.isEmpty(etPrenom.getText().toString()))){
            etPrenom.requestFocus();
            etPrenom.setError("Veuillez saisir le prenom");
        }else if(TextUtils.isEmpty(etTelephone.getText().toString())){
            etTelephone.requestFocus();
            etTelephone.setError("Veuiller saisir le numero téléphonique ");
        }
        else if(TextUtils.isEmpty(etEmail.getText().toString())){
            etEmail.requestFocus();
            etEmail.setError("Veuiller saisir le mail");

        }else if(TextUtils.isEmpty(etPassword.getText().toString())){
            etPassword.requestFocus();
            etPassword.setError("Veuillez saisir le mot de passe");

        }else if(TextUtils.isEmpty(etRepassword.getText().toString())){
            etRepassword.requestFocus();
            etRepassword.setError("Veuillez confirmer le mot de passe");

        }
    }

}
