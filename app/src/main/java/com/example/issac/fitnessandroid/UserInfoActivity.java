package com.example.issac.fitnessandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {
    String UserKey;
    private TextView userName, userProfName, userStatus, userCountry, userGender, userDOB;
    private CircleImageView userProfileImage;
    private DatabaseReference UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        UserKey = getIntent().getExtras().get("User").toString();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(UserKey);

        userProfileImage = (CircleImageView) findViewById(R.id.my_profile_pic);

        userName = (TextView) findViewById(R.id.my_username);
        userProfName = (TextView) findViewById(R.id.my_profile_full_name);
        userStatus = (TextView) findViewById(R.id.my_profile_status);
        userCountry = (TextView) findViewById(R.id.my_country);
        userGender = (TextView) findViewById(R.id.my_gender);
        userDOB = (TextView) findViewById(R.id.my_dob);

        UserRef.addValueEventListener(new ValueEventListener() {
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

                    Picasso.with(UserInfoActivity.this).load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);

                    userName.append(myUsername);
                    //userName.setText("@" + myUsername);
                    userProfName.append(myUserProfileName);
                    //userProfName.setText("Nombre: " + myUserProfileName);
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

        //Toast.makeText(this, Pos.toString(), Toast.LENGTH_SHORT).show();

    }
}
