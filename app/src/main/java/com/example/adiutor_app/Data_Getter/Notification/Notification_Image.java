package com.example.adiutor_app.Data_Getter.Notification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.adiutor_app.R;
import com.squareup.picasso.Picasso;

public class Notification_Image extends AppCompatActivity {
    String url;
    ImageView notification_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification__image);
        notification_image = findViewById(R.id.schedule_image);

        Intent intent = getIntent();
        url = intent.getStringExtra("Image_URL");
        System.out.println(url);
       getimageurl();


    }
    public void getimageurl() {
        Picasso.get().load(url).placeholder(R.drawable.loading).error(R.drawable.fail).into(notification_image, new com.squareup.picasso.Callback() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }
    public void back_button(View view) {
        onBackPressed();
    }
}