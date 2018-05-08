package com.example.issac.fitnessandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ShareActivity extends AppCompatActivity {
    private ImageButton SelectPostImage;
    private Button UpdatePostButton;
    private EditText PostDescription;
    private ProgressDialog loadingBar;

    private static final int imagen_galeria = 1;

    private Uri ImageUri;
    private String Rutina;

    private StorageReference ImagenRutinaReference;
    private DatabaseReference RefUsuario, RefRutinas;
    private FirebaseAuth mAuth;

    private String guardarFechaActual, guardarTiempoActual, enviarNombreRandom, UrlDescarga, current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        SelectPostImage = (ImageButton) findViewById(R.id.imagenRutina);
        UpdatePostButton = (Button) findViewById(R.id.enviarButton);
        PostDescription = (EditText) findViewById(R.id.rutinaText);
        loadingBar = new ProgressDialog(this);

        ImagenRutinaReference = FirebaseStorage.getInstance().getReference();
        RefUsuario = FirebaseDatabase.getInstance().getReference().child("Users");
        //Creamos nodo rutinas
        RefRutinas = FirebaseDatabase.getInstance().getReference().child("Rutinas");


        SelectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirGaleria();
            }
        });

        UpdatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidarInfo();
            }
        });

        PostDescription.setImeActionLabel("Enviar", KeyEvent.KEYCODE_ENTER);

        PostDescription.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    ValidarInfo();
                    return true;
                }
                return false;
            }
        });

    }

    private void ValidarInfo() {
        Rutina = PostDescription.getText().toString();

        if(ImageUri == null){
            Toast.makeText(this, "Selecciona una imagen valida", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(Rutina)){
            Toast.makeText(this, "Escribe una rutina valida", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Añadiendo Rutina");
            loadingBar.setMessage("Por favor espere mientras subimos su rutina");
            loadingBar.show();
            //If user click on screen this bar wont dissapear until it is completed
            loadingBar.setCanceledOnTouchOutside(true);
            GuardarImagenEnFirebaseStorage();
        }
    }

    private void GuardarImagenEnFirebaseStorage() {
        Calendar calendarioParaID = Calendar.getInstance();
        SimpleDateFormat fechaActual = new SimpleDateFormat("dd-MMM-yyyy");
        guardarFechaActual = fechaActual.format(calendarioParaID.getTime());
        guardarFechaActual = guardarFechaActual.replace(".","a");
        Calendar tiempoParaID = Calendar.getInstance();
        SimpleDateFormat tiempoActual = new SimpleDateFormat("HH:mm");
        guardarTiempoActual = tiempoActual.format(tiempoParaID.getTime());
        guardarTiempoActual = guardarTiempoActual.replace(".","a");

        enviarNombreRandom = guardarFechaActual + guardarTiempoActual;

        StorageReference filePath = ImagenRutinaReference.child("Imagenes Rutinas").child(ImageUri.getLastPathSegment() + enviarNombreRandom + ".jpg");

        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    UrlDescarga = task.getResult().getDownloadUrl().toString();
                    Toast.makeText(ShareActivity.this, "Imagen Subida con Éxito", Toast.LENGTH_SHORT).show();
                    GuardandoRutinaFirebase();
                }else{
                    String message = task.getException().getMessage();
                    Toast.makeText(ShareActivity.this, "Erro: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void GuardandoRutinaFirebase() {
        RefUsuario.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                  String nombreUsuario = dataSnapshot.child("fullname").getValue().toString();
                  String imagenUsuario = dataSnapshot.child("imagenPerfil").getValue().toString();

                    HashMap rutinasMap = new HashMap();
                    rutinasMap.put("uid", current_user_id);
                    rutinasMap.put("date", guardarFechaActual);
                    rutinasMap.put("tiempo", guardarTiempoActual);
                    rutinasMap.put("descripcion", Rutina);
                    rutinasMap.put("imagenRutina", UrlDescarga);
                    rutinasMap.put("imagenPerfil", imagenUsuario);
                    rutinasMap.put("fullname", nombreUsuario);

                    //Nombre unico de rutina
                    RefRutinas.child(current_user_id + enviarNombreRandom).updateChildren(rutinasMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                EnviaAInicio();
                                Toast.makeText(ShareActivity.this, "Rutina Enviada Exitosamente", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }else{
                                Toast.makeText(ShareActivity.this, "Ocurrio Error", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void EnviaAInicio() {
        Intent inicioIntent = new Intent(ShareActivity.this, MainActivity.class);
        startActivity(inicioIntent);
        finish();
    }

    private void AbrirGaleria() {
        Intent galeriaIntent = new Intent();
        galeriaIntent.setAction(Intent.ACTION_GET_CONTENT);
        galeriaIntent.setType("image/*");
        startActivityForResult(galeriaIntent, imagen_galeria);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==imagen_galeria && resultCode == RESULT_OK){
            ImageUri = data.getData();
            SelectPostImage.setImageURI(ImageUri);

        }
    }
}
