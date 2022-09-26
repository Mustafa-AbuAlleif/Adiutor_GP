package com.example.adiutor_app.Data_Getter.Subjects;

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
import com.example.adiutor_app.Entities.Subject_Data;
import com.example.adiutor_app.Entities.User_Data;
import com.example.adiutor_app.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Subjects extends AppCompatActivity {
    RecyclerView recyclerView;
    User_Data userData;
    HashMap<String, String> subjectsData;
    Integer userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) &&
                    intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        subjectsData = new HashMap<>();
        recyclerView = findViewById(R.id.courses_rv);
        Intent intent = getIntent();
        userData = (User_Data) intent.getSerializableExtra("Users");
        userID = new Integer(userData.getID());
        String UserID = Integer.toString(userID);
        subjectsData.put("ID", UserID);
        selectsubject("lectures", subjectsData);
    }

    public void back_button(View view) {
        onBackPressed();
    }

    public void selectsubject(final String tableName, HashMap<String, String> conditions) {
        Gson gson = new Gson();
        final String con = gson.toJson(conditions);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://adiutorgp.000webhostapp.com/selectSubject.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error")) {
                        System.out.println(obj.getString("message"));
                        Toast.makeText(getApplicationContext(),"No Data To Load",Toast.LENGTH_LONG).show();

                    } else {
                        final ArrayList<Subject_Data> subjectArrayList = new ArrayList<>();
                        JSONArray arr = obj.getJSONArray("lectures");
                        for (int i = 0; i < arr.length(); i++) {
                            final JSONObject subjectobj = (JSONObject) arr.get(i);
                            final Subject_Data subjectData = new Subject_Data();
                            subjectData.setID(subjectobj.getInt("ID"));
                            subjectData.setName(subjectobj.getString("Name"));
                            subjectData.setPlace(subjectobj.getString("Place"));
                            subjectArrayList.add(subjectData);
                        }
                        Subject_Adapter adapter = new Subject_Adapter(getApplicationContext(), subjectArrayList);
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
