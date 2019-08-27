package com.andsomore.mobilit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.icu.text.Transliterator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andsomore.mobilit.entite.Reservation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.BubbleLayout;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.light.Position;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.PathModel;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_BOTTOM;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMapClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String GEOJSON_SOURCE_ID = "GEOJSON_SOURCE_ID";
    private static final String MARKER_IMAGE_ID = "MARKER_IMAGE_ID";
    private static final String MARKER_LAYER_ID = "MARKER_LAYER_ID";
    private static final String CALLOUT_LAYER_ID = "CALLOUT_LAYER_ID";
    private static final String PROPERTY_SELECTED = "selected";
    private static final String PROPERTY_NAME = "name";
    private static final String PROPERTY_CAPITAL = "capital";
    private MapView mapView;
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private SymbolManager symbolManager;
    private List<Symbol> symbols = new ArrayList<>();
    private LocationEngine locationEngine;
    private Point originPosition;
    private Point destinationPosition;
    private NavigationMapRoute mapRoute;
    private DirectionsRoute currentRoute;
    private CurvedBottomNavigationView bottomNavigationView;
    private VectorMasterView fab,fab1,fab2;
    private FloatingActionButton FAB;
    RelativeLayout lin_id;
    PathModel outline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.mapBox_key) );
        setContentView(R.layout.activity_main);
        InitView();
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        bottomNavigationView.inflateMenu(R.menu.main_menu);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        bottomNavigationView.getMenu().getItem(1).setIcon(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }



    private void InitView() {
        mapView = findViewById(R.id.mapView);
        bottomNavigationView = (CurvedBottomNavigationView)findViewById(R.id.bottom_nav);
        fab = (VectorMasterView)findViewById(R.id.fab);
        fab1 = (VectorMasterView)findViewById(R.id.fab1);
        fab2 = (VectorMasterView)findViewById(R.id.fab2);
        FAB = (FloatingActionButton)findViewById(R.id.myLocationButton);
        lin_id = (RelativeLayout)findViewById(R.id.lin_id);

    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                enableLocationComponent(style);
                symbolManager = new SymbolManager(mapView, mapboxMap, style);
                symbolManager.setIconAllowOverlap(true);
                symbolManager.setTextAllowOverlap(false);
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
                mapboxMap.getStyle().addImage("my-marker",bm);
                symbolManager.addClickListener(new OnSymbolClickListener() {
                    @Override
                    public void onAnnotationClick(Symbol symbol) {
                        destinationPosition = Point.fromLngLat(symbol.getLatLng().getLongitude(),symbol.getLatLng().getLatitude());
                        originPosition = Point.fromLngLat(mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude(),mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude());
                        getRoute(originPosition,destinationPosition);
                        Toast.makeText(MainActivity.this,"clicked  " + symbol.getTextField().toLowerCase().toString(),Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Voulez-vous naviguer vers cette agence de voyage?");
                        builder.setPositiveButton("OUI", (dialogInterface, i) -> {

                            NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                    .directionsRoute(currentRoute)
                                    .shouldSimulateRoute(true)
                                    .build();
                            NavigationLauncher.startNavigation(MainActivity.this,options);

                        });
                        builder.setNegativeButton("NON", (dialogInterface, i) -> dialogInterface.dismiss());

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                setMarkerOnCities();
                initMapStuff(style);
            }
        });

    }

    private void getRoute(Point origin, Point destination){
        NavigationRoute.builder(MainActivity.this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null){
                            Log.e("Erreur: ","Token incorrect");
                            return;
                        } else if (response.body().routes().size() == 0){
                            Toast.makeText(MainActivity.this,"Aucun chemin trouvé. " ,Toast.LENGTH_SHORT).show();
                            return;
                        }
                         currentRoute = response.body().routes().get(0);
                        if(mapRoute != null){
                          mapRoute.updateRouteVisibilityTo(false);
                          mapRoute.updateRouteArrowVisibilityTo(false);

                        } else {
                            mapRoute = new NavigationMapRoute(null,mapView,mapboxMap);
                        }
                        mapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e("Erreur: ",t.getMessage());
                    }
                });
    }

    private void setMarkerOnCities() {
        List<SymbolOptions> options = new ArrayList<>();
        //Lomé
            options.add(new SymbolOptions()
                    .withLatLng(new LatLng(	6.172497,1.231362))
                    .withIconImage("my-marker")
                    //set the below attributes according to your requirements
                    .withIconSize(1.5f)
                    .withIconOffset(new Float[] {0f,-1.5f})
                    .withZIndex(10)
                    .withTextField("Agence  LOME")
                    .withTextHaloColor("rgba(255, 255, 255, 100)")
                    .withTextHaloWidth(5.0f)
                    .withTextAnchor("top")
                    .withTextOffset(new Float[] {0f, 1.5f}));

        //Atakpamé
        options.add(new SymbolOptions()
                .withLatLng(new LatLng(7.528691 ,1.130505))
                .withIconImage("my-marker")
                //set the below attributes according to your requirements
                .withIconSize(1.5f)
                .withIconOffset(new Float[] {0f,-1.5f})
                .withZIndex(10)
                .withTextField("Agence  ATAKPAME")
                .withTextHaloColor("rgba(255, 255, 255, 100)")
                .withTextHaloWidth(5.0f)
                .withTextAnchor("top")
                .withTextOffset(new Float[] {0f, 1.5f}));
        //Sokodé
        options.add(new SymbolOptions()
                .withLatLng(new LatLng(	8.977983 ,1.144898))
                .withIconImage("my-marker")
                //set the below attributes according to your requirements
                .withIconSize(1.5f)
                .withIconOffset(new Float[] {0f,-1.5f})
                .withZIndex(10)
                .withTextField("Agence  SOKODE")
                .withTextHaloColor("rgba(255, 255, 255, 100)")
                .withTextHaloWidth(5.0f)
                .withTextAnchor("top")
                .withTextOffset(new Float[] {0f, 1.5f}));
        //Kara
        options.add(new SymbolOptions()
                .withLatLng(new LatLng(	9.546837 , 1.193264))
                .withIconImage("my-marker")
                //set the below attributes according to your requirements
                .withIconSize(1.5f)
                .withIconOffset(new Float[] {0f,-1.5f})
                .withZIndex(10)
                .withTextField("Agence  KARA")
                .withTextHaloColor("rgba(255, 255, 255, 100)")
                .withTextHaloWidth(5.0f)
                .withTextAnchor("top")
                .withTextOffset(new Float[] {0f, 1.5f}));

        //Dapaong
        options.add(new SymbolOptions()
                .withLatLng(new LatLng(	10.873306 , 0.201023))
                .withIconImage("my-marker")
                //set the below attributes according to your requirements
                .withIconSize(1.5f)
                .withIconOffset(new Float[] {0f,-1.5f})
                .withZIndex(10)
                .withTextField("Agence  DAPAONG")
                .withTextHaloColor("rgba(255, 255, 255, 100)")
                .withTextHaloWidth(5.0f)
                .withTextAnchor("top")
                .withTextOffset(new Float[] {0f, 1.5f}));

        symbols = symbolManager.create(options);

    }

    //Fonction pour actualiser la position de l'utilisateur
    private void initMapStuff(Style style) {
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapboxMap.getLocationComponent().getLastKnownLocation() != null)
                {
                    mapboxMap.animateCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory
                            .newLatLngZoom(new LatLng(mapboxMap.getLocationComponent().getLastKnownLocation().getLatitude()
                            ,mapboxMap.getLocationComponent().getLastKnownLocation().getLongitude()), 14));

                }
            }
        });
    }

    //Fonction pour initialiser les objet du MapBox
   @SuppressWarnings( {"MissingPermission"})
   private void enableLocationComponent(@NonNull Style loadedMapStyle) {
       if (PermissionsManager.areLocationPermissionsGranted(MainActivity.this)) {
           LocationComponent locationComponent = mapboxMap.getLocationComponent();
           locationComponent.activateLocationComponent(MainActivity.this, loadedMapStyle);
           locationComponent.setLocationComponentEnabled(true);
           locationComponent.setCameraMode(CameraMode.TRACKING);
           locationComponent.setRenderMode(RenderMode.COMPASS);
       } else {
           permissionsManager = new PermissionsManager(new PermissionsListener() {
               @Override
               public void onExplanationNeeded(List<String> permissionsToExplain) {
                   Toast.makeText(MainActivity.this, "location not enabled", Toast.LENGTH_LONG).show();
               }

               @Override
               public void onPermissionResult(boolean granted) {
                   if (granted) {
                       mapboxMap.getStyle(new Style.OnStyleLoaded() {
                           @Override
                           public void onStyleLoaded(@NonNull Style style) {
                               initMapStuff(style);
                           }
                       });
                   } else {
                       Toast.makeText(MainActivity.this, "Location services not allowed", Toast.LENGTH_LONG).show();
                   }
               }
           });
           permissionsManager.requestLocationPermissions(MainActivity.this);
       }
   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        mapRoute.updateRouteVisibilityTo(false);
        mapRoute.updateRouteArrowVisibilityTo(false);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapboxMap != null) {
            mapboxMap.removeOnMapClickListener(this);
        }
        mapView.onDestroy();

    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId())
        {
            case R.id.action_reservation:
                draw(6);
                lin_id.setX(bottomNavigationView.mFirstCurveControlPoint1.x);
                fab.setVisibility(View.VISIBLE);
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.GONE);
                bottomNavigationView.getMenu().getItem(0).setIcon(null);
                bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.ic_home);
                bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_account);
                drawAnimation(fab);
                startActivityForResult(new Intent(MainActivity.this,ReservationActivity.class),0);
                break;

            case R.id.action_home:
               // startActivity(new Intent(MainActivity.this,ReservationActivity.class));
                draw(2);
                lin_id.setX(bottomNavigationView.mFirstCurveControlPoint1.x);
                fab.setVisibility(View.GONE);
                fab1.setVisibility(View.VISIBLE);
                fab2.setVisibility(View.GONE);
                bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_car);
                bottomNavigationView.getMenu().getItem(1).setIcon(null);
                bottomNavigationView.getMenu().getItem(2).setIcon(R.drawable.ic_account);
                drawAnimation(fab1);
                break;

            case R.id.action_account:
               // startActivity(new Intent(MainActivity.this,ReservationActivity.class));
                draw();
                lin_id.setX(bottomNavigationView.mFirstCurveControlPoint1.x);
                fab.setVisibility(View.GONE);
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.VISIBLE);
                bottomNavigationView.getMenu().getItem(0).setIcon(R.drawable.ic_car);
                bottomNavigationView.getMenu().getItem(1).setIcon(R.drawable.ic_home);
                bottomNavigationView.getMenu().getItem(2).setIcon(null);
                drawAnimation(fab2);
                break;
        }

        return true;
    }

    private void draw() {

      bottomNavigationView.mFirstCurveStartPoint.set((bottomNavigationView.mNavigationBarWidth*10/12)
      -(bottomNavigationView.CURVE_CIRCLE_RADIUS*2)
      -(bottomNavigationView.CURVE_CIRCLE_RADIUS/3),0);

        bottomNavigationView.mFirstCurveEndPoint.set(bottomNavigationView.mNavigationBarWidth *10/12,
                bottomNavigationView.CURVE_CIRCLE_RADIUS
                +(bottomNavigationView.CURVE_CIRCLE_RADIUS/4));

        bottomNavigationView.mSecondCurveStartPoint = bottomNavigationView.mFirstCurveEndPoint;
        bottomNavigationView.mSecondCurveEndPoint.set((bottomNavigationView.mNavigationBarWidth *10/12)
                +(bottomNavigationView.CURVE_CIRCLE_RADIUS*2) + (bottomNavigationView.CURVE_CIRCLE_RADIUS/3),0 );

        bottomNavigationView.mFirstCurveControlPoint1.set(bottomNavigationView.mFirstCurveStartPoint.x
                        +bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4),
                bottomNavigationView.mFirstCurveStartPoint.y);

        bottomNavigationView.mFirstCurveControlPoint2.set(bottomNavigationView.mFirstCurveEndPoint.x
                        - (bottomNavigationView.CURVE_CIRCLE_RADIUS*2) + bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.mFirstCurveEndPoint.y);

        //pour le 2nd
        bottomNavigationView.mSecondCurveControlPoint1.set(bottomNavigationView.mSecondCurveStartPoint.x
                        + (bottomNavigationView.CURVE_CIRCLE_RADIUS*2) - bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.mSecondCurveStartPoint.y);
        bottomNavigationView.mSecondCurveControlPoint2.set(bottomNavigationView.mSecondCurveEndPoint.x -
                        (bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4)),
                bottomNavigationView.mSecondCurveEndPoint.y);
    }

    private void drawAnimation( final VectorMasterView fab) {
        outline = fab.getPathModelByName("outline");
        outline.setStrokeColor(Color.WHITE);
        outline.setTrimPathEnd(0.0f);
        ValueAnimator animator = ValueAnimator.ofFloat(0.0f,1.0f);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                outline.setTrimPathEnd((Float)valueAnimator.getAnimatedValue());
                fab.update();
            }
        });
        animator.start();

    }

    private void draw(int i) {
        bottomNavigationView.mFirstCurveStartPoint.set((bottomNavigationView.mNavigationBarWidth/i)
        -(bottomNavigationView.CURVE_CIRCLE_RADIUS*2) - (bottomNavigationView.CURVE_CIRCLE_RADIUS/3),0);

        bottomNavigationView.mFirstCurveEndPoint.set(bottomNavigationView.mNavigationBarWidth/i,bottomNavigationView.CURVE_CIRCLE_RADIUS
        +(bottomNavigationView.CURVE_CIRCLE_RADIUS/4));

        bottomNavigationView.mSecondCurveStartPoint = bottomNavigationView.mFirstCurveEndPoint;
        bottomNavigationView.mSecondCurveEndPoint.set((bottomNavigationView.mNavigationBarWidth/i)
        +(bottomNavigationView.CURVE_CIRCLE_RADIUS*2) + (bottomNavigationView.CURVE_CIRCLE_RADIUS/3),0 );

        bottomNavigationView.mFirstCurveControlPoint1.set(bottomNavigationView.mFirstCurveStartPoint.x
        +bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4),
                 bottomNavigationView.mFirstCurveStartPoint.y);

        bottomNavigationView.mFirstCurveControlPoint2.set(bottomNavigationView.mFirstCurveEndPoint.x
        - (bottomNavigationView.CURVE_CIRCLE_RADIUS*2) + bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.mFirstCurveEndPoint.y);

        //pour le 2nd
        bottomNavigationView.mSecondCurveControlPoint1.set(bottomNavigationView.mSecondCurveStartPoint.x
                        + (bottomNavigationView.CURVE_CIRCLE_RADIUS*2) - bottomNavigationView.CURVE_CIRCLE_RADIUS,
                bottomNavigationView.mSecondCurveStartPoint.y);
        bottomNavigationView.mSecondCurveControlPoint2.set(bottomNavigationView.mSecondCurveEndPoint.x -
                (bottomNavigationView.CURVE_CIRCLE_RADIUS + (bottomNavigationView.CURVE_CIRCLE_RADIUS/4)),
                bottomNavigationView.mSecondCurveEndPoint.y);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

}
