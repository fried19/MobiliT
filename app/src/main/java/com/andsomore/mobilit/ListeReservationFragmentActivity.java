package com.andsomore.mobilit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andsomore.mobilit.dao.TraitementClient;
import com.andsomore.mobilit.entite.Reservation;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class ListeReservationFragmentActivity extends Fragment {
    private View view;
    private TextView tvItemEmpty;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference reservationRef=db.collection("RESERVATION");
    private ListeReservationAdapter adapter;
    public ListeReservationFragmentActivity() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          view=inflater.inflate(R.layout.liste_reservation_fragment,container,false);
          setHasOptionsMenu(true);
          InitView();
          new TraitementClient().updateReservation();
          setUpRecyclerView(((ReservationActivity)getActivity()).getnumTelephone());
//          ((ReservationActivity)getActivity()).getnumTelephone();
          return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitView();
    }

    private void InitView(){

        tvItemEmpty=view.findViewById(R.id.tvItemEmpty);
    }

    protected void setUpRecyclerView(String numTel) {

            Query query = reservationRef.whereEqualTo("numTelephone",numTel)
                    .orderBy("dateReservation", Query.Direction.DESCENDING);

            FirestoreRecyclerOptions<Reservation> options=new FirestoreRecyclerOptions.Builder<Reservation>()
                    .setQuery(query,Reservation.class)
                    .build();

        adapter = new ListeReservationAdapter(options);
        RecyclerView recyclerView=view.findViewById(R.id.rvReservation);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

        //Suppression d'une réservation dès qu'on glisse le RecyclerView
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
                checkEmptyItem();
            }
        }).attachToRecyclerView(recyclerView);

       query.get().addOnCompleteListener(task -> {
                if(task.getResult().isEmpty()){
                      tvItemEmpty.setVisibility(View.VISIBLE);
                }else tvItemEmpty.setVisibility(View.INVISIBLE);
       });
    }

    private void checkEmptyItem(){
        if (adapter.getItemCount() == 0){
            tvItemEmpty.setVisibility(View.VISIBLE);
        }

    }

    //Actualiser la page
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_actualiser){
            ((ReservationActivity)getActivity()).setRefresh();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }



}
