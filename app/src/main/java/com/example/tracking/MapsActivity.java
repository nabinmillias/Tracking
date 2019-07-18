package com.example.tracking;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.harishpadmanabh.apppreferences.AppPreferences;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    AppPreferences appPreferences;
    DatabaseReference reff;
    double latd,longd;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        appPreferences = AppPreferences.getInstance(this, getResources().getString(R.string.app_name));





        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        toolbar=findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menuu);
        toolbar.setSubtitle("Map");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.users)
                {
                    Intent ne=new Intent(MapsActivity.this,Display.class);
                    startActivity(ne);
                }
                return false;
            }
        });
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        appPreferences = AppPreferences.getInstance(this, getResources().getString(R.string.app_name));
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        reff=FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                latd= Double.parseDouble(dataSnapshot.child("Latitude").getValue().toString());
                longd= Double.parseDouble(dataSnapshot.child("Longitude").getValue().toString());
                // Toast.makeText(MapsActivity.this, String.format("%s", latd), Toast.LENGTH_SHORT).show();
                //Toast.makeText(MapsActivity.this, String.format("%s", longd), Toast.LENGTH_SHORT).show();

                LatLng sydney = new LatLng(latd,longd);
                System.out.println(sydney);
                mMap.addMarker(new MarkerOptions().position(sydney).title("You are here"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(sydney)      // Sets the center of the map to Mountain View
                        .zoom(20)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Add a marker in Sydney and move the camera
         //double latd= Double.parseDouble(appPreferences.getData("keylat"));
         //double longd=Double.parseDouble(appPreferences.getData("keylong"));
      //  Toast.makeText(this, ""+(appPreferences.getData("keylat")), Toast.LENGTH_SHORT).show();

    }
}
