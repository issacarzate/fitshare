package com.example.issac.fitnessandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.issac.fitnessandroid.pojo.Notification;
import com.example.issac.fitnessandroid.pojo.Rooms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity {
    private RequestQueue mQueue;
    public ArrayList<Notification> notificaciones = new ArrayList<>();
    private ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        lv = (ListView) findViewById(R.id.notificationPanel);
        getNotifications("http://hackweb.azurewebsites.net/api/notification");

    }

    private void getNotifications (String url){
        //Toast.makeText(Preferences.this, "Entre", Toast.LENGTH_SHORT).show();


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(Notifications.this, "entre 2", Toast.LENGTH_SHORT).show();
                try {

                    //JSONObject data = response.getJSONObject("data");
                    JSONArray jsonArray = response.getJSONArray("NotificationOutputModels");
                    for (int i=0 ; i<jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Notification notification = new Notification();
                        //marvelDude.id = jsonObject.getLong("id") + "";
                        notification.id = jsonObject.getString("id");
                        notification.Description = jsonObject.getString("Description");
                        notification.Date = jsonObject.getString("Date");
                        notification.RoomId = jsonObject.getString("RoomId");

                        notificaciones.add(notification);


                    }
                    ArrayAdapter<Notification> arrayAdapter = new ArrayAdapter<Notification>(
                            getApplicationContext(), android.R.layout.simple_list_item_1, notificaciones);

                    lv.setAdapter(arrayAdapter);

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
}
