package com.andsomore.mobilit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.commons.validator.routines.EmailValidator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private ImageButton btRegister;
    private TextView tvLogin, tvRegister;
    private Button btConnexion,btShowPassword;
    private EditText Email,Password,RePassword,Nom,Prenom,Telephone;

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

        if((Email.length()==0) ||(Password.length()==0))
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
        Email = findViewById(R.id.etEmail);
        Password = findViewById(R.id.etPassword);
        RePassword = findViewById(R.id.etRePassword);
        btShowPassword = findViewById(R.id.showPassword);
        btConnexion = findViewById(R.id.btConnexion);
    }

    //Animation au chargement de l'activite
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onClick(View v) {
        if ((v == btRegister)||(v == tvRegister) ) {
            Intent intent = new Intent(LoginActivity.this, EnregistrementUtilisateurActivity.class);
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String>(tvLogin, "login");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
            startActivity(intent, activityOptions.toBundle());
        }

        //Methode qui se declanche dès un click sur le boutton Connexion
        if(v==btConnexion){
            if(isEmpty()==true){
                affichageErreur();
            }else{
                if(isValidEmail(Email.getText().toString())==false){
                    Email.setError("Veuillez saisir un mail sous la forme xyz@gmail.com");
                }
            }
        }
    }

    public void affichageErreur(){
        if((Email.length()==0)&&(Password.length()==0)){
            Email.setError("Veuiller saisir le mail");
            Password.setError("Veuillez saisir le mot de passe");

        }else if(Email.length()==0){
            Email.setError("Veuiller saisir le mail");

        }else if(Password.length()==0){
            Password.setError("Veuillez saisir le mot de passe");

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

                        Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    if (!btShowPassword.isPressed()) {
                        Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                }
            };
            new Thread(runnable).start();

        }
        return true;
    }

}




