package com.andsomore.mobilit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.RecyclerView;

import com.andsomore.mobilit.entite.Reservation;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ListeReservationAdapter extends FirestoreRecyclerAdapter<Reservation, ListeReservationAdapter.ReservationHolder> {
    private final ObservableSnapshotArray<Reservation> Snapshots;


    public ListeReservationAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options) {
        super(options);
        Snapshots = options.getSnapshots();

        if (options.getOwner() != null) {
            options.getOwner().getLifecycle().addObserver(this);
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void startListening() {
        if (!Snapshots.isListening(this)) {
             Snapshots.addChangeEventListener(this);
        }

    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stopListening() {
        Snapshots.removeChangeEventListener(this);
        notifyDataSetChanged();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void cleanup(LifecycleOwner source) {
        source.getLifecycle().removeObserver(this);
    }

    public ObservableSnapshotArray<Reservation> getSnapshots() {
        return Snapshots;

    }

    @NonNull
    public Reservation getItem(int position) {
        return Snapshots.get(position);
    }


    @Override
    public int getItemCount() {
        return Snapshots.isListening(this) ? Snapshots.size() : 0;
    }


    @Override
    public void onChildChanged(@NonNull ChangeEventType type,
                               @NonNull DocumentSnapshot snapshot,
                               int newIndex,
                               int oldIndex) {
        switch (type) {
            case ADDED:
                notifyItemInserted(newIndex);
                break;
            case CHANGED:
                notifyItemChanged(newIndex);
                break;
            case REMOVED:
                notifyItemRemoved(oldIndex);
                break;
            case MOVED:
                notifyItemMoved(oldIndex, newIndex);
                break;
            default:
                throw new IllegalStateException("Incomplete case statement");
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull ReservationHolder holder, int i, @NonNull Reservation reservation) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy HH:mm",new Locale("fr","FR"));
        SimpleDateFormat formatter1=new SimpleDateFormat("EEEE dd MMMM yyyy",new Locale("fr","FR"));

        try {
            if((reservation.getJourVoyage() != null) && (reservation.getDateReservation()== null)){
                String jourVoyage=formatter1.format(reservation.getJourVoyage());
                holder.tvJourvoyage.setText(jourVoyage);

            }else if((reservation.getJourVoyage() != null) && (reservation.getDateReservation() != null)){
                String dateReservation = formatter.format(reservation.getDateReservation());
                String jourVoyage=formatter1.format(reservation.getJourVoyage());//catch exception
                holder.tvDatereservation.setText(dateReservation);
                holder.tvJourvoyage.setText(jourVoyage);
            }


        } catch (Exception e) {

            Log.e("Erreur: ",e.getMessage());
        }


         holder.tvLieuvoyage.setText(String.format("%s-%s", reservation.getVilleDepart(), reservation.getVilleArrivee()));
        if(reservation.getNomBus() == null){
            holder.tvNombus.setText("...");

        }else
            holder.tvNombus.setText(reservation.getNomBus());

         if((reservation.getJourVoyage() != null) &&(reservation.getCodeRef() !=null )){

             holder.tvModepaiement.setText(reservation.getModePaiement()+",  codeRef : "+reservation.getCodeRef());
         }else if((reservation.getJourVoyage() != null) &&(reservation.getCodeRef() ==null )){
             holder.tvModepaiement.setText(reservation.getModePaiement()+",  codeRef : ... ");
         }
         if(reservation.getNumPlace() == 0 ){

             holder.tvNumplacebus.setText("...");
         }else
           holder.tvNumplacebus.setText(Integer.toString(reservation.getNumPlace()));


         if(reservation.getEtatPaiement().equals("Validé")){

             //Rendre les composants initialement inivisible, visible
             holder.tvicSucces.setVisibility(View.VISIBLE);
             holder.tvDeviseSucces.setVisibility(View.VISIBLE);
             holder.tvSucces.setVisibility(View.VISIBLE);
             //Affectation du montant
             holder.tvicSucces.setText(Integer.toString(reservation.getAmount()));

         }else if(reservation.getEtatPaiement().equals("En cours")){

             holder.tvicEncours.setVisibility(View.VISIBLE);
             holder.tvDeviseencours.setVisibility(View.VISIBLE);
             holder.tvEncours.setVisibility(View.VISIBLE);
             holder.tvicEncours.setText(Integer.toString(reservation.getAmount()));
         }else if(reservation.getEtatPaiement().equals("Annulé")){

             holder.tvicAnnule.setVisibility(View.VISIBLE);
             holder.tvDeviseannule.setVisibility(View.VISIBLE);
             holder.tvAnnule.setVisibility(View.VISIBLE);
             holder.tvicAnnule.setText(Integer.toString(reservation.getAmount()));
         }else if(reservation.getEtatPaiement().equals("Expiré")){

             holder.tvicExpire.setVisibility(View.VISIBLE);
             holder.tvDeviseexpire.setVisibility(View.VISIBLE);
             holder.tvExpire.setVisibility(View.VISIBLE);
             holder.tvicExpire.setText(Integer.toString(reservation.getAmount()));
         }




    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();

    }


    @NonNull
    @Override
    public ReservationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.liste_reservation_item,parent,false);
        return new ReservationHolder(view);
    }

    @Override
    public void onDataChanged() {

    }

    @Override
    public void onError(FirebaseFirestoreException e) {
           Log.e("Erreur: ",e.getMessage());
    }




    class ReservationHolder extends RecyclerView.ViewHolder{

        private TextView tvDatereservation;
        private TextView tvLieuvoyage;
        private TextView tvJourvoyage;
        private TextView tvicSucces,tvicEncours,tvicAnnule,tvicExpire;
        private TextView tvDeviseSucces,tvDeviseencours,tvDeviseannule,tvDeviseexpire;
        private TextView tvSucces,tvEncours,tvAnnule,tvExpire;
        private TextView tvNombus;
        private TextView tvNumplacebus;
        private TextView tvModepaiement;


        public ReservationHolder(@NonNull View itemView) {
            super(itemView);
            InitView();

        }





        //Initialisation des composants de la vue liste_reservation_item
        private void InitView(){
            tvDatereservation=itemView.findViewById(R.id.tvDateReservation);
            tvLieuvoyage=itemView.findViewById(R.id.tvLieuVoyage);
            tvJourvoyage=itemView.findViewById(R.id.tvJourVoyage);
            tvNombus=itemView.findViewById(R.id.tvNomBus);
            tvNumplacebus=itemView.findViewById(R.id.tvNumPlace);
            tvModepaiement=itemView.findViewById(R.id.tvModePaiement);
            //Initialisation des TextView pour le montants
            tvicSucces=itemView.findViewById(R.id.tvIcSucces);
            tvicEncours=itemView.findViewById(R.id.tvIcEnCours);
            tvicAnnule=itemView.findViewById(R.id.tvIcAnnule);
            tvicExpire=itemView.findViewById(R.id.tvIcExpire);
            //Initialisation des TextView pour la devise
            tvDeviseSucces=itemView.findViewById(R.id.tvDeviseSucces);
            tvDeviseencours=itemView.findViewById(R.id.tvDeviseEnCours);
            tvDeviseannule=itemView.findViewById(R.id.tvDeviseAnnule);
            tvDeviseexpire=itemView.findViewById(R.id.tvDeviseExpire);
            //Initialisation des TextView pour le libelle de l'etat paiement
            tvSucces=itemView.findViewById(R.id.tvPaiementSucces);
            tvEncours=itemView.findViewById(R.id.tvPaiementEnCours);
            tvAnnule=itemView.findViewById(R.id.tvPaiementAnnule);
            tvExpire=itemView.findViewById(R.id.tvPaiementExpire);


        }

    }


}
