package com.andsomore.mobilit;



import android.annotation.TargetApi;
import android.app.AlertDialog;

import com.andsomore.mobilit.idao.IValue;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.MonthAdapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import com.andsomore.mobilit.Singleton.ApplicationContext;
import com.andsomore.mobilit.dao.TraitementClient;
import com.andsomore.mobilit.dao.TraitementUtilisateur;
import com.andsomore.mobilit.entite.Reservation;
import com.andsomore.mobilit.idao.IResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;

public class NewReservationFragmentActivity extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private View view;
    private Button btReservation;
    private TextView tvDate, tvMontant;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int Year, Month, Day;
    private Spinner spvilleDepart, spvilleArrive;
    private String villeDepart;
    private String villeArrivee;
    private String dateVoyage;
    private Date date;
    private int amount;
    private AlertDialog alertDialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference vehiculeRef = db.collection("VEHICULE");

    private static final int PAYGATE_ACTIVITY_REQUEST_CODE = 0;

    //Initialisation de la vue
    public NewReservationFragmentActivity() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_reservation_fragment, container, false);
        setHasOptionsMenu(true);
        InitViews();
        tvDate.setOnClickListener(this);
        btReservation.setOnClickListener(this);
        spvilleDepart.setOnItemSelectedListener(this);
        spvilleArrive.setOnItemSelectedListener(this);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitViews();

    }

    protected void InitViews() {
        btReservation = view.findViewById(R.id.btReservation);
        tvDate = view.findViewById(R.id.tvDate);
        spvilleDepart = view.findViewById(R.id.spDepart);
        spvilleArrive = view.findViewById(R.id.spArrive);
        tvMontant = view.findViewById(R.id.tvMontant);
        alertDialog = new SpotsDialog(getActivity());
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);


    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYGATE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                //refreshPage();
                //Récupération de l'id généré pour le paiement du client
                String idClient = data.getStringExtra("idClient");
                Reservation reservation = new Reservation();
                reservation.setVilleDepart(villeDepart);
                reservation.setVilleArrivee(villeArrivee);
                reservation.setNomBus("...");
                reservation.setJourVoyage(date);
                //récupération de la date  (au format mardi 13 août 2019 par ex)

//                 reservation.setEtatPaiement("...");
//                 reservation.setModePaiement("...");
                reservation.setIdClient(idClient);
                reservation.setAmount(amount);
                reservation.setNumTelephone(((ReservationActivity) getActivity()).getnumTelephone());
//                 reservation.setCodeRef(0);
                TraitementClient client = new TraitementClient();
                client.acheterBillet(reservation, ok -> {
                    if (ok) {
                        ((ReservationActivity) getActivity()).onRefresh();
                        ((ReservationActivity) getActivity()).navigateFragment(1);
                        alertDialog.show();
                        alertDialog.setMessage("Actualisation...");
                        new TraitementClient().updateReservation();
                        alertDialog.dismiss();
                        View view = getActivity().findViewById(R.id.listReservation);
                        Snackbar.make(view, "Veuillez consulter vos réservation ici", Snackbar.LENGTH_LONG).show();
                    } else {
                        View view = getActivity().findViewById(R.id.newReservation);
                        Snackbar.make(view, "La réservation n'as pas été effectuée.Veuillez ressayer.", Snackbar.LENGTH_SHORT).show();
                    }

                });


            } else if (requestCode == getActivity().RESULT_CANCELED) {
                View view = getActivity().findViewById(R.id.newReservation);
                Snackbar.make(view, "Le paiement n'as pas été effectué", Snackbar.LENGTH_LONG).show();
            }
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        //Popup calendrier
        if (view == tvDate) {


            datePickerDialog = DatePickerDialog.newInstance(this, Year, Month, Day);
            datePickerDialog.setThemeDark(false);
            datePickerDialog.showYearPickerFirst(false);
            datePickerDialog.setTitle("Nos jours de voyage");


            // Setting Min Date to today date
            Calendar min_date_c = Calendar.getInstance();
            datePickerDialog.setMinDate(min_date_c);


            // Setting Max Date to next 2 years
            Calendar max_date_c = Calendar.getInstance();
            max_date_c.set(Calendar.DATE, Day + 6);
            datePickerDialog.setMaxDate(max_date_c);


            //Disable all SUNDAYS and SATURDAYS between Min and Max Dates
          /*  for (Calendar loopdate = min_date_c; min_date_c.before(max_date_c); min_date_c.add(Calendar.DATE, 1), loopdate = min_date_c) {
                int dayOfWeek = loopdate.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                    Calendar[] disabledDays =  new Calendar[1];
                    disabledDays[0] = loopdate;
                    datePickerDialog.setDisabledDays(disabledDays);
                }
            }*/

            datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
        }

        if (view == btReservation) {

            dateVoyage = tvDate.getText().toString();
            amount = Integer.parseInt(tvMontant.getText().toString());
            //Conversion de la dateVoyage en type Date
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", new Locale("fr", "FR"));
            try {
                date = formatter.parse(dateVoyage);//catch exception
            } catch (ParseException e) {

                e.printStackTrace();
            }

            //Affiche erreur si le deux villes sont équivalentes
            if (amount == 0) {
                TextView errorText = (TextView) spvilleArrive.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);
                errorText.setText("Choisissez");
            } else if (dateVoyage.equals("Choisissez")) {
                tvDate.setTextColor(Color.RED);
            } else {

                verifierPlaceDispo(villeDepart, villeArrivee, new IValue() {
                    @Override
                    public void getValue(int value) {
                        if (value > 5) {
                            Intent intent = new Intent(NewReservationFragmentActivity.this.getActivity(), PaygatePayementPageActivity.class);
                            //Envoie du montant
                            intent.putExtra("amount", amount);
                            NewReservationFragmentActivity.this.startActivityForResult(intent, PAYGATE_ACTIVITY_REQUEST_CODE);
                        } else if((value >= 1) && (value <= 5)) {
                            String text="Il ne reste que "+value+" place pour ce bus.Voulez-vous poursuivre le paiement?";
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                            builder.setMessage(text);
                            builder.setPositiveButton("OUI", (dialogInterface, i) -> {
                                Intent intent = new Intent(NewReservationFragmentActivity.this.getActivity(), PaygatePayementPageActivity.class);
                                //Envoie du montant
                                intent.putExtra("amount", amount);
                                NewReservationFragmentActivity.this.startActivityForResult(intent, PAYGATE_ACTIVITY_REQUEST_CODE);
                                dialogInterface.dismiss();
                            });
                            builder.setNegativeButton("NON", (dialogInterface, i) -> dialogInterface.dismiss());

                            androidx.appcompat.app.AlertDialog dialog = builder.create();
                            dialog.show();
                        }else if(value == 0){
                            Toast.makeText(getActivity(),"Il ne reste plus de place disponible pour ce voyage.Veuillez choisir une autre date de voyage.",Toast.LENGTH_LONG).show();
                        }
                    }

                });


            }

        }
    }

    //Methode qui génère le montant en fonction des villes séléctionnées
    private int genereMontant(String villeDepart, String villeArrivee) {
        int montant = 0;
        if (!villeDepart.equals(villeArrivee)) {
            if (((villeDepart.equals("Lomé")) && (villeArrivee.equals("Atakpamé"))) ||
                    ((villeDepart.equals("Atakpamé")) && (villeArrivee.equals("Lomé")))) {
                montant = 2800;
            } else if (((villeDepart.equals("Lomé")) && (villeArrivee.equals("Sokodé"))) ||
                    ((villeDepart.equals("Sokodé")) && (villeArrivee.equals("Lomé")))) {
                montant = 5100;
            } else if (((villeDepart.equals("Lomé")) && (villeArrivee.equals("Kara"))) ||
                    ((villeDepart.equals("Kara")) && (villeArrivee.equalsIgnoreCase("Lomé")))) {
                montant = 5900;
            } else if (((villeDepart.equals("Lomé")) && (villeArrivee.equals("Dapaong"))) ||
                    ((villeDepart.equals("Dapaong")) && (villeArrivee.equals("Lomé")))) {
                montant = 8700;
            }
        }

        return montant;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        villeDepart = spvilleDepart.getSelectedItem().toString();
        villeArrivee = spvilleArrive.getSelectedItem().toString();
        int Montant = genereMontant(villeDepart, villeArrivee);
        tvMontant.setText(String.valueOf(Montant));

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //Actualiser la page
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_actualiser) {
            ((ReservationActivity) getActivity()).setRefresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    protected void refreshPage() {

        spvilleDepart.setSelection(0, true);
        spvilleArrive.setSelection(0, true);
        if (tvDate.getCurrentTextColor() == Color.RED) {
            tvDate.setTextColor(Color.BLACK);
        }
        tvDate.setText("Choisissez");
        tvMontant.setText("0");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void verifierPlaceDispo(String villeDepart, String villeArrivee, IValue value) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE", new Locale("fr", "FR"));
        String jour=null ;
        try {

            jour = formatter.format(date);

            } catch (Exception e) {
                e.printStackTrace();
            }
        String direction = villeDepart + "-" + villeArrivee;
        String finalJour = jour;
        vehiculeRef
                .whereEqualTo("direction", direction)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Map<String, Long> map = (Map<String,Long>)document.get("placeDisponible");

                                  value.getValue(map.get(finalJour).intValue());

                        }
                    }else {
                        Log.e(TAG, "Erreur: ", task.getException());
                    }


                });

    }
        @Override
        public void onDateSet (DatePickerDialog view,int year, int monthOfYear, int dayOfMonth){
            String date = Day + "/" + "0" + (Month + 1) + "/" + Year;
            if (tvDate.getCurrentTextColor() == Color.RED) {
                tvDate.setTextColor(Color.BLACK);
            }

            tvDate.setText(date);

        }


    }
