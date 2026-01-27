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
        // Initialize with a copy so filtering has a baseline
        this.studentListFull = new ArrayList<>(studentList);
    }

    /**
     * Call this method after fetching data from MySQL via Volley
     * to ensure the filter has the latest data.
     */
    public void updateOriginalList(List<StudentItem> newList) {
        this.studentListFull = new ArrayList<>(newList);
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
        if (dept.equalsIgnoreCase("ALL")) {
            studentList.addAll(studentListFull);
        } else {
            for (StudentItem item : studentListFull) {
                // Case-insensitive comparison is safer
                if (item.getDept() != null && item.getDept().equalsIgnoreCase(dept)) {
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