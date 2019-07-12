package com.example.tracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.harishpadmanabh.apppreferences.AppPreferences;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements LocationListener {
    TextView reg;
    Button logbtn;
    AppPreferences appPreferences;
    EditText email,password;
    private FirebaseAuth mAuth;
    LocationManager locationManager;
    double latitude,longitude;
    private DatabaseReference mDatabase;
    DatabaseReference reff;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reg=findViewById(R.id.txtreg);
        logbtn=findViewById(R.id.buttonlog);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.emailxml);
        password=findViewById(R.id.passxml);
        getlocation();

        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                    String uid = current_user.getUid();
                                    Log.e("USERID ", uid);
                                   mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                                    //  String device_token = FirebaseInstanceId.getInstance().getToken();

                                    HashMap<String, String> userMap = new HashMap<>();//hashmap is used instead of params.put

                                    userMap.put("Latitude", String.valueOf(latitude));
                                    userMap.put("Longitude",String.valueOf(longitude));

                                    mDatabase.child("Latitude").setValue(String.valueOf(latitude)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(MainActivity.this, "Lat"+String.valueOf(latitude)+"                  Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    mDatabase.child("Longitude").setValue(String.valueOf(longitude)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(MainActivity.this, "Long"+String.valueOf(longitude)+"                  Updated", Toast.LENGTH_SHORT).show();

                                        }
                                    });


                                    Toast.makeText(MainActivity.this, "Sucess!!!!", Toast.LENGTH_SHORT).show();
                                    Log.e("hi", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                   startActivity(new Intent(MainActivity.this,MapsActivity.class));
                                    // updateUI(user);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });

            }

        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(MainActivity.this,Registration.class);
                startActivity(a);
            }
        });
    }

    private void getlocation() {
        try{
            locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,5,(LocationListener) this);
        }catch (SecurityException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }
}
