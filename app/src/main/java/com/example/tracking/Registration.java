package com.example.tracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.harishpadmanabh.apppreferences.AppPreferences;

import java.util.HashMap;

public class Registration extends AppCompatActivity implements LocationListener{
    Button regbtn;
    EditText name,email,password;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    LocationManager locationManager;
    double latitude,longitude;
    AppPreferences appPreferences;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("REGISTERING.............");
        regbtn=findViewById(R.id.regg);
        name=findViewById(R.id.namexml);
        email=findViewById(R.id.emailxml);
        password=findViewById(R.id.passxml);
        mAuth = FirebaseAuth.getInstance();
        appPreferences = AppPreferences.getInstance(this, getResources().getString(R.string.app_name));

        getloction();
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                // Intent h=new Intent(Registration.this,MainActivity.class);
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {


                                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                    String uid = current_user.getUid();

                                    Log.e("USERID ", uid);
                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                                    //  String device_token = FirebaseInstanceId.getInstance().getToken();

                                    HashMap<String, String> userMap = new HashMap<>();//hashmap is used instead of params.put
                                    userMap.put("Name",name.getText().toString());
                                    userMap.put("Latitude", String.valueOf(latitude));
                                    userMap.put("Longitude",String.valueOf(longitude));

                                    appPreferences.saveData("uidd",uid);
                                    //appPreferences.saveData("keylong",String.valueOf(longitude));
                                   // Log.e("keylat",String.valueOf(latitude));


//                                    userMap.put("status", "Hi there I'm using Lapit Chat App.");
//                                    userMap.put("image", "default");
//                                    userMap.put("thumb_image", "default");
                                    //   userMap.put("device_token", device_token);

                                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){

                                                progressDialog.dismiss();
//
                                                Intent mainIntent = new Intent(Registration.this, MainActivity.class);
                                                //. mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(mainIntent);
                                                finish();

                                            }
                                            else{
                                                Toast.makeText(Registration.this, "dberror", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    });



                                }

                                else {
                                    // If sign in fails, display a message to the user.
                                    progressDialog.dismiss();

                                    Log.e("hello", "createUserWithEmail:failure");
                                    Toast.makeText(Registration.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
                //  startActivity(h);
            }

        });
    }




    private void getloction() {
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
