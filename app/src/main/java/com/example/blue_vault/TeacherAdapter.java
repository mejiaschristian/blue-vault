package com.example.blue_vault;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {

    private List<TeacherItem> teacherList;
    private List<TeacherItem> teacherListFull;

    public TeacherAdapter(List<TeacherItem> teacherList) {
        this.teacherList = teacherList;
        this.teacherListFull = new ArrayList<>(teacherList);
    }

    @NonNull
    @Override
    public TeacherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_student_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherAdapter.ViewHolder holder, int position) {
        TeacherItem item = teacherList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvID.setText(item.getTeacherID());
        holder.tvDept.setText(item.getDept());
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public void filter(String dept) {
        teacherList.clear();
        if (dept.equals("ALL")) {
            teacherList.addAll(teacherListFull);
        } else {
            for (TeacherItem item : teacherListFull) {
                if (item.getDept().equals(dept)) {
                    teacherList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvID, tvDept;

        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvStudentName);
            tvID = view.findViewById(R.id.tvStudentID);
            tvDept = view.findViewById(R.id.tvStudentDept);
        }
    }
}