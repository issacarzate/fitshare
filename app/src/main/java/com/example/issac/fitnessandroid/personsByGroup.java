package com.example.issac.fitnessandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.issac.fitnessandroid.pojo.Groups;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class personsByGroup extends AppCompatActivity {
    private RequestQueue mQueue;
    public ArrayList<Groups> groups = new ArrayList<>();
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons_by_group);
        lv = (ListView) findViewById(R.id.groupsView2);
        getGroups("http://hackweb.azurewebsites.net/api/group");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Groups entry= (Groups) parent.getAdapter().getItem(position);
                Intent intent = new Intent(personsByGroup.this, groupMembers.class);
                String message = entry.getId();
                Toast.makeText(personsByGroup.this, message, Toast.LENGTH_SHORT).show();
                intent.putExtra("id", message);
                startActivity(intent);
            }
        });
    }

    private void getGroups (String url){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(personsByGroup.this, "entre 2", Toast.LENGTH_SHORT).show();
                try {

                    JSONArray jsonArray = response.getJSONArray("groups");
                    for (int i=0 ; i<jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Groups group = new Groups();
                        group.id = jsonObject.getString("Id");
                        group.name = jsonObject.getString("Name");
                        groups.add(group);
                    }
                    ArrayAdapter<Groups> arrayAdapter = new ArrayAdapter<Groups>(
                            getApplicationContext(), android.R.layout.simple_list_item_1, groups);

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
