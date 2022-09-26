package com.example.adiutor_app.Data_Getter.Content;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adiutor_app.Entities.ContentData;
import com.example.adiutor_app.R;

import java.util.ArrayList;

public class Content_Adapter extends RecyclerView.Adapter<Content_Adapter.MyViewHolder> {
    ArrayList<ContentData> contentDataArrayList;
    Context context;

    public Content_Adapter(Context ct, ArrayList<ContentData> contentData) {
        this.context = ct;
        this.contentDataArrayList = contentData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.content_design_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.txt1.setText(contentDataArrayList.get(position).getName());
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(contentDataArrayList.get(position).getUrl());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle(contentDataArrayList.get(position).getName());
                request.setDescription("Downloading");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //request.setVisibleInDownloadsUi(true);
                request.setDestinationUri(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
                downloadmanager.enqueue(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contentDataArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt1;
        ImageView img;
        ImageView download;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.content_name);
            img = itemView.findViewById(R.id.imageView5);
            download = itemView.findViewById(R.id.download_btn);
        }
    }
}
