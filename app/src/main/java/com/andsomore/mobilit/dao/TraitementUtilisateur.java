package com.andsomore.mobilit.dao;

import android.util.Log;

import com.andsomore.mobilit.entite.Utilisateur;
import com.andsomore.mobilit.idao.IConnected;
import com.andsomore.mobilit.idao.IUtilisateur;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;


public class TraitementUtilisateur implements IUtilisateur<Utilisateur> {


    FirebaseFirestore db= FirebaseFirestore.getInstance();
    DocumentReference UserRef =db.collection("UTILISATEUR").document();

    @Override
    public void seConnecter(Utilisateur utilisateur, IConnected connected) {
        db.collection("UTILISATEUR")
                .whereEqualTo("email", utilisateur.getEmail())
                .whereEqualTo("password", utilisateur.getPassword())
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {

                            Log.d(TAG, "Utilisateur trouvé: ");
                            connected.isConnected(true);

                        } else {

                            Log.e(TAG, "Utilisateur non trouvé ");
                            connected.isConnected(false);
                        }
                    } else {

                        Log.e(TAG, "Erreur: ", task.getException());
                        connected.isConnected(false);

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

