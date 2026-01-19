package com.example.blue_vault;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AdminResearchAdapter extends RecyclerView.Adapter<AdminResearchAdapter.ViewHolder> {

    private List<ResearchItem> researchList;

    public AdminResearchAdapter(List<ResearchItem> researchList) {
        this.researchList = researchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_admin_item_research, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResearchItem item = researchList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvAuthor.setText(item.getAuthor());
        holder.tvSchool.setText(item.getSchool());
        holder.tvCourse.setText(item.getCourse());
        holder.tvDate.setText(item.getDate());
        holder.tvTags.setText(item.getTags()); 

        holder.btnViewResearch.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), view_research_admin.class);
            intent.putExtra("research_data", item);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return researchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvAuthor, tvSchool, tvCourse, tvDate, tvTags;
        public Button btnViewResearch;

        public ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvAuthor = view.findViewById(R.id.tvAuthor);
            tvSchool = view.findViewById(R.id.tvSchool);
            tvCourse = view.findViewById(R.id.tvCourse);
            tvDate = view.findViewById(R.id.tvDate);
            tvTags = view.findViewById(R.id.tvTags);
            btnViewResearch = view.findViewById(R.id.btnViewResearch);
        }
    }
}