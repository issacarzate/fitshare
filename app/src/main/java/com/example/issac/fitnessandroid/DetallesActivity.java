package com.example.issac.fitnessandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetallesActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;

    private CircleImageView Profileimage;

    String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        final TextView userName = (TextView) findViewById(R.id.setup_username);
        final TextView fullName = (TextView) findViewById(R.id.setup_fullname);
        final TextView country = (TextView) findViewById(R.id.setup_country_name);

        Profileimage = (CircleImageView) findViewById(R.id.setup_profile_image);


        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName.setText(dataSnapshot.child("username").getValue().toString());
                fullName.setText(dataSnapshot.child("fullname").getValue().toString());
                country.setText(dataSnapshot.child("country").getValue().toString());
                if(dataSnapshot.hasChild("imagenPerfil")){
                    String imagen = dataSnapshot.child("imagenPerfil").getValue().toString();

                    Picasso.with(DetallesActivity.this).load(imagen).placeholder(R.drawable.profile).into(Profileimage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button regresar = (Button) findViewById(R.id.setup_information_button);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToMainActivity();
            }
        });
        Button ajustar = (Button) findViewById(R.id.setinformation_button);
        ajustar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToSettingsActivity();
            }
        });
    }
    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(DetallesActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
    private void SendUserToSettingsActivity() {
        Intent ajustesIntent = new Intent(DetallesActivity.this, AjustesActivity.class);
        startActivity(ajustesIntent);
        finish();
    }
}
