package com.example.adiutor_app.Data_Getter.Notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.adiutor_app.Database_Connection.VolleySingleton;
import com.example.adiutor_app.Entities.Notif_Data;
import com.example.adiutor_app.Entities.User_Data;
import com.example.adiutor_app.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Notifications extends AppCompatActivity {
    RecyclerView recyclerView;
    int Id;
    User_Data userData;
    HashMap<String, String> notificationData;
    Integer userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification__data__show);

        //Todo-----

        notificationData = new HashMap<>();
        Intent intent = getIntent();
        userData = (User_Data) intent.getSerializableExtra("Users");
        userID = new Integer(userData.getID());
        String UserID = Integer.toString(userID);
        notificationData.put("UserID", UserID);
        selectnotifications("notification",notificationData);
        recyclerView = findViewById(R.id.schedule_RV);
    }

    public void back_button(View view) {
        onBackPressed();
    }

    public void selectnotifications(final String tableName, HashMap<String, String> conditions) {
        Gson gson = new Gson();
        final String con = gson.toJson(conditions);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://adiutorgp.000webhostapp.com/selectNotification.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error")) {
                        System.out.println(obj.getString("message"));

                    } else {
                        final ArrayList<Notif_Data> notifDataArrayList = new ArrayList<>();
                        JSONArray arr = obj.getJSONArray("notification");
                        for (int i = 0; i < arr.length(); i++) {
                            final JSONObject notifObj = (JSONObject) arr.get(i);
                            final Notif_Data N_Data = new Notif_Data();
                            N_Data.setID(notifObj.getInt("ID"));
                            N_Data.setDate("Date: " + notifObj.getString("Date"));
                            N_Data.setDescription(notifObj.getString("Description"));
                            N_Data.setImage_URL(notifObj.getString("Image_URL"));
                            notifDataArrayList.add(N_Data);
                        }
                        Notification_Data_Adapter adapter = new Notification_Data_Adapter(getApplicationContext(), notifDataArrayList);
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
                        Toast.makeText(getApplicationContext(), "Incorrect Login" + error, Toast.LENGTH_LONG).show();

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
