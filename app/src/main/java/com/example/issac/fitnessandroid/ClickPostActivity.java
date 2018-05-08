package com.example.issac.fitnessandroid;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ClickPostActivity extends AppCompatActivity {

    private ImageView PstImage;
    private TextView PostDescription;
    private Button DeletePostButton, EditPostButton;

    private DatabaseReference ClickPostReferences;
    private FirebaseAuth mAuth;

    private String PostKey, currentUserID, databaseUserID, description, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);

        mAuth =FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        PostKey = getIntent().getExtras().get("PostKey").toString();
        Toast.makeText(this, PostKey, Toast.LENGTH_SHORT).show();
        ClickPostReferences = FirebaseDatabase.getInstance().getReference().child("Rutinas").child(PostKey);

        PstImage = (ImageView) findViewById(R.id.imageView2);
        PostDescription = (TextView) findViewById(R.id.textView2);
        DeletePostButton = (Button) findViewById(R.id.buttonEliminar);
        EditPostButton = (Button) findViewById(R.id.buttonEditar);

        DeletePostButton.setVisibility(View.INVISIBLE);
        EditPostButton.setVisibility(View.INVISIBLE);

        ClickPostReferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    description = dataSnapshot.child("descripcion").getValue().toString();
                    image = dataSnapshot.child("imagenRutina").getValue().toString();
                    databaseUserID = dataSnapshot.child("uid").getValue().toString();

                    PostDescription.setText(description);
                    Picasso.with(ClickPostActivity.this).load(image).into(PstImage);

                    if(currentUserID.equals(databaseUserID)){
                        DeletePostButton.setVisibility(View.VISIBLE);
                        EditPostButton.setVisibility(View.VISIBLE);
                    }

                    EditPostButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditCurrentPost(description);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DeletePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeletecCurrentPost();
                regresarPrincipal();
                Toast.makeText(ClickPostActivity.this, "Borrado con éxito!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void EditCurrentPost(String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClickPostActivity.this);
        builder.setTitle(getString(R.string.button_edit));
        final EditText input = new EditText(ClickPostActivity.this);
        input.setText(description);
        builder.setView(input);
        builder.setPositiveButton(getString(R.string.button_update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ClickPostReferences.child("descripcion").setValue(input.getText().toString());
                Toast.makeText(ClickPostActivity.this, "Actualizado con éxito!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_blue_bright);
    }


    private void DeletecCurrentPost() {
        ClickPostReferences.removeValue();
    }
    private void regresarPrincipal() {
        Intent intentoPrincipal = new Intent(ClickPostActivity.this, MainActivity.class);
        intentoPrincipal.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentoPrincipal);
        finish();
    }
}
