package com.example.adiutor_app.Data_Getter.QR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.adiutor_app.Database_Connection.VolleySingleton;
import com.example.adiutor_app.Entities.User_Data;
import com.example.adiutor_app.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class qrcode_generating extends AppCompatActivity {
    EditText edQrCode;
    Button btnGenerateQR;
    ImageView generatedQR;
    HashMap<String, String> hashMap;
    User_Data user_data;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_generateing_layout);
        generatedQR = findViewById(R.id.generatedQR);
        btnGenerateQR = findViewById(R.id.button2);
        Intent intent = getIntent();
        name = intent.getStringExtra("event");
        user_data = new User_Data();
        user_data = (User_Data) intent.getSerializableExtra("userData");
        hashMap = new HashMap<>();


        btnGenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here Insert Data or text
                btnGenerateQR.setBackgroundResource(R.drawable.btn_shape);
                btnGenerateQR.setTextColor(Color.WHITE);
                generatedQR.setImageResource(R.drawable.loading_white);
                    hashMap.put("UserName", user_data.getName());
                    hashMap.put("LectureName", name);
                    insertApi("GeneratedQR", hashMap);
            }
        });
    }

    public void back_button(View view) {
        onBackPressed();
    }

    public void insertApi(final String tableName, HashMap<String, String> data) {

        Gson gson = new Gson();
        final String insert_data = gson.toJson(data);

        System.out.println(insert_data);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://adiutorgp.000webhostapp.com/insertAttendance.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                btnGenerateQR.setBackgroundResource(R.drawable.btn_shape);
                btnGenerateQR.setText("QR Generated");
                btnGenerateQR.setTextColor(Color.WHITE);
                System.out.println(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error")) {
                        System.out.println(obj.getString("message"));
                        Toast.makeText(getApplicationContext(), "Failed" + obj.getString("message"), Toast.LENGTH_LONG).show();
                        //
                    } else {
                        final Integer lastid;
                        lastid = obj.getInt("id");
                        System.out.println(lastid);

                        String data = lastid.toString();
                        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
                        QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 1000);
                        qrgEncoder.setColorBlack(Color.BLACK);
                        qrgEncoder.setColorWhite(Color.WHITE);

                        try {

                            // Getting QR-Code as Bitmap
                            Bitmap bitmap = qrgEncoder.getBitmap();
                            // Setting Bitmap to ImageView
                            generatedQR.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                            generatedQR.setImageResource(R.drawable.fail_white);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    generatedQR.setImageResource(R.drawable.fail_white);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        generatedQR.setImageResource(R.drawable.fail_white);
                        Toast.makeText(getApplicationContext(),"Failed To Generate QR",Toast.LENGTH_LONG).show();
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
