package com.andsomore.mobilit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.andsomore.mobilit.dao.TraitementUtilisateur;
import com.andsomore.mobilit.entite.Utilisateur;
import com.andsomore.mobilit.idao.IConnected;


import org.apache.commons.validator.routines.EmailValidator;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private ImageButton btRegister;
    private TextView tvLogin, tvRegister;
    private Button btConnexion,btShowPassword;
    private EditText etEmail,etPassword;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitViews();
        btRegister.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        btConnexion.setOnClickListener(this);
        btShowPassword.setOnLongClickListener(this);

    }


    public boolean isEmpty(){

        if((TextUtils.isEmpty(etEmail.getText().toString()))
                ||(TextUtils.isEmpty(etPassword.getText().toString())))
        {
            return true;
        }else
            return false;

    }

    //Initialisation des composants
    private void InitViews() {
        btRegister = findViewById(R.id.btRegister);
        tvRegister = findViewById(R.id.tvRegister);
        tvLogin = findViewById(R.id.tvLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btShowPassword = findViewById(R.id.showPassword);
        btConnexion = findViewById(R.id.btConnexion);
        alertDialog =new SpotsDialog(this);
    }

    //Animation au chargement de l'activite
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        InitViews();
        if ((v == btRegister)||(v == tvRegister) ) {
            Intent intent = new Intent(LoginActivity.this, EnregistrementUtilisateurActivity.class);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(tvLogin, "login");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
            startActivity(intent, activityOptions.toBundle());

        }

        //Methode qui se declanche dès un click sur le boutton Connexion
        if(v==btConnexion){

            String Email=etEmail.getText().toString();
            String Password=etPassword.getText().toString();
            if(isEmpty()){
                afficherMessageErreur();
            }else{
                if(!isValidEmail(Email)){
                    etEmail.requestFocus();
                    etEmail.setError("Veuillez saisir un mail sous la forme xyz@gmail.com");
                }else {
                    Utilisateur utilisateur=new Utilisateur(Email,Password);
                    TraitementUtilisateur traitementUtilisateur=new TraitementUtilisateur();

                    alertDialog.show();
                    alertDialog.setMessage("Connexion en cour...");
                    traitementUtilisateur.seConnecter(utilisateur, ok -> {

                        if (ok) {
                            LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            alertDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "L'email et/ou le mot de passes " +
                                    "ne figurent pas dans la base de données", Toast.LENGTH_SHORT).show();
                        }

                    });

                }
            }
        }
    }

    public void afficherMessageErreur(){
        if((TextUtils.isEmpty(etEmail.getText().toString()))
                &&(TextUtils.isEmpty(etPassword.getText().toString()))){
            etEmail.requestFocus();
            etEmail.setError("Veuiller saisir le mail");
            etPassword.setError("Veuillez saisir le mot de passe");

        }else if(TextUtils.isEmpty(etEmail.getText().toString())){
            etEmail.requestFocus();
            etEmail.setError("Veuiller saisir le mail");


        }else if(TextUtils.isEmpty(etPassword.getText().toString())){
            etPassword.requestFocus();
            etPassword.setError("Veuillez saisir le mot de passe");

        }
    }
    public boolean isValidEmail(String email){
        boolean valid = EmailValidator.getInstance(false).isValid(email);
        if(valid==true){
            return true;
        }else
            return false;

    }

    //Thread pour afficher le mdp sur l'action onLongClick du boutton
    @Override
    public boolean onLongClick(View view) {
        if (view == btShowPassword) {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    while ((btShowPassword.isPressed())) {

                        etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    if (!btShowPassword.isPressed()) {
                        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                }
            };
            new Thread(runnable).start();

        }
        return true;
    }

}




