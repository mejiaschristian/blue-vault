package com.example.blue_vault;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ResearchAdapter extends RecyclerView.Adapter<ResearchAdapter.ViewHolder> {

    private List<ResearchItem> researchList;

    public ResearchAdapter(List<ResearchItem> researchList) {
        this.researchList = researchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_item_research, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResearchItem item = researchList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvAuthor.setText(item.getAuthor());
        holder.tvCourse.setText(item.getCourse());
        holder.tvDate.setText(item.getDate());

        holder.btnItem.setOnClickListener(v -> {
            Log.d("ResearchAdapter", "Author: " + item.getAuthor() + ", Course: " + item.getCourse() + ", Date: " + item.getDate());
        });
    }

    @Override
    public int getItemCount() {
        return researchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvAuthor, tvCourse, tvDate;
        public Button btnItem;

        public ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvAuthor = view.findViewById(R.id.tvAuthor);
            tvCourse = view.findViewById(R.id.tvCourse);
            tvDate = view.findViewById(R.id.tvDate);
            btnItem = view.findViewById(R.id.research_item_btn);
        }
    }
}