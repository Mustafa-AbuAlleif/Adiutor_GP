package com.example.adiutor_app.Data_Getter.Upload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.adiutor_app.Database_Connection.VolleySingleton;
import com.example.adiutor_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Uploading_Content extends AppCompatActivity {

    HashMap<String ,String >hashMap;

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnDisplay;
    Button button_selectfile ,upload ;
    Uri uri;
    EditText filename;
    String FileName;
    String ID;
    FirebaseStorage storage; // used for uploading files
    FirebaseDatabase database; // used to store URLs of uploaded files
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading__content);

        //ToDo-------

        hashMap = new HashMap<>();
        Intent intent = getIntent();
        ID = intent.getStringExtra("courseId");
        filename = findViewById(R.id.filename_ET);
        radioGroup = findViewById(R.id.radioGroup1);
        storage = FirebaseStorage.getInstance();// return an object of FireBase Storage
        database = FirebaseDatabase.getInstance();// return an object of FireBase DataBase
        button_selectfile = findViewById(R.id.file_btn);
        upload = findViewById(R.id.upload);

        button_selectfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Uploading_Content.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    select();
                }
                else
                {
                    ActivityCompat.requestPermissions(Uploading_Content.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uri!=null && radioGroup.getCheckedRadioButtonId()!=-1){
                    uploadFile(uri);
                }else {
                    Toast.makeText(Uploading_Content.this,"Missing constrain", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void uploadFile(final Uri uri) {

        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();

        final String fileName = System.currentTimeMillis()+"";
        StorageReference storageReference =storage.getReference(); // returns root path
        storageReference.child("Uploads").child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getUploadSessionUri().toString(); // return the url of your uploaded file..
                        FileName = filename.getText().toString();
                        int selectedId = radioGroup.getCheckedRadioButtonId();

                        // find the radiobutton by returned id

                        radioButton = (RadioButton) findViewById(selectedId);
                        String content;
                        if(radioButton.getText().equals("Content"))
                        {
                            content  = "others";
                        }
                        else
                        {
                            content = "sheets";
                        }

                        hashMap.put("Name",FileName);
                        hashMap.put("Url",url);
                        hashMap.put("LectureID",ID);
                        hashMap.put("contentType",content);
                        insertApi("content",hashMap);
                        DatabaseReference reference= database.getReference(); // return the path to root


                        reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // ( fileName ) This used value Name of File
                                Toast.makeText(Uploading_Content.this,"File Successfuly uploaded",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Uploading_Content.this,"File not Successfuly uploaded",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                // track the progress of = our upload..
                int currentProgress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==9 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            select();
        }else
        {
            Toast.makeText(Uploading_Content.this,"please provide permission..",Toast.LENGTH_LONG).show();
        }
    }

    private void select() {
        // to offer user to select a file using manager
        // we will be using Intent
        Intent intent= new Intent();
        // setType("*/ *") Select any type File (PDF/Word/PowrPoint/Viduo/Image ;) / .....)
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT); // to fetch files
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==86 && resultCode==RESULT_OK && data!=null)
        {
            uri=data.getData();// return the url of selected file
            System.out.println(uri);



        }else {
            Toast.makeText(Uploading_Content.this,"Please select a file",Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void insertApi(final String tableName, HashMap<String, String> data) {

        Gson gson = new Gson();
        final String insert_data = gson.toJson(data);

        System.out.println(insert_data);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://adiutorgp.000webhostapp.com/insertcontent.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error")) {
                        System.out.println(obj.getString("message"));
                        Toast.makeText(getApplicationContext(), "Failed" + obj.getString("message"), Toast.LENGTH_LONG).show();
                        //
                    } else {

                        System.out.println("Insert Successfully");

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

                        Toast.makeText(getApplicationContext(),"Failed To Upload",Toast.LENGTH_LONG).show();
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



    public void back_button(View view) {
        onBackPressed();
    }
}