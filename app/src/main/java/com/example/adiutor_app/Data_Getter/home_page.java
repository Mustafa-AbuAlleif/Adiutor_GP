package com.example.adiutor_app.Data_Getter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.adiutor_app.Data_Getter.QR.qrcode_generating;
import com.example.adiutor_app.Database_Connection.VolleySingleton;
import com.example.adiutor_app.Entities.Subject_Data;
import com.example.adiutor_app.Entities.User_Data;
import com.example.adiutor_app.R;
import com.example.adiutor_app.Data_Getter.NewsFeed.NewsFeed;
import com.example.adiutor_app.Data_Getter.QR.qrcode_scanning;
import com.example.adiutor_app.Data_Getter.Notification.Notifications;
import com.example.adiutor_app.Data_Getter.Subjects.Subjects;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class home_page extends AppCompatActivity {
    //Defining Activity variables that will be used later in onCreat Main Activity

    public static TextView Event_Data, Event_Time, Event_Place;
    ImageView User_image, QRCode;
    int request;
    TextView UserNameData, UserEmailData;
    User_Data userData;
    String url;
    HashMap<String, String> hashMap;
    Subject_Data subjectData1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //TO DO----
        //this part is for variables configurations

        UserNameData = findViewById(R.id.user_nametxt);
        UserEmailData = findViewById(R.id.user_emailtxt);
        User_image = findViewById(R.id.profile_image);
        Event_Data = findViewById(R.id.event_Data);
        Event_Time = findViewById(R.id.time_Data);
        Event_Place = findViewById(R.id.place_Data);
        QRCode = findViewById(R.id.scanqr_img);
        subjectData1 = new Subject_Data();
        hashMap = new HashMap<>();

        //this part is for Main Functions

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        get_userData();
        getimageurl();
        Integer id = userData.getID();
        String Id = Integer.toString(id);
        hashMap.put("ID", Id);
        hashMap.put("Day", dayOfTheWeek.toLowerCase());
        selectCurrent("lectures", hashMap);
        if (userData.getUserTypeID() == 1) {
            QRCode.setImageResource(R.drawable.scanqr);
        } else {
            QRCode.setImageResource(R.drawable.geneqr);

        }

    }

    private void get_userData() {

        //This Function Only Retrieve Data from The Login Activity Which Called "MainActivity.java"

        Intent intent = getIntent();
        userData = (User_Data) intent.getSerializableExtra("User_Data");
        UserNameData.setText(userData.getName());
        UserEmailData.setText(userData.getEmail());
        url = userData.getImgUrl();

    }

    public void newsfeedclicked(View view) {
        //This Method will be executed When News Feed Image will be clicked to direct to Its Activity
        startActivity(new Intent(home_page.this, NewsFeed.class));
    }

    public void subjectclicked(View view) {

        //This Method will be executed when Subject Image will be clicked to Direct to its Activity

        if (ContextCompat.checkSelfPermission(home_page.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(this, Subjects.class);
            intent.putExtra("Users", (Serializable) userData);
            startActivity(intent);
        }
        else
        {
            ActivityCompat.requestPermissions(home_page.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 9);
        }

    }

    public void scanqrclicked(View view) {
        //This Method will be executed When scanQr image will be clicked to direct to Its Activity
        if (userData.getUserTypeID() == 1) {
            if (checking_camera_permission()) {
                startActivity(new Intent(this, qrcode_scanning.class).putExtra("userData", userData));
            }

        } else {
            startActivity(new Intent(this, qrcode_generating.class).putExtra("userData", userData).putExtra("event", subjectData1.getName()));
        }
    }

    public void notificationsclicked(View view) {
        //This Method will be executed When schedule image will be clicked to direct to Its Activity
        Intent intent = new Intent(this, Notifications.class);
        intent.putExtra("Users", (Serializable) userData);
        startActivity(intent);

    }

   // This Method is Made to Request Data that needs to be displayed for users
    public void selectCurrent(final String tableName, HashMap<String, String> conditions) {
        Gson gson = new Gson();
        final String con = gson.toJson(conditions);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://adiutorgp.000webhostapp.com/selectCurrent.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error")) {
                        System.out.println(obj.getString("message"));
                        Toast.makeText(getApplicationContext(), "Failed : " + obj.getString("message"), Toast.LENGTH_LONG).show();

                        Event_Data.setText("--- ----- -----");
                        Event_Time.setText("--- ----- -----");
                        Event_Place.setText("--- ----- -----");
                    } else {
                        JSONArray arr = obj.getJSONArray("lectures");
                        final JSONObject coursesObject = (JSONObject) arr.get(0);
                        final Subject_Data subjectData = new Subject_Data();
                        subjectData.setID(coursesObject.getInt("ID"));
                        subjectData.setName(coursesObject.getString("Name"));
                        subjectData.setDay(coursesObject.getString("Day"));
                        subjectData.setTime(coursesObject.getString("Time"));
                        subjectData.setPlace(coursesObject.getString("Place"));
                        subjectData.setType(coursesObject.getString("Type"));
                        Event_Data.setText(subjectData.getName());
                        Event_Time.setText(subjectData.getTime());
                        Event_Place.setText(subjectData.getPlace());
                        subjectData1 = subjectData;
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
                        Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_LONG).show();

                    }
                }
        ) {
            //Parsing Conditions with Volley Request to the PHP files
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("table", tableName);
                param.put("conditions", con);

                return param;
            }
        };
       // Single ton for checking if there is already a connection to the Data Base or not
        // if there is no connection it will call the Volley Singleton method to open a connection to the Data Base
        VolleySingleton.getnInstance(this).addRequestQue(stringRequest);
    }

    //Method For Getting Image_Url

    public void getimageurl() {

        Picasso.get().load(url).placeholder(R.drawable.loading_white).error(R.drawable.fail_white).into(User_image, new com.squareup.picasso.Callback() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {
            }
        });

    }

    //Method for checking Camera permission ->

    public boolean checking_camera_permission() {

        //this Method just made to check on if camera permission granted to the Application or not

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, request);
            return false;
        }
    }

    //This Function  made to Exit the app from the Home Page

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press Back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}


