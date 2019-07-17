package com.example.tracking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.harishpadmanabh.apppreferences.AppPreferences;

import java.util.ArrayList;

public class Display extends AppCompatActivity {
ListView listView;
DatabaseReference dref;
 TextView texth;
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> uidarry=new ArrayList<>();
    ArrayList<String> latarry=new ArrayList<>();
    ArrayList<String> longarry=new ArrayList<>();
String Value,userid;
double lat,longg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        listView = findViewById(R.id.lstv);
        dref = FirebaseDatabase.getInstance().getReference("Users");
        final adapt apt=new  adapt();
        listView.setAdapter(apt);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Display.this, name.get(position), Toast.LENGTH_SHORT).show();
                Toast.makeText(Display.this,uidarry.get(position), Toast.LENGTH_SHORT).show();
                Toast.makeText(Display.this, latarry.get(position), Toast.LENGTH_SHORT).show();
                Toast.makeText(Display.this,longarry.get(position), Toast.LENGTH_SHORT).show();



            }
        });
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Value = dataSnapshot.child("Name").getValue().toString();
                userid=dataSnapshot.child("Uid").getValue().toString();
                lat= Double.parseDouble(dataSnapshot.child("Latitude").getValue().toString());
                longg=Double.parseDouble(dataSnapshot.child("Longitude").getValue().toString());
                name.add(Value);
                uidarry.add(userid);
                latarry.add(String.valueOf(lat));
                longarry.add(String.valueOf(longg));
                apt.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
         class adapt extends BaseAdapter{

             @Override
             public int getCount() {
                 return name.size();
             }

             @Override
             public Object getItem(int position) {
                 return position;
             }

             @Override
             public long getItemId(int position) {
                 return position;
             }

             @Override
             public View getView(int position, View convertView, ViewGroup parent) {
                 LayoutInflater infl=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 convertView=infl.inflate(R.layout.dislay,null);
                 texth=convertView.findViewById(R.id.textdislay);
                 texth.setText(name.get(position));
                 return convertView;
             }
         }




    }



