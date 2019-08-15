package com.andsomore.mobilit.dao;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.andsomore.mobilit.Singleton.ApplicationContext;
import com.andsomore.mobilit.entite.Utilisateur;
import com.andsomore.mobilit.idao.IConnected;
import com.andsomore.mobilit.idao.IUtilisateur;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;


public class TraitementUtilisateur implements IUtilisateur<Utilisateur> {


    FirebaseFirestore db= FirebaseFirestore.getInstance();
    DocumentReference UserRef =db.collection("UTILISATEUR").document();
    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(ApplicationContext.getAppContext());
    SharedPreferences.Editor editor=preferences.edit();

    @Override
    public void seConnecter(Utilisateur utilisateur, IConnected connected) {
        db.collection("UTILISATEUR")
                .whereEqualTo("email", utilisateur.getEmail())
                .whereEqualTo("password", utilisateur.getPassword())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {

                                Log.d(TAG, "Utilisateur trouvé: ");
                                connected.isConnected(true);
                                //Recuperation de données
                                for(QueryDocumentSnapshot doucument:task.getResult() ){

                                    //Enregistrement de donnees en session
                                      editor.putString("Nom",doucument.getString("nom"));
                                      editor.putString("Prenom",doucument.getString("prenom"));
                                      editor.putString("Telephone",doucument.getString("telephone"));
                                      editor.putString("TypeUtilisateur",doucument.getString("typeUtilisateur"));
                                      editor.apply();

                                }

                            } else {

                                Log.e(TAG, "Utilisateur non trouvé ");
                                connected.isConnected(false);
                            }
                        } else {

                            Log.e(TAG, "Erreur: ", task.getException());
                            connected.isConnected(false);

                        }
                    }
                });
    }



    @Override
    public boolean seDeconnecter(Utilisateur utilisateur) {
        return false;
    }

    @Override
    public boolean modifierInfoCompte(Utilisateur utilisateur) {
        return false;
    }

    @Override
    public boolean supprimerCompte(Utilisateur utilisateur) {
        return false;
    }

    @Override
    public boolean creerCompte(Utilisateur utilisateur,IConnected connected) {

                  UserRef.set(utilisateur).addOnCompleteListener(task -> {

                      if (task.isSuccessful()) {
                          Log.d(TAG,"Utilisateur inséré avec succès");
                          connected.isConnected(true);

                      } else {
                          Log.e(TAG, "Erreur: ", task.getException());
                          connected.isConnected(false);

                      }
                  });

        return false;

    }

    @Override
    public void isConnected(boolean ok) {

    }

}

