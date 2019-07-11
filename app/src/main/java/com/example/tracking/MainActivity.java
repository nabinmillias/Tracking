package com.example.tracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView reg;
    Button logbtn;
    EditText email,password;

    private FirebaseAuth mAuth;
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
        mAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.emailxml);
        password=findViewById(R.id.passxml);
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
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
}
