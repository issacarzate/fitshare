package com.example.issac.fitnessandroid;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by issac on 03/02/18.
 */

public class Tab3Ejemplos extends Fragment{

    private TextView userName, userProfName, userStatus, userCountry, userGender, userDOB;
    private CircleImageView userProfileImage;

    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;
    private Button cambiarEstado;

    private String currentUserId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        userProfileImage = (CircleImageView) rootView.findViewById(R.id.my_profile_pic);

        userName = (TextView) rootView.findViewById(R.id.my_username);
        userProfName = (TextView) rootView.findViewById(R.id.my_profile_full_name);
        userStatus = (TextView) rootView.findViewById(R.id.my_profile_status);
        userCountry = (TextView) rootView.findViewById(R.id.my_country);
        userGender = (TextView) rootView.findViewById(R.id.my_gender);
        userDOB = (TextView) rootView.findViewById(R.id.my_dob);
        cambiarEstado = (Button) rootView.findViewById(R.id.changeStatus);

        cambiarEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tutorialsIntent = new Intent(getActivity().getApplicationContext(), ChangeStatusActivity.class);
                startActivity(tutorialsIntent);
            }
        });

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String myProfileImage = dataSnapshot.child("imagenPerfil").getValue().toString();
                    String myUsername = dataSnapshot.child("username").getValue().toString();
                    String myUserProfileName = dataSnapshot.child("fullname").getValue().toString();
                    String myUserProfileCountry = dataSnapshot.child("country").getValue().toString();
                    String myUserProfileGender = dataSnapshot.child("gender").getValue().toString();
                    String myUserProfileStatus = dataSnapshot.child("estado").getValue().toString();
                    String myUserDOB = dataSnapshot.child("birth").getValue().toString();

                    Picasso.with(getActivity().getApplicationContext()).load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);

                    userName.setText("@" + myUsername);
                    //userProfName.setText("Nombre: " + myUserProfileName);
                    userProfName.append(myUserProfileName);
                    userStatus.append(myUserProfileStatus);
                    //userStatus.setText("Estado: " + myUserProfileStatus);
                    userCountry.append(myUserProfileCountry);
                    //userCountry.setText("País: " + myUserProfileCountry);
                    userGender.append(myUserProfileGender);
                    //userGender.setText("Género: " + myUserProfileGender);
                    userDOB.append(myUserDOB);
                    //userDOB.setText("Cumpleaños: " + myUserDOB);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;
    }

}
