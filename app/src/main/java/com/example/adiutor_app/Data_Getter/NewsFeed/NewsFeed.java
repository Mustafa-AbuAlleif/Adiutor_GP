package com.example.adiutor_app.Data_Getter.NewsFeed;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.adiutor_app.Entities.News_Data;
import com.example.adiutor_app.R;
import com.example.adiutor_app.Database_Connection.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewsFeed extends AppCompatActivity {
    RecyclerView recyclerView;
    HashMap<String, String> newsfeeddata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        //ToDo-------

        newsfeeddata = new HashMap<>();
        recyclerView = findViewById(R.id.newsfeed_rv);

        //there should be a adapter object created from NewsFeed_Adapter class to receive
        //data and display it in our recycler view in this activity

        selectNotification("newsfeed", newsfeeddata);

    }


    public void back_button(View view) {
        onBackPressed();
    }


    public void selectNotification(final String tableName, HashMap<String, String> conditions) {
        Gson gson = new Gson();
        final String con = gson.toJson(conditions);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://adiutorgp.000webhostapp.com/selectNewsFeed.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error")) {
                        System.out.println(obj.getString("message"));

                    } else {
                        final ArrayList<News_Data> newsfeedArrayList = new ArrayList<>();
                        JSONArray arr = obj.getJSONArray("newsfeed");
                        for (int i = 0; i < arr.length(); i++) {
                            final JSONObject newsObject = (JSONObject) arr.get(i);
                            final News_Data newsData = new News_Data();
                            newsData.setID(newsObject.getInt("ID"));
                            newsData.setDomain(newsObject.getString("Domain"));
                            newsData.setBody(newsObject.getString("Body"));
                            newsData.setImage_URL(newsObject.getString("Image_URL"));
                            newsData.setDate("Date: " + newsObject.getString("Date"));
                            newsfeedArrayList.add(newsData);
                        }
                        NewsFeed_Adapter adapter = new NewsFeed_Adapter(getApplicationContext(), newsfeedArrayList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("table", tableName);
                param.put("conditions", con);

                return param;
            }
        };

        VolleySingleton.getnInstance(this).addRequestQue(stringRequest);
    }

}
