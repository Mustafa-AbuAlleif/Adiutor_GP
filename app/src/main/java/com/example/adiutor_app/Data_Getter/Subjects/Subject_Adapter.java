package com.example.adiutor_app.Data_Getter.Subjects;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adiutor_app.Data_Getter.Content.Content;
import com.example.adiutor_app.Entities.Subject_Data;
import com.example.adiutor_app.R;

import java.util.ArrayList;


public class Subject_Adapter extends RecyclerView.Adapter<Subject_Adapter.MyViewHolder> {
    Context context;
    ArrayList<Subject_Data> arr;

    public Subject_Adapter(Context ct, ArrayList<Subject_Data> arr) {
        context = ct;
        this.arr = arr;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.subject_data_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.subject_name.setText(arr.get(position).getName());
        holder.subject_grade.setText(" Place " + arr.get(position).getPlace());
        holder.subject_image.setImageResource(R.drawable.courses);
        holder.lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(arr.get(position).getPlace());
                Intent intent = new Intent(context, Content.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("courseID", arr.get(position).getID());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subject_name, subject_grade;
        ImageView subject_image;
        LinearLayout lo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subject_name = itemView.findViewById(R.id.name_tv);
            subject_grade = itemView.findViewById(R.id.grade_tv);
            subject_image = itemView.findViewById(R.id.course_img);
            lo = itemView.findViewById(R.id.courses_parentLO);
        }

    }
}
