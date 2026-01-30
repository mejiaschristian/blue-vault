package com.example.blue_vault;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {

    private List<TeacherItem> teacherList;

    public TeacherAdapter(List<TeacherItem> teacherList) {
        this.teacherList = teacherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_teacher_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TeacherItem teacher = teacherList.get(position);
        holder.tvName.setText(teacher.getName());
        holder.tvId.setText(teacher.getId());
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Replace these IDs with the actual IDs in your item_teacher.xml
            tvName = itemView.findViewById(R.id.tvTeacherName);
            tvId = itemView.findViewById(R.id.tvTeacherId);
        }
    }
}