package com.example.issac.fitnessandroid;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AjustesActivity extends AppCompatActivity {
    private EditText Username, Fullname, UserStatus, edittext;
    private Button SaveInfoButton, paises;
    private CircleImageView Profileimage;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;

    private ProgressDialog loadingBar;
    private Calendar myCalendar = Calendar.getInstance();

    String currentUserID, CountrySelection;

    private CountryPicker countryPicker =
            new CountryPicker.Builder().with(this)
                    .listener(new OnCountryPickerListener() {
                        @Override public void onSelectCountry(Country country) {
                            //DO something here
                            paises.setText(country.getName());
                            CountrySelection = country.getName();
                        }
                    })
                    .build();

    final static int imagen_galeria = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        mAuth = FirebaseAuth.getInstance();
        //Obtenemos el uid del usuario actual para encontrarlo en nuestros usuarios
        currentUserID = mAuth.getCurrentUser().getUid();
        //obtenemos el usuario actual desde la lista de todos los usuarios
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Imagenes Perfil");

        Username = (EditText) findViewById(R.id.setup_username);
        Fullname = (EditText) findViewById(R.id.setup_fullname);
        SaveInfoButton = (Button) findViewById(R.id.setup_information_button);
        UserStatus = (EditText) findViewById(R.id.setup_status);
        Profileimage = (CircleImageView) findViewById(R.id.setup_profile_image);
        paises = (Button) findViewById(R.id.button_country);

        loadingBar = new ProgressDialog(this);


        SaveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarInfo();
            }
        });

        Profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galeriaIntent = new Intent();
                galeriaIntent.setAction(Intent.ACTION_GET_CONTENT);
                galeriaIntent.setType("image/*");
                startActivityForResult(galeriaIntent, imagen_galeria);
            }
        });

        paises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.showDialog(getSupportFragmentManager()); // Show the dialog
            }
        });


        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("imagenPerfil")){
                    String imagen = dataSnapshot.child("imagenPerfil").getValue().toString();

                    Picasso.with(AjustesActivity.this).load(imagen).placeholder(R.drawable.profile).into(Profileimage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        edittext= (EditText) findViewById(R.id.Birthday);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                    String myFormat = "MM/dd/yy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                    edittext.setText(sdf.format(myCalendar.getTime()));
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                new DatePickerDialog(AjustesActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==imagen_galeria && resultCode == RESULT_OK && data!=null){
            Uri ImageUri = data.getData();
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(this);
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK){
                loadingBar.setTitle("Imagen de perfil");
                loadingBar.setMessage("Por favor espere mientras actualizamos su imagen de perfil");
                loadingBar.show();
                //If user click on screen this bar wont dissapear until it is completed
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();

                StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            //Si la imagen se guarda exitosamente pedimos el la liga de la imagen
                            //para guardarla en el atributo imagenPerfil de cada usuario

                            Toast.makeText(AjustesActivity.this, "Imágen actualizada exitosamente ", Toast.LENGTH_SHORT).show();
                            final String downloadUrl = task.getResult().getDownloadUrl().toString();

                            UsersRef.child("imagenPerfil").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent propioIntent = new Intent(AjustesActivity.this, AjustesActivity.class);
                                        startActivity(propioIntent);
                                        loadingBar.dismiss();
                                        Toast.makeText(AjustesActivity.this, "Imágen con referencia en base de datos", Toast.LENGTH_SHORT).show();
                                    }else {
                                        String message = task.getException().getMessage();
                                        loadingBar.dismiss();
                                        Toast.makeText(AjustesActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    }
                });


            }else {
                Toast.makeText(AjustesActivity.this, "Error: La imágen no puede recortarse", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }

    private void enviarInfo() {
        String username = Username.getText().toString();
        String fullname = Fullname.getText().toString();
        String countryname = CountrySelection.toString();
        String userStatus =  UserStatus.getText().toString();
        String birth = edittext.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "Porfavor ingrese su nombre de usuario", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(fullname)){
            Toast.makeText(this, "Porfavor ingrese su nombre completo", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(countryname)){
            Toast.makeText(this, "Porfavor ingrese su país", Toast.LENGTH_SHORT).show();
        }if(TextUtils.isEmpty(userStatus)){
            Toast.makeText(this, "Porfavor ingrese un Status", Toast.LENGTH_SHORT).show();
        }if(TextUtils.isEmpty(birth)){
            Toast.makeText(this, "Porfavor ingrese su cumpleaños", Toast.LENGTH_SHORT).show();
        }else {
            loadingBar.setTitle("Actualizando Información");
            loadingBar.setMessage("Por favor espere mientras añadimos tus datos");
            loadingBar.show();
            //If user click on screen this bar wont disapear until it is completed
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("fullname", fullname);
            userMap.put("country", countryname);
            userMap.put("Lat", "Secret");
            userMap.put("Lng", "Secreto");
            userMap.put("gender", "M");
            userMap.put("birth", birth);
            userMap.put("estado", userStatus);
            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        irActividadPrincipal();
                        Toast.makeText(AjustesActivity.this, "La cuenta está completa!!", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }else {
                        String message = task.getException().getMessage();
                        Toast.makeText(AjustesActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void irActividadPrincipal() {
        Intent principalIntent = new Intent(AjustesActivity.this, MainActivity.class);
        //Usuario no puede regresas una vez creada la siguiente actividad por seguridad
        //principalIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(principalIntent);
        finish();
    }
}