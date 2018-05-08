package com.example.issac.fitnessandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.issac.fitnessandroid.pojo.Rooms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Preferences extends AppCompatActivity {
    private RequestQueue mQueue;
    public String[] letra = {"Cuarto de máquinas","Área de Carga","Zona de peligro"};
    public ArrayList<Rooms> cuartos= new ArrayList<>();
    private Spinner spinner;
    private ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preeferences);
        lv = (ListView) findViewById(R.id.listaView);
        //getRooms("http://hackweb.azurewebsites.net/api/room");
        getRooms("http://hackweb.azurewebsites.net/api/room");
        ArrayAdapter<Rooms> adapter =
                new ArrayAdapter<Rooms>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, cuartos);
       // adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        //Spinner.setAdapter(adapter);
         //= (Spinner) findViewById(R.id.spinner);
        //spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cuartos));

        //final CheckBox casco = (CheckBox) findViewById(R.id.checkBoxCasco);
      //  final CheckBox lentes = (CheckBox) findViewById(R.id.checkBoxLentes);

        //Button enviar = (Button) findViewById(R.id.sendButton);

        //final  ArrayList<String> selectedRoom = new ArrayList<>();
        final ArrayList<String> selectedPreferences = new ArrayList<>();
        final String[] selectedRoom = new String[1];




       /* enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(casco.isChecked()){
                    //Toast.makeText(Preferences.this, "Casco Seleccionado", Toast.LENGTH_SHORT).show();
                    selectedPreferences.add("headwear");

                }
                if(lentes.isChecked()){
                    //Toast.makeText(Preferences.this, "Lentes Seleccionado", Toast.LENGTH_SHORT).show();
                    selectedPreferences.add("glasses");
                }
                //postPreferences(selectedRoom[0], selectedPreferences);
                getRooms("http://hackweb.azurewebsites.net/api/room");

            }
        });*/

        initializeUI();
        ArrayAdapter<Rooms> arrayAdapter = new ArrayAdapter<Rooms>(
                this,
                android.R.layout.simple_list_item_1, cuartos);

        lv.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                setContentView(R.layout.support_simple_spinner_dropdown_item);
                if (item != null) {
                    Toast.makeText(Preferences.this, item.toString(),
                            Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(Preferences.this, "Selected",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
    }



    private void initializeUI() {

        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<Rooms> adapter =
                new ArrayAdapter<Rooms>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, cuartos);

        spinner.setAdapter(adapter);

    }

    private void getRooms (String url){
        //Toast.makeText(Preferences.this, "Entre", Toast.LENGTH_SHORT).show();


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(Preferences.this, "entre 2", Toast.LENGTH_SHORT).show();
                try {

                    //JSONObject data = response.getJSONObject("data");
                    JSONArray jsonArray = response.getJSONArray("RoomOutputModels");
                    for (int i=0 ; i<jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Rooms room = new Rooms();
                        //marvelDude.id = jsonObject.getLong("id") + "";
                        room.roomName = jsonObject.getString("roomName");
                        room.roomDescription = jsonObject.getString("roomDescription");
                        room.glasses = jsonObject.getBoolean("glasses");
                        room.helmet = jsonObject.getBoolean("helmet");

                        cuartos.add(room);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();


        mQueue.add(request);
    }

    private void postPreferences (final String room, final ArrayList accessory){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://lenmiapi.azurewebsites.net/api/service/place");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("cuarto", room);
                    jsonParam.put("preferencias", accessory);

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
