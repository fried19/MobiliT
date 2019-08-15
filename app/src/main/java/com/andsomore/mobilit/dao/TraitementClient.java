package com.andsomore.mobilit.dao;

import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.andsomore.mobilit.ListeReservationAdapter;
import com.andsomore.mobilit.ReservationActivity;
import com.andsomore.mobilit.Singleton.ApplicationContext;
import com.andsomore.mobilit.Singleton.VolleySingleton;
import com.andsomore.mobilit.entite.Reservation;
import com.andsomore.mobilit.entite.Utilisateur;
import com.andsomore.mobilit.idao.IClient;
import com.andsomore.mobilit.idao.IResult;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class TraitementClient implements IClient<Reservation> {
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference reservationRef=db.collection("RESERVATION");
    private ListeReservationAdapter adapter;


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

        }, error -> {

        });
        queue.add(request);
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
