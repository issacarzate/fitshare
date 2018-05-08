package com.example.issac.fitnessandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RoomPic extends AppCompatActivity {
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
        setContentView(R.layout.analizar_cuarto);

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

        StorageReference filePath = ImagenRutinaReference.child("Imagenes Riesgos").child(ImageUri.getLastPathSegment() + enviarNombreRandom + ".jpg");

        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    UrlDescarga = task.getResult().getDownloadUrl().toString();
                   // postPreferences(UrlDescarga, UpdatePostButton.getText().toString());

                    postPreferences(UrlDescarga, "22e75c84-182f-4f2a-b22b-235faabec4ab");
                    Toast.makeText(RoomPic.this, "Imagen Subida con Éxito", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }else{
                    String message = task.getException().getMessage();
                    Toast.makeText(RoomPic.this, "Erro: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
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

    private void postPreferences (final String image, final String room){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://hackweb.azurewebsites.net/api/picture");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("uri", image);
                    jsonParam.put("id", room);

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
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
