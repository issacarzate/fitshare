package com.example.issac.fitnessandroid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private Button registrarse;
    private Button LoginButton;
    private EditText UserEmail, UserPasword;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        registrarse = (Button) findViewById(R.id.registrarse);

        UserEmail = (EditText) findViewById(R.id.email);
        UserPasword = (EditText) findViewById(R.id.password);
        LoginButton = (Button) findViewById(R.id.email_sign_in_button);
        loadingBar = new ProgressDialog(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permitirIngreso();
            }

            private void permitirIngreso() {
                String email = UserEmail.getText().toString();
                String password = UserPasword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Por favor escribe tu email", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Por favor escribe tu contraseña", Toast.LENGTH_SHORT).show();
                }else {
                    loadingBar.setTitle("Ingresando...");
                    loadingBar.setMessage("Por favor espere en lo que conectamos al servicio...");
                    loadingBar.show();
                    //If user click on screen this bar wont disapear until it is completed
                    loadingBar.setCanceledOnTouchOutside(true);
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                loadingBar.dismiss();
                                permitirAcceso();
                                Toast.makeText(LoginActivity.this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();
                            }else{
                                String errorMessage = task.getException().getMessage();
                                loadingBar.dismiss();
                                Toast.makeText(LoginActivity.this, "Ocurrio Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        //registrarse.setOnClickListener(startActivity(new Intent(LoginActivity.this,Register.class)));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            permitirAcceso();
        }
    }

    private void permitirAcceso() {
        Intent intentoPrincipal = new Intent(LoginActivity.this, MainActivity.class);
        intentoPrincipal.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentoPrincipal);
        finish();
    }

    public void registro(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

}

