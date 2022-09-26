package com.example.adiutor_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.adiutor_app.Database_Connection.VolleySingleton;
import com.example.adiutor_app.Entities.UserType;
import com.example.adiutor_app.Entities.User_Data;
import com.example.adiutor_app.Data_Getter.home_page;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Delayed;

public class MainActivity extends AppCompatActivity {
    //Here is going to be declared the variables that we are going to assign the values for it in onCreate_Main
    EditText user_email, user_password;
    String Email, Password;
    Button Signin;
    HashMap<String, String> postinguserdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ToDo-------

        user_email = findViewById(R.id.email_txt);
        user_password = findViewById(R.id.password_txt);
        Signin = findViewById(R.id.signin_btn);
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = user_email.getText().toString();
                Password = user_password.getText().toString();

                //Checking If the fileds are empty or not.......


                if (Email.isEmpty() && Password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill the blank", Toast.LENGTH_LONG).show();
                } else if (Email.isEmpty()) {

                    user_email.setError("Enter Email");
                    user_email.requestFocus();

                } else if (Password.isEmpty()) {
                    user_password.setError("Enter Password");
                    user_password.requestFocus();
                } else {
                    postinguserdata = new HashMap<>();

                    postinguserdata.put("Email", user_email.getText().toString());
                    postinguserdata.put("Password", user_password.getText().toString());

                    selectStudent("Users", postinguserdata);


                }


            }
        });


    }



    //This Function is the connection between Data Base and Mobile app using Php File.......


    public void selectStudent(final String tableName, HashMap<String, String> conditions) {
        Gson gson = new Gson();
        final String con = gson.toJson(conditions);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://adiutorgp.000webhostapp.com/selectData.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error")) {
                        System.out.println(obj.getString("message"));
                        Toast.makeText(getApplicationContext(), "Faild :" + obj.getString("message"), Toast.LENGTH_LONG).show();
                        //
                    } else {
                        JSONArray arr = obj.getJSONArray("Users");
                        final JSONObject userObject = (JSONObject) arr.get(0);
                        final User_Data userData = new User_Data();
                        userData.setID(userObject.getInt("ID"));
                        userData.setName(userObject.getString("name"));
                        userData.setEmail(userObject.getString("Email"));
                        userData.setPassword(userObject.getString("Password"));
                        userData.setUserTypeID(userObject.getInt("UserTypeID"));
                        userData.setImgUrl(userObject.getString("ImgUrl"));
                        userData.setUserTypeID(userObject.getInt("UserTypeID"));
                        Intent intent = new Intent(getApplicationContext(), home_page.class);
                        intent.putExtra("User_Data", (Serializable) userData);
                        startActivity(intent);
                        finish();
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
            //Those are the parameters that we sent in Hashmap to the Php file to Query on it and give data back to mobile
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("table", tableName);
                param.put("conditions", con);

                return param;
            }
        };
        //This is a design pattern we created only to check if there is already an existing connectiong be
        VolleySingleton.getnInstance(this).addRequestQue(stringRequest);
    }

    public void forgotpass(View view) {
        //This Method has been created just to let the person be able to be redirected to the website just to get his new password
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://learn.sha.edu.eg/login/forgot_password.php")));

    }

}
