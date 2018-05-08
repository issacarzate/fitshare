package com.example.issac.fitnessandroid;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_task);

        Button personButton = (Button) findViewById(R.id.addperson);
        Button roomButton = (Button) findViewById(R.id.editRoom);
        Button sceneButton = (Button) findViewById(R.id.survScene);
        Button createSceneButton = (Button) findViewById(R.id.buttonNew);
        Button analizarLugarButtn = (Button) findViewById(R.id.analizarLugarButton);
        Button addGroup = (Button) findViewById(R.id.addGroup);
        FloatingActionButton addPeople = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        FloatingActionButton addPeople2 = (FloatingActionButton) findViewById(R.id.floatingActionButton2);


        personButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToAddPerson();
            }
        });
        roomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToEditArea();
            }
        });
        sceneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToSurveilance();
            }
        });
        createSceneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserAddScene();
            }
        });
        analizarLugarButtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToAnalyze();
            }
        });
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToAddGroup();
            }
        });
        addPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SenduserToAddPeople();
            }
        });
        addPeople2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SenduserToAddPeopleBG();
            }
        });
    }

    private void SendUserToAddPerson() {
        Intent ajustesIntent = new Intent(SelectTask.this, Notifications.class);
        ajustesIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(ajustesIntent);
    }
    private void SendUserToEditArea() {
        Intent ajustesIntent = new Intent(SelectTask.this, Preferences.class);
        ajustesIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(ajustesIntent);
    }
    private void SendUserToSurveilance() {
        Intent ajustesIntent = new Intent(SelectTask.this, surveilance.class);
        ajustesIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(ajustesIntent);
    }
    private void SenduserToAddPeople() {
        Intent ajustesIntent = new Intent(SelectTask.this, addPeople.class);
        ajustesIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(ajustesIntent);
    }
    private void SenduserToAddPeopleBG() {
        Intent ajustesIntent = new Intent(SelectTask.this, personsByGroup.class);
        ajustesIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(ajustesIntent);
    }
    private void SendUserAddScene() {
        Intent ajustesIntent = new Intent(SelectTask.this, CrearArea.class);
        ajustesIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(ajustesIntent);
    }
    private void SendUserToAddGroup() {
        Intent ajustesIntent = new Intent(SelectTask.this, addGroup.class);
        ajustesIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(ajustesIntent);
    }
    private void SendUserToAnalyze() {
        Intent ajustesIntent = new Intent(SelectTask.this, RoomPic.class);
        ajustesIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(ajustesIntent);
    }
}
