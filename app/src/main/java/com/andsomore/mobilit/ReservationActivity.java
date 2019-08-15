package com.andsomore.mobilit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class ReservationActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
       private Toolbar toolbar;
       private TabLayout tabLayout;
       private AppBarLayout appBarLayout;
       private ViewPager viewPager;
       private SwipeRefreshLayout srReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        InitViews();
        srReservation.setColorSchemeResources(R.color.colorPrimaryDark);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewPageAdapter adapter=new ViewPageAdapter(getSupportFragmentManager());

        //Ajout de fragments
        adapter.AddFragment(new NewReservationFragmentActivity(),"NOUVEAU");
        adapter.AddFragment(new ListeReservationFragmentActivity(),"HISTORIQUE");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager,true);
        srReservation.setOnRefreshListener(this);
    }

    private void InitViews(){
        toolbar =  findViewById(R.id.toolbar);
        tabLayout= findViewById(R.id.tabLayout);
        appBarLayout=findViewById(R.id.appBar);
        viewPager=findViewById(R.id.viewpager);
        srReservation =findViewById(R.id.srReservation);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reservation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
            if(id == android.R.id.home){
                onBackPressed();
                return true;
            }

            if(id == R.id.action_actualiser){

                return false;
            }

        return super.onOptionsItemSelected(item);
    }








    public void navigateFragment(int position){
        viewPager.setCurrentItem(position, true);

    }


    @Override
    public void onRefresh() {
        Fragment page=getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager +":"+viewPager.getCurrentItem());
            srReservation.setRefreshing(true);
            switch ((viewPager.getCurrentItem())){
                case 0:
                    NewReservationFragmentActivity fragmentN=(NewReservationFragmentActivity) page;
                    if (fragmentN != null) {
                        fragmentN.refreshPage();
                    }
                    srReservation.setRefreshing(false);
                    break;
                case 1:
                    ListeReservationFragmentActivity fragmentL=(ListeReservationFragmentActivity) page;
                    if (fragmentL != null) {
                        fragmentL.setUpRecyclerView(getnumTelephone());
                    }
                    srReservation.setRefreshing(false);
                    break;
            }


    }

    public void setRefresh(){
        this.onRefresh();
    }

    //Obtention du numero de telephone du client
    public String getnumTelephone(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String Telephone=preferences.getString("Telephone","Nom defini");
        return Telephone;
    }
}
