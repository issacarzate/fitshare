package com.example.issac.fitnessandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class surveilance extends AppCompatActivity {
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
        setContentView(R.layout.activity_surveilance);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        SelectPostImage = (ImageButton) findViewById(R.id.surveilanceImg);
        UpdatePostButton = (Button) findViewById(R.id.sendSurv);
        loadingBar = new ProgressDialog(this);

        ImagenRutinaReference = FirebaseStorage.getInstance().getReference();
        RefUsuario = FirebaseDatabase.getInstance().getReference().child("Users");
        //Creamos nodo surveillance
        RefRutinas = FirebaseDatabase.getInstance().getReference().child("surveillance");

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
    }

    private void ValidarInfo() {

        if(ImageUri == null){
            Toast.makeText(this, "Selecciona una imagen valida", Toast.LENGTH_SHORT).show();
        }else {
            loadingBar.setTitle("Añadiendo Escena");
            loadingBar.setMessage("Por favor espere mientras subimos su escena");
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

        StorageReference filePath = ImagenRutinaReference.child("Imagenes Escenas").child(ImageUri.getLastPathSegment() + enviarNombreRandom + ".jpg");

        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    UrlDescarga = task.getResult().getDownloadUrl().toString();
                    Toast.makeText(surveilance.this, "Imagen Subida con Éxito", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }else{
                    String message = task.getException().getMessage();
                    Toast.makeText(surveilance.this, "Erro: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
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
