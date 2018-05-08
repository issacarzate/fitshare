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
import com.example.issac.fitnessandroid.pojo.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class groupMembers extends AppCompatActivity {
    private RequestQueue mQueue;
    public ArrayList<Person> persons = new ArrayList<>();
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
        lv = (ListView) findViewById(R.id.personas);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Person entry= (Person) parent.getAdapter().getItem(position);
                Intent intent = new Intent(groupMembers.this, AddPersonFace.class);
                String message = entry.getId();
                Toast.makeText(groupMembers.this, message, Toast.LENGTH_SHORT).show();
                intent.putExtra("id", message);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        getGroups("http://hackweb.azurewebsites.net/api/peoplebygroup?id="+id);
    }
    private void getGroups (String url){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(groupMembers.this, "entre 2", Toast.LENGTH_SHORT).show();
                try {

                    JSONArray jsonArray = response.getJSONArray("peopleOutput");
                    for (int i=0 ; i<jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Person person = new Person();
                        person.id = jsonObject.getString("id");
                        person.name = jsonObject.getString("name");
                        person.groupId = jsonObject.getString("groupId");
                        persons.add(person);
                    }
                    ArrayAdapter<Person> arrayAdapter = new ArrayAdapter<Person>(
                            getApplicationContext(), android.R.layout.simple_list_item_1, persons);

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
