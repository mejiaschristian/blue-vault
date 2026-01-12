package com.example.blue_vault;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<StudentItem> studentList;
    private List<StudentItem> studentListFull;

    public StudentAdapter(List<StudentItem> studentList) {
        this.studentList = studentList;
        this.studentListFull = new ArrayList<>(studentList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_student_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentItem item = studentList.get(position);
        holder.tvName.setText(item.getName());
        holder.tvID.setText(item.getStudentID());
        holder.tvDept.setText(item.getDept());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void filter(String dept) {
        studentList.clear();
        if (dept.equals("ALL")) {
            studentList.addAll(studentListFull);
        } else {
            for (StudentItem item : studentListFull) {
                if (item.getDept().equals(dept)) {
                    studentList.add(item);
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