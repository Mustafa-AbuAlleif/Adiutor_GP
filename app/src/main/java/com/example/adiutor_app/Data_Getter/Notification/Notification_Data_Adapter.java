package com.example.adiutor_app.Data_Getter.Notification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adiutor_app.Entities.Notif_Data;
import com.example.adiutor_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Notification_Data_Adapter extends RecyclerView.Adapter<Notification_Data_Adapter.MyViewHolder> {

    ArrayList<Notif_Data> arr;
    Context context;
    public Notification_Data_Adapter(Context ct , ArrayList<Notif_Data> arr) {
        context = ct;
        this.arr = arr;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notification_desgin_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.datetxt.setText(arr.get(position).getDate());
        holder.description.setText(arr.get(position).getDescription());
        getimageurl(arr.get(position).getImage_URL(),holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Notification_Image.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Image_URL", arr.get(position).getImage_URL());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView datetxt,description;
        ImageView notification_image;
        ImageView img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            datetxt = itemView.findViewById(R.id.message_date);
            description = itemView.findViewById(R.id.schedule_desc);
            notification_image= itemView.findViewById(R.id.schedule_img);
            img= itemView.findViewById(R.id.schedule_img);
        }
    }
    public void getimageurl(String url , ImageView img) {
        Picasso.get().load(url).placeholder(R.drawable.loading).error(R.drawable.fail).into(img, new com.squareup.picasso.Callback() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(context,"Failed to Load Image",Toast.LENGTH_LONG).show();
            }
        });

    }

}
