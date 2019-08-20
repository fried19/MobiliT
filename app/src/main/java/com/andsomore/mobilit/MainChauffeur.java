package com.andsomore.mobilit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.andsomore.mobilit.Singleton.ApplicationContext;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainChauffeur extends AppCompatActivity implements View.OnClickListener {
    private Button btVerifier, btReset;
    private Toolbar toolbar;
    String nom, prenom, numTel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference chauffeurRef = db.collection("CHAUFFEUR");
    private CollectionReference vehiculeRef = db.collection("VEHICULE");
    private SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.getAppContext());

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chauffeur);
        InitView();
        nom = preferences.getString("Nom", "");
        prenom = preferences.getString("Prenom", "");
        numTel = preferences.getString("Telephone", "");
        toolbar.setTitle(nom + " " + prenom);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        btVerifier.setOnClickListener(this);
        btReset.setOnClickListener(this);

    }


    private void InitView() {
        btReset = findViewById(R.id.btReset);
        btVerifier = findViewById(R.id.btVerifier);
        toolbar = findViewById(R.id.toolbarChauffeur);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == btVerifier) {
            startActivity(new Intent(this, verificationRecuReservation.class));
        }

        if (v == btReset) {
            SimpleDateFormat formatter = new java.text.SimpleDateFormat("EEEE", new Locale("fr", "FR"));
            String jour=null ;
            try {
                jour = formatter.format(new Date());

            } catch (Exception e) {
                e.printStackTrace();
            }
            String finalJour = jour;
            chauffeurRef
                    .whereEqualTo("numTelephone", numTel)
                    .get()
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String nomBus=document.getString("nomBus");

                                vehiculeRef
                                        .whereEqualTo("nomBapteme",nomBus)
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if(task1.isSuccessful()){

                                                for(QueryDocumentSnapshot documeent : task1.getResult()){
                                                    String idVehicule=documeent.getId();
                                                    vehiculeRef
                                                            .document(idVehicule)
                                                            .update("placeDisponible."+finalJour,50)
                                                            .addOnSuccessListener(aVoid -> Toast.makeText(MainChauffeur.this,"Félicitation!! Voyage terminé avec succès",Toast.LENGTH_SHORT).show())
                                                            .addOnFailureListener(e -> {
                                                                e.printStackTrace();
                                                                Toast.makeText(MainChauffeur.this,"Impossible de signaler.Veuillez patienter puis reprendre.",Toast.LENGTH_SHORT).show();

                                                            });
                                                }

                                            }else {
                                                Toast.makeText(MainChauffeur.this,"Opération interdite",Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        }else {
                            Toast.makeText(MainChauffeur.this,"Autorisation non accordée",Toast.LENGTH_SHORT).show();
                        }

                    });

        }
    }
}
