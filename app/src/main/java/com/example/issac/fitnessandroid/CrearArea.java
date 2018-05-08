package com.example.issac.fitnessandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CrearArea extends AppCompatActivity {
    public boolean helmet = false;
    public boolean glasses = false;
    public String roomName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_area);

        final CheckBox casco = (CheckBox) findViewById(R.id.checkBoxCasco2);
        final CheckBox lentes = (CheckBox) findViewById(R.id.checkBoxLentes);

        final EditText nombre = (EditText) findViewById(R.id.roomName);

        Button crear = (Button)  findViewById(R.id.sendScene);


        final ArrayList<String> selectedPreferences = new ArrayList<>();



        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(casco.isChecked()){
                    Toast.makeText(CrearArea.this, "Casco Seleccionado", Toast.LENGTH_SHORT).show();
                    helmet = true;

                }
                if(lentes.isChecked()){
                    Toast.makeText(CrearArea.this, "Lentes Seleccionado", Toast.LENGTH_SHORT).show();
                    glasses = true;
                }
                roomName = nombre.getText().toString();
                postPreferences(roomName, helmet, glasses);
            }
        });
    }

    private void postPreferences (final String room, final boolean glasses, final boolean helmet){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://hackweb.azurewebsites.net/api/room");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("roomName", room);
                    jsonParam.put("roomDescription", "Esta es la descripcion");
                    jsonParam.put("glasses", glasses);
                    jsonParam.put("helmet", helmet);

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
}
