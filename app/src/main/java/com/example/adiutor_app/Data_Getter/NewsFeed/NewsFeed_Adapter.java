package com.example.adiutor_app.Data_Getter.NewsFeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adiutor_app.Entities.News_Data;
import com.example.adiutor_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsFeed_Adapter extends RecyclerView.Adapter<NewsFeed_Adapter.MyViewHolder> {
//Creating Variables that will be intialized later with the data that will be

    ArrayList<News_Data> newsfeedList;
    Context context;
    News_Data newsData;

    public NewsFeed_Adapter(Context ct, ArrayList<News_Data> notificationsList) {
        //here we set the  previous data we intialized to the Adapter Parameters that be declared into the MainActivity which is "notifications.java"
        context = ct;
        this.newsfeedList = notificationsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Here we choose to inflate the data from this class and in what design we created
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.newsfeed_designrow, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Here we set the Design to its variables that we want it to be displayed
        holder.txt1.setText(newsfeedList.get(position).getDomain());
        holder.txt2.setText(newsfeedList.get(position).getBody());
        holder.logo.setImageResource(R.drawable.academy_logo);
        holder.date.setText(newsfeedList.get(position).getDate());
        getimageurl(newsfeedList.get(position).getImage_URL(),holder.img);
    }

    @Override
    public int getItemCount() {
        //here we return the items to its array length
        return newsfeedList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt1, txt2,date;
        ImageView img;
        ImageView logo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // here we declaring a variables of the design that we created to be inflated by the recyclerView

            newsData = new News_Data();
            txt1 = itemView.findViewById(R.id.domain_txt);
            txt2 = itemView.findViewById(R.id.des_txt);
            img = itemView.findViewById(R.id.not_img);
            logo = itemView.findViewById(R.id.imageView3);
            date = itemView.findViewById(R.id.not_date);
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
