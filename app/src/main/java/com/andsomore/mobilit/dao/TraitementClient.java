package com.andsomore.mobilit.dao;

import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.andsomore.mobilit.ListeReservationAdapter;
import com.andsomore.mobilit.Singleton.ApplicationContext;
import com.andsomore.mobilit.Singleton.VolleySingleton;
import com.andsomore.mobilit.entite.Reservation;
import com.andsomore.mobilit.idao.IClient;
import com.andsomore.mobilit.idao.IGenplace;
import com.andsomore.mobilit.idao.IResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class TraitementClient implements IClient<Reservation> {
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference reservationRef=db.collection("RESERVATION");
    private CollectionReference vehiculeRef=db.collection("VEHICULE");
    private ListeReservationAdapter adapter;
    private SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(ApplicationContext.getAppContext());



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void acheterBillet(Reservation reservation, IResult result) {
        RequestQueue queue= VolleySingleton.getInstance(ApplicationContext.getAppContext()).getRequestQueue();
        String auth_token= ApplicationContext.Token;
        String identifier=reservation.getIdClient();
        String url="https://paygateglobal.com/api/v2/status";
        Map<String, String> params = new HashMap();
        try {
            params.put("auth_token", auth_token);
            params.put("identifier", identifier);
        }catch (Exception e){
            e.printStackTrace();
        }

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, parameters, response -> {
                 try {
                     String ref=response.getString("payment_reference");



                     reservation.setModePaiement(response.getString("payment_method"));
                     String status=response.getString("status");
                     if(status.contains("0")){
                         reservation.setCodeRef(ref);
                         String dateReservation= response.getString("datetime").substring(0,10)+" "+response.getString("datetime").substring(11,16);
                         SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm",new Locale("fr","FR"));
                         reservation.setDateReservation(format.parse(dateReservation));
                         reservation.setEtatPaiement("Validé");
                     }else if(status.contains("2")){
                         reservation.setCodeRef("...");
                         reservation.setDateReservation(null);
                         reservation.setEtatPaiement("En cours");
                     }else if(status.contains("4")){
                         reservation.setCodeRef("...");
                         reservation.setDateReservation(null);
                         reservation.setEtatPaiement("Expiré");
                     }else if(status.contains("6")){
                         reservation.setCodeRef("...");
                         reservation.setDateReservation(null);
                         reservation.setEtatPaiement("Annulé");

                     }

                 }catch (Exception e){

                       e.printStackTrace();
                 }

                 reservationRef.add(reservation)
                    .addOnSuccessListener(documentReference -> {
                        result.getResult(true);
                    })
                    .addOnFailureListener(e -> {
                        result.getResult(false);
                        Log.e(TAG, "Erreur : ", e);
                    });

        }, error -> Log.e(TAG, "Erreur Volley:  ", error));
        queue.add(request);
    }


public void updateReservation(){
        String numTel=preferences.getString("Telephone","non defini");
        reservationRef
                .whereEqualTo("numTelephone",numTel)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String idClient=document.getString("idClient");
                            String villeDepart=document.getString("villeDepart");
                            String idReservation=document.getId();
                            Date jourVoyage = document.getDate("jourVoyage");
                            String villeArrivee=document.getString("villeArrivee") ;
                            int firePlace =document.getLong("numPlace").intValue();

                            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("EEEE", new Locale("fr", "FR"));
                            String jour=null ;

                            try {
                                jour = formatter.format(jourVoyage);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String finalJour = jour;
             //Requete Json pour vérifier l'état du paiement et mettre à jour la reservation du client

                            RequestQueue queue= VolleySingleton.getInstance(ApplicationContext.getAppContext()).getRequestQueue();
                            String auth_token= ApplicationContext.Token;
                            String identifier=idClient;
                            String url="https://paygateglobal.com/api/v2/status";
                            Map<String, String> params = new HashMap();
                            try {
                                params.put("auth_token", auth_token);
                                params.put("identifier", identifier);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            JSONObject parameters = new JSONObject(params);
                            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, url, parameters, response -> {

                                try {
                                    String status=response.getString("status");
                                    if(status.contains("0") && firePlace == 0){

                                        genererPlace(villeDepart,villeArrivee,finalJour, (nomBus, numPlace ) -> reservationRef
                                                .document(idReservation)
                                                .update("nomBus",nomBus,"numPlace",numPlace,"etatPaiement","Validé")
                                                .addOnSuccessListener(aVoid -> {

                                                })
                                                .addOnFailureListener(e -> e.printStackTrace()));


                                    }


                                } catch (Exception e) {

                                    e.printStackTrace();
                                }

                            }, error -> error.printStackTrace());

                            queue.add(request);




                        }




                    }
                });
}


public void genererPlace(String villeDepart, String villeArrivee,String jour, IGenplace genplace){
        String direction=villeDepart+"-"+villeArrivee;

        vehiculeRef
                .whereEqualTo("direction",direction)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Long> map = (Map<String,Long>)document.get("placeDisponible");
                            int placeTotale,placeDisponible,numPlace;
                            String idVehicule,nomBus;
                            placeTotale=document.getLong("placeTotale").intValue();
                            placeDisponible=map.get(jour).intValue();
                            idVehicule=document.getId();
                            nomBus=document.getString("nomBapteme");
                            if(placeDisponible != 0){
                                numPlace=placeTotale-(placeDisponible) +1;
                                genplace.getPlace(nomBus,numPlace);
                                placeDisponible--;
                                vehiculeRef
                                        .document(idVehicule)
                                        .update("placeDisponible."+jour,placeDisponible)
                                        .addOnSuccessListener(aVoid -> { })
                                        .addOnFailureListener(e -> e.printStackTrace());
                            }

                        }
                    } else {
                        Log.e(TAG, "Euureur:  ", task.getException());
                    }
                });


}




    @Override
    public boolean annulerAchatBillet(Reservation reservation) {
        return false;
    }

    @Override
    public boolean crediterCompte(Reservation reservation) {
        return false;
    }

    @Override
    public boolean debiterCompte(Reservation reservation) {
        return false;
    }



    @Override
    public List listClient() {
        return null;
    }



}
