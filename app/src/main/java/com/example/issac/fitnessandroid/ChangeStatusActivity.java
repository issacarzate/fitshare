package com.example.issac.fitnessandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class ChangeStatusActivity extends AppCompatActivity {

    private EditText userstatus;
    private Button updateStatus;
    private ProgressDialog loadingBar;

    //Firebase
    private DatabaseReference UsersRef;
    private FirebaseAuth mAuth;

    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        userstatus = (EditText) findViewById(R.id.statusText);
        updateStatus = (Button) findViewById(R.id.updateStatusButton);
        loadingBar = new ProgressDialog(this);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateStatus();
            }
        });

    }

    private void UpdateStatus() {
        String estado = userstatus.getText().toString();

        if(TextUtils.isEmpty(estado)){
            Toast.makeText(this,"Por favor escribe el nuevo estado", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Actualizando Información");
            loadingBar.setMessage("Por favor espere mientras actualizamos tu estado");
            loadingBar.show();
            //If user click on screen this bar wont disapear until it is completed
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("estado", estado);

            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        SendUserToMainActivity();
                        Toast.makeText(ChangeStatusActivity.this, "La cuenta está completa!!", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }else {
                        String message = task.getException().getMessage();
                        Toast.makeText(ChangeStatusActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void SendUserToMainActivity() {
        Intent setUpIntent = new Intent(ChangeStatusActivity.this, MainActivity.class);
        //Usuario no puede regresas una vez creada la siguiente actividad por seguridad
        setUpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setUpIntent);
        finish();
    }
}
