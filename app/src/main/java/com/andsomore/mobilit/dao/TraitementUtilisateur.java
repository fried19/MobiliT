package com.andsomore.mobilit.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.andsomore.mobilit.entite.Utilisateur;
import com.andsomore.mobilit.idao.IUtilisateur;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import io.opencensus.common.Function;

import static android.content.ContentValues.TAG;


public class TraitementUtilisateur implements IUtilisateur<Utilisateur> {
              private boolean res;
              private static boolean result;

    public boolean getResult() {
        return result;
    }

    public boolean getRes() {
        return res;
    }

    public void setRes(boolean res) {
        this.res = res;
    }



    FirebaseFirestore db= FirebaseFirestore.getInstance();
             DocumentReference UserRef =db.collection("UTILISATEUR").document();
            // CollectionReference reference = db.collection("UTILISATEUR");

    @Override
    public boolean seConnecter(Utilisateur utilisateur) {

            db.collection("UTILISATEUR")
                    .whereEqualTo("email", utilisateur.getEmail())
                    .whereEqualTo("password", utilisateur.getPassword())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Runnable runnable=()-> {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {

                                        Log.d(TAG, "Utilisateur trouvé: ");


                                    } else {

                                        Log.e(TAG, "Utilisateur non trouvé ");
                                    }
                                } else {

                                    Log.e(TAG, "Erreur: ", task.getException());
                                }
                            };

                        }
                    });



   return true;
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
    public boolean creerCompte(Utilisateur utilisateur) {

                  UserRef.set(utilisateur).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          boolean ok;
                          if (task.isSuccessful()) {
                              Log.d(TAG,"Utilisateur inséré avec succès");
                              res=true;
                          } else {
                              Log.e(TAG, "Erreur: ", task.getException());
                              res =false;
                          }
                      }
                  });

        return res;

    }
}
