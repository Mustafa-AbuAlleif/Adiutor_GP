package com.example.adiutor_app.Data_Getter.QR;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.adiutor_app.Data_Getter.home_page;
import com.example.adiutor_app.Database_Connection.VolleySingleton;
import com.example.adiutor_app.Entities.User_Data;
import com.google.gson.Gson;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

// Immpelemt the Zxing scanner result handler to the class

public class qrcode_scanning extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    //Creating a variable from Zxingscanner view Liberary
    ZXingScannerView scannerView;
    User_Data userData;
    HashMap<String,String >hashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        Intent intent = getIntent();
        userData = (User_Data) intent.getSerializableExtra("userData");
        hashMap=new HashMap<>();
    }


    @Override
    public void handleResult(Result result) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        //This function gets the result from the scanning operation
        scannerView.setResultHandler(this);
        hashMap.put("UserID",Integer.toString(userData.getID()));
        hashMap.put("UserName",userData.getName());
        hashMap.put("Date",date);
        hashMap.put("geneID", result.toString());
        insertApi("Attendance",hashMap);
        onBackPressed();


    }

    @Override
    protected void onPause() {
        //This function operate when Scanning is Done to Pause using Camera

        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        //This function operate when a Scanning Operation will be done again to start using Camera again

        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
    public void insertApi(final String tableName, HashMap<String, String> data) {

        Gson gson = new Gson();
        final String insert_data = gson.toJson(data);

        System.out.println(insert_data);


        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://adiutorgp.000webhostapp.com/insertAttendance.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Toast.makeText(getApplicationContext(),"Scanned Successfully",Toast.LENGTH_LONG).show();
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
                param.put("data", insert_data);

                System.out.println(param);

                return param;
            }
        };

        VolleySingleton.getnInstance(getApplicationContext()).addRequestQue(stringRequest);
    }


}

